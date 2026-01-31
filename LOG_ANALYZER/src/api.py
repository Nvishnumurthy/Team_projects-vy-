# api.py
from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse
from src.analyze import analyze_event
import os

app = FastAPI(title="Log Analyzer API", version="1.0")


@app.post("/api/analyze")
async def analyze_logs(event_data: dict):
    """
    Accepts JSON input with keys: nodeid, timestamp, eventuei
    Generates CSV reports locally.
    """
    required_keys = ["nodeid", "timestamp", "eventuei"]
    for key in required_keys:
        if key not in event_data:
            raise HTTPException(status_code=400, detail=f"Missing required field: {key}")

    try:
        # ✅ Ensure report directory exists
        out_dir = "./reports"
        os.makedirs(out_dir, exist_ok=True)

        # ✅ Call the analyzer (CSV only)
        result = analyze_event(
            event_data,
            model_name="llama2",
            out_dir=out_dir
        )

        # ✅ Return relative paths (only CSV)
        return JSONResponse(content={
            "status": "success",
            "csv_report": os.path.basename(result["csv"]),
            "summary": result["report"].get("summary", {}),
            "ollama_summary": result["report"].get("llm_explanation")
        })

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error analyzing logs: {str(e)}")


@app.get("/")
def home():
    return {"message": "Log Analyzer API: POST /api/analyze with JSON input to generate reports."}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("server:app", host="0.0.0.0", port=8000, reload=True)
