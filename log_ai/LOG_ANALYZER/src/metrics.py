# src/metrics.py
import statistics

def analyze_metrics(metrics_response):
    """
    Expected structure from MetronMS APIs:
    {
      "measurements": [
        {
          "columns": ["timestamp", "lsnr", "rsnr", "traincab"],
          "values": [
            [1760504400000, 25.3, 24.8, 12],
            [1760508000000, 24.9, 24.1, 11]
          ]
        }
      ]
    }
    """
    res = {}

    for key, metric in metrics_response.items():
        # MetronMS puts data in 'measurements'
        measurements = metric.get("measurements", [])
        if not measurements:
            res[key] = {"current": None, "avg": None, "min": None, "max": None}
            continue

        numeric_values = []
        for m in measurements:
            values = m.get("values", [])
            for row in values:
                # Ignore timestamp (first element)
                for val in row[1:]:
                    if isinstance(val, (int, float)):
                        numeric_values.append(val)

        if not numeric_values:
            res[key] = {"current": None, "avg": None, "min": None, "max": None}
            continue

        current = numeric_values[-1]
        avg = statistics.mean(numeric_values)
        minimum = min(numeric_values)
        maximum = max(numeric_values)

        res[key] = {
            "current": current,
            "avg": avg,
            "min": minimum,
            "max": maximum,
            "is_min": current == minimum,
            "is_max": current == maximum,
            "pct_from_avg": ((current - avg) / avg * 100) if avg else None
        }

    return res
