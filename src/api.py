# src/api.py
import uvicorn
import os
import tempfile
from fastapi import FastAPI, File, UploadFile
from .infer import infer_file

app = FastAPI(title="Log AI (offline)")


@app.post("/analyze")
async def analyze(file: UploadFile = File(...)):
    if not (file.filename.endswith('.log') or file.filename.endswith('.txt')):
        return {"error": "Please upload a .log or .txt file"}

    # Save uploaded file temporarily
    tmpdir = tempfile.mkdtemp()
    path = os.path.join(tmpdir, file.filename)
    content = await file.read()
    with open(path, 'wb') as f:
        f.write(content)

    try:
        report = infer_file(path)

        # Only ERROR logs, use reasons as-is from infer_file
        error_logs = report.get("details", [])

        response = {
            "summary": report["summary"],
            "errors": [
                {
                    "message": err["message"],
                    "reason": err["reason"],
                    "template_id": err["template_id"]
                }
                for err in error_logs
            ]
        }

    finally:
        # Clean up temp file
        try:
            os.remove(path)
            os.rmdir(tmpdir)
        except Exception:
            pass

    return response


if __name__ == "__main__":
    uvicorn.run("src.api:app", host="0.0.0.0", port=8000, reload=True)
