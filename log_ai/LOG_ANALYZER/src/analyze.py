# src/analyze.py
import json
import os
from requests.auth import HTTPBasicAuth
from src.fetcher import fetch_logs, fetch_all_metrics, to_millis
from src.extractor import extract_records
from src.metrics import analyze_metrics
from src.ollama_client import ask_ollama
from src.report import write_csv_report

# ✅ Add OpenNMS authentication info
OPENNMS_USER = os.getenv("OPENNMS_USER", "admin")
OPENNMS_PASS = os.getenv("OPENNMS_PASS", "admin")


def analyze_event(event_data: dict, model_name="ollama-model", out_dir="."):
    nodeid = event_data["nodeid"]
    ts = event_data["timestamp"]

    # Convert timestamp to milliseconds for API calls
    ts_ms = to_millis(ts)

    # Ensure output directory exists
    os.makedirs(out_dir, exist_ok=True)

    # Load or initialize persistent per-log summary cache
    cache_file = os.path.join(out_dir, "summary_cache.json")
    if os.path.exists(cache_file):
        try:
            with open(cache_file, "r") as f:
                summary_cache = json.load(f)
        except Exception:
            summary_cache = {}
    else:
        summary_cache = {}

    def summarize_log_message(message: str):
        if not message:
            return "No message content available."
        if message in summary_cache:
            return summary_cache[message]
        try:
            summary = ask_ollama(model_name,
                f"Summarize this log message in one short sentence:\n\n{message}"
            ).strip()
        except Exception:
            summary = "Summary unavailable (Ollama error)."
        summary_cache[message] = summary
        return summary

    # ✅ Pass authentication to data fetching functions
    auth = HTTPBasicAuth(OPENNMS_USER, OPENNMS_PASS)

    # Fetch logs and extract records
    logs = fetch_logs(nodeid, ts, minutes_before=10, auth=auth)
    extracted = extract_records(logs, ts)

    # Fetch metrics
    metrics_raw = fetch_all_metrics(nodeid, ts, auth=auth)
    metrics_analysis = analyze_metrics(metrics_raw)

    # Build detailed report
    details = []

    if extracted.get("nearest_registered"):
        r = extracted["nearest_registered"]
        msg = r.get("message", "")
        details.append({
            "message": msg,
            "template_id": str(r.get("_dt", "na")),
            "reason": "registered record",
            "summary": summarize_log_message(msg)
        })

    for nd in extracted.get("node_down", []):
        msg = nd.get("message", "")
        details.append({
            "message": msg,
            "template_id": str(nd.get("_dt", "na")),
            "reason": "node down",
            "summary": summarize_log_message(msg)
        })

    if extracted.get("thermal"):
        t = extracted["thermal"]
        log = t.get("log", {})
        msg = log.get("message", "")
        details.append({
            "message": msg,
            "template_id": str(log.get("_dt", "na")),
            "reason": f"thermal {t.get('match', '')}",
            "summary": summarize_log_message(msg)
        })

    summary = {"error_count": len(details)}

    report_obj = {
        "summary": summary,
        "details": details,
        "metrics": metrics_analysis,
        "event": event_data
    }

    # Overall explanation from Ollama
    prompt = (
    "You are a log analyzer. Given the event, extracted logs, and metrics, "
    "summarize the likely root cause in one short, single-line sentence (no bullet points, no extra newlines).\n\n"
    f"Event:\n{json.dumps(event_data, indent=2)}\n\n"
    f"Extracted logs:\n{json.dumps(details, indent=2)}\n\n"
    f"Metrics:\n{json.dumps(metrics_analysis, indent=2)}"
    )
    try:
        explanation = ask_ollama(model_name, prompt).replace("\n", " ").strip()
        # keep only first 180 chars to ensure concise single line
        if len(explanation) > 180:
            explanation = explanation[:180].rstrip() + "..."
    except Exception:
        explanation = "Summary unavailable (Ollama error)."

    report_obj["llm_explanation"] = explanation


    # Generate filename using nodeid and native timestamp
    safe_ts = ts.replace(":", "-").replace(" ", "_")
    csv_path = os.path.join(out_dir, f"{nodeid}_{safe_ts}.analysis.csv")

    # Write only CSV report
    write_csv_report(report_obj, csv_path)

    # Save updated summary cache
    with open(cache_file, "w") as f:
        json.dump(summary_cache, f, indent=2)

    print(f"✅ CSV report saved in: {os.path.abspath(csv_path)}")

    return {"csv": csv_path, "report": report_obj}
