# src/fetcher.py
import requests
from datetime import datetime, timedelta
from dateutil import parser
import urllib.parse

# ✅ Real MetronMS event log API base URL
LOG_API_BASE = "http://localhost:8980/metronms/api/v2/events/list"

# Base templates for metrics APIs
SNR_API_TEMPLATE = "http://192.168.66.156:8980/metronms/rest/measurements/node[{nodeid}].worpindex[1.1]"
THROUGHPUT_API_TEMPLATE = "http://192.168.66.156:8980/metronms/rest/measurements/node[{nodeid}].worpindex[1.1]"
LATENCY_API_TEMPLATE = "http://192.168.66.156:8980/metronms/rest/measurements/icmp/node[{nodeid}].responseTime[{target_ip}]"


# ✅ Helper: convert timestamp string to epoch milliseconds safely
def to_millis(timestamp_str: str) -> int:
    try:
        dt = parser.parse(timestamp_str)
        return int(dt.timestamp() * 1000)
    except Exception as e:
        print(f"[to_millis] Failed to parse timestamp '{timestamp_str}': {e}")
        return 0


def fetch_logs(nodeid: str, timestamp: str, minutes_before: int = 10, auth=None):
    """
    Fetch logs from MetronMS for the given node and time range.
    Dynamically builds query params for eventCreateTime window.
    """
    ts_ms = to_millis(timestamp)
    start_ms = ts_ms - (minutes_before * 60 * 1000)

    # Build query string for MetronMS
    query = (
        f"eventDisplay==Y;"
        f"eventSource!=syslogd;"
        f"eventCreateTime=gt={start_ms};"
        f"eventCreateTime=lt={ts_ms}"
    )

    params = {
        "_s": query,
        "ar": "glob",
        "limit": 50,
        "offset": 0,
        "order": "desc",
        "orderBy": "id"
    }

    try:
        r = requests.get(LOG_API_BASE, params=params, timeout=30, auth=auth)
        r.raise_for_status()
        logs_data = r.json()
        return logs_data.get("events", logs_data)  # handle wrapper key if present
    except Exception as e:
        print(f"[fetch_logs] Error fetching logs for node {nodeid}: {e}")
        return []


def fetch_snr(nodeid: str, start_ts: int, end_ts: int, auth=None):
    url = SNR_API_TEMPLATE.format(nodeid=nodeid)
    params = {
        "aggregation": "AVERAGE",
        "att": "lsnr,rsnr,traincab",
        "duration": "1c",
        "start": start_ts,
        "end": end_ts
    }
    r = requests.get(url, params=params, timeout=30, auth=auth)
    r.raise_for_status()
    return r.json()


def fetch_throughput(nodeid: str, start_ts: int, end_ts: int, auth=None):
    url = THROUGHPUT_API_TEMPLATE.format(nodeid=nodeid)
    params = {
        "aggregation": "AVERAGE",
        "att": "txrate,remtexrate,traincab",
        "duration": "1c",
        "start": start_ts,
        "end": end_ts
    }
    r = requests.get(url, params=params, timeout=30, auth=auth)
    r.raise_for_status()
    return r.json()


def fetch_latency(nodeid: str, target_ip: str, start_ts: int, end_ts: int, auth=None):
    url = LATENCY_API_TEMPLATE.format(nodeid=nodeid, target_ip=target_ip)
    params = {
        "aggregation": "AVERAGE",
        "relaxed": "true",
        "duration": "1c",
        "start": start_ts,
        "end": end_ts
    }
    r = requests.get(url, params=params, timeout=30, auth=auth)
    r.raise_for_status()
    return r.json()


def fetch_all_metrics(nodeid: str, timestamp: str, target_ip: str = "10.205.7.91", auth=None):
    """
    Fetch all metrics (SNR, latency, throughput) for a node over the last 60 minutes.
    """
    end_ts = to_millis(timestamp)
    start_ts = end_ts - (60 * 60 * 1000)  # 60 minutes before

    metrics = {}
    metrics["snr"] = fetch_snr(nodeid, start_ts, end_ts, auth=auth)
    metrics["latency"] = fetch_latency(nodeid, target_ip, start_ts, end_ts, auth=auth)
    metrics["throughput"] = fetch_throughput(nodeid, start_ts, end_ts, auth=auth)
    return metrics
