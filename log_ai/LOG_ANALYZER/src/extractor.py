# src/extractor.py
import re
from dateutil import parser
from datetime import timedelta

THERMAL_RE = re.compile(r'(\d+(?:\.\d+)?)\s*[CF]\b[,;]?\s*(\d+(?:\.\d+)?)\s*[CF]?', re.IGNORECASE)
# Example: matches "66 C, 112.00 F" or "66C 112.00F"


def parse_log_obj(log):
    """Normalize log item. Accepts either string or dict."""
    if isinstance(log, str):
        return {"timestamp": None, "message": log}
    if isinstance(log, dict):
        return {
            "timestamp": log.get("timestamp"),
            "message": log.get("message") or log.get("raw") or ""
        }
    return {"timestamp": None, "message": str(log)}


def extract_records(logs, reboot_ts):
    """Extract registered, node down, and thermal records."""
    parsed_logs = [parse_log_obj(l) for l in logs]

    for p in parsed_logs:
        ts = p.get("timestamp")
        try:
            p["_dt"] = parser.parse(ts) if ts else None
        except Exception:
            p["_dt"] = None

    reboot_dt = parser.parse(reboot_ts)

    # Registered record nearest to reboot
    registered = [p for p in parsed_logs if 'registered' in (p['message'] or '').lower()]
    nearest_registered = (
        sorted(registered, key=lambda x: abs((x['_dt'] - reboot_dt).total_seconds()) if x['_dt'] else float('inf'))[0]
        if registered else None
    )

    # Node down near reboot
    node_down = [p for p in parsed_logs if any(term in (p['message'] or '').lower()
                                               for term in ['node down', 'down', 'unreachable'])]
    node_down_near = [p for p in node_down if p['_dt'] and abs((p['_dt'] - reboot_dt).total_seconds()) <= 600]
    node_down_result = node_down_near or node_down

    # Thermal logs near reboot
    thermal_logs = []
    for p in parsed_logs:
        m = THERMAL_RE.search(p['message'] or '')
        if m:
            thermal_logs.append({"log": p, "match": m.groups()})

    thermal_nearest = (
        sorted(thermal_logs, key=lambda x: abs((x['log']['_dt'] - reboot_dt).total_seconds())
               if x['log']['_dt'] else float('inf'))[0]
        if thermal_logs else None
    )

    return {
        "nearest_registered": nearest_registered,
        "node_down": node_down_result,
        "thermal": thermal_nearest,
    }
