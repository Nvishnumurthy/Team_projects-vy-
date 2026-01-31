# src/infer.py
import os
import json
import re
from .parse_logs import read_file_lines, split_multiline, parse_events
from .template_miner import make_miner, process_messages

MODELS_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "models")
EXPL_FILE = os.path.join(MODELS_DIR, "explanations.json")


def clean_text_for_reason(text: str) -> str:
    """Cleans a template or raw log to produce a short human-readable reason."""
    if not text:
        return "Unknown issue"

    # Remove timestamps
    text = re.sub(r"\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}(,\d+)?", "", text)
    # Remove things in brackets like [OBC-5401]
    text = re.sub(r"\[[^\]]+\]", "", text)
    # Remove common log keywords/noise
    text = re.sub(r"\b(ERROR|WARN|INFO|DEBUG|TRACE|PDT|TAG|Link|Encoder|OBC|application|read|setval)\b",
                  "", text, flags=re.IGNORECASE)
    # Remove punctuation and extra symbols
    text = re.sub(r"[:;,.\[\]]", " ", text)
    # Collapse spaces
    text = re.sub(r"\s+", " ", text).strip()

    words = text.split()
    if not words:
        return "Unknown issue"

    # Remove leading 'Failed' if present
    if words[0].lower() == "failed":
        words = words[1:]

    # Take last 3–4 meaningful words
    reason_words = words[-4:]
    reason = " ".join(reason_words)

    if not reason.lower().endswith("failed"):
        reason += " failed"

    return reason


def load_explanations():
    if os.path.exists(EXPL_FILE):
        with open(EXPL_FILE, "r") as f:
            try:
                return json.load(f)
            except json.JSONDecodeError:
                print("⚠️ explanations.json is empty or invalid, using empty dict")
                return {}
    return {}


def infer_file(path):
    explanations = load_explanations()
    miner = make_miner()

    lines = read_file_lines(path)
    events = split_multiline(lines)
    parsed = parse_events(events)
    templated = process_messages(miner, [p["raw"] for p in parsed])

    report = {"summary": {"error": 0}, "details": []}

    for p, t in zip(parsed, templated):
        if p.get("level", "").upper() == "ERROR":
            report["summary"]["error"] += 1
            raw_reason = explanations.get(t["template_id"])
            reason = clean_text_for_reason(raw_reason) if raw_reason else clean_text_for_reason(p["raw"])
            report["details"].append({
                "message": p["raw"],
                "template_id": t["template_id"],
                "reason": reason
            })

    return report


if __name__ == "__main__":
    import sys

    if len(sys.argv) < 2:
        print("Usage: python -m src.infer <log_file>")
        sys.exit(1)

    log_path = sys.argv[1]

    if not os.path.exists(log_path):
        print(f"❌ File not found: {log_path}")
        sys.exit(1)

    report = infer_file(log_path)

    out_path = log_path + ".report.json"
    with open(out_path, "w") as f:
        json.dump(report, f, indent=2)

    print(f"✅ Report saved to {out_path}")
    print(f"Total ERROR logs found: {report['summary']['error']}")
