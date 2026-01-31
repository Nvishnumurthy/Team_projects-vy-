# src/report.py
import pandas as pd
from reportlab.lib.pagesizes import A4
from reportlab.pdfgen import canvas
from reportlab.lib.utils import simpleSplit
import os
from datetime import datetime


def write_csv_report(data: dict, out_path: str):
    """Write analysis report as CSV file."""
    rows = []
    for detail in data.get("details", []):
        rows.append({
            "message": detail.get("message"),
            "template_id": detail.get("template_id"),
            "reason": detail.get("reason"),
        })

    df = pd.DataFrame(rows)
    df.to_csv(out_path, index=False)
    return out_path


def write_pdf_report(data: dict, out_path: str):
    """Write analysis report as PDF file."""
    c = canvas.Canvas(out_path, pagesize=A4)
    w, h = A4
    margin_x = 50
    y = h - 50

    def new_page():
        nonlocal y
        c.showPage()
        c.setFont("Helvetica", 10)
        y = h - 50

    def draw_wrapped_text(text, x, y, width, font="Helvetica", size=9, leading=12):
        """Draws text wrapped to the given width."""
        c.setFont(font, size)
        lines = simpleSplit(text, font, size, width)
        for line in lines:
            if y < 70:
                new_page()
            c.drawString(x, y, line)
            y -= leading
        return y

    # Title
    c.setFont("Helvetica-Bold", 14)
    c.drawString(margin_x, y, f"Log Analyzer Report - {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    y -= 30

    # Event Info
    ev = data.get("event", {})
    c.setFont("Helvetica-Bold", 10)
    c.drawString(margin_x, y, "Event Information:")
    y -= 15
    c.setFont("Helvetica", 9)
    for k, v in ev.items():
        line = f"{k}: {v}"
        y = draw_wrapped_text(line, margin_x + 10, y, w - 100)

    # Summary
    summary = data.get("summary", {})
    y -= 15
    c.setFont("Helvetica-Bold", 10)
    c.drawString(margin_x, y, "Summary:")
    y -= 15
    c.setFont("Helvetica", 9)
    y = draw_wrapped_text(str(summary), margin_x + 10, y, w - 100)

    # Metrics
    metrics = data.get("metrics", {})
    if metrics:
        y -= 15
        c.setFont("Helvetica-Bold", 10)
        c.drawString(margin_x, y, "Metrics Analysis:")
        y -= 15
        c.setFont("Helvetica", 9)
        y = draw_wrapped_text(json_pretty(metrics), margin_x + 10, y, w - 100)

    # Details
    y -= 15
    c.setFont("Helvetica-Bold", 10)
    c.drawString(margin_x, y, "Extracted Log Details:")
    y -= 15
    for detail in data.get("details", []):
        if y < 100:
            new_page()
        c.setFont("Helvetica-Bold", 9)
        c.drawString(margin_x, y, f"Reason: {detail.get('reason', 'N/A')}")
        y -= 12
        c.setFont("Helvetica", 9)
        msg = detail.get("message") or ""
        y = draw_wrapped_text(f"Message: {msg[:500]}", margin_x + 10, y, w - 100)
        y -= 10

    # Ollama summary
    llm_summary = data.get("llm_explanation", "")
    if llm_summary:
        y -= 20
        c.setFont("Helvetica-Bold", 10)
        c.drawString(margin_x, y, "🧠 Ollama Summary:")
        y -= 15
        c.setFont("Helvetica-Oblique", 9)
        y = draw_wrapped_text(llm_summary, margin_x + 10, y, w - 100)

    c.save()
    return out_path


def json_pretty(obj):
    """Compact pretty-print helper."""
    import json
    return json.dumps(obj, indent=2, ensure_ascii=False)
