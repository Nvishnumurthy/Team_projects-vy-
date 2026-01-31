# src/train.py
import os
import json
import sys
import re
from sentence_transformers import SentenceTransformer
from sklearn.cluster import KMeans
from .parse_logs import read_file_lines, split_multiline, parse_events
from .template_miner import make_miner, process_messages

MODELS_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "models")
os.makedirs(MODELS_DIR, exist_ok=True)


def get_log_files(paths):
    all_files = []
    for path in paths:
        if os.path.isfile(path) and path.endswith(".log"):
            all_files.append(path)
        elif os.path.isdir(path):
            for f in os.listdir(path):
                if f.endswith(".log"):
                    all_files.append(os.path.join(path, f))
    return all_files


def clean_text_for_reason(text: str) -> str:
    """Clean a template or raw log to form a short human-readable reason."""
    if not text:
        return "Unknown issue"

    # Remove timestamps
    text = re.sub(r"\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}(,\d+)?", "", text)
    # Remove bracketed codes like [OBC-5401]
    text = re.sub(r"\[[^\]]+\]", "", text)
    # Remove log levels and common noise
    text = re.sub(r"\b(ERROR|WARN|INFO|DEBUG|TRACE|PDT|TAG|Link|Encoder|OBC|application|read|setval)\b",
                  "", text, flags=re.IGNORECASE)
    # Remove punctuation
    text = re.sub(r"[:;,\[\]]", " ", text)
    # Collapse multiple spaces
    text = re.sub(r"\s+", " ", text).strip()

    words = text.split()
    if not words:
        return "Unknown issue"

    if words and words[0].lower() == "failed":
        words = words[1:]

    # Keep last 3–4 meaningful words
    reason_words = words[-4:]
    reason = " ".join(reason_words)

    # Append 'failed' only if not already present
    if not reason.lower().endswith("failed"):
        reason += " failed"

    return reason


def train_model(train_logs, output_file="explanations.json", n_clusters=5):
    miner = make_miner()
    model = SentenceTransformer("all-MiniLM-L6-v2")

    error_texts, error_meta = [], []

    for logfile in train_logs:
        print(f"Processing {logfile}...")
        lines = read_file_lines(logfile)
        events = split_multiline(lines)
        parsed = parse_events(events)

        templated = process_messages(miner, [p["raw"] for p in parsed])
        for p, t in zip(parsed, templated):
            if p.get("level", "").upper() == "ERROR":
                error_texts.append(p["raw"])
                error_meta.append({
                    "template_id": t["template_id"],
                    "template": t["template"]
                })

    if not error_texts:
        print("⚠️ No ERROR logs found in training data!")
        outpath = os.path.join(MODELS_DIR, output_file)
        with open(outpath, "w") as f:
            json.dump({}, f)
        return

    embeddings = model.encode(error_texts)
    kmeans = KMeans(n_clusters=min(n_clusters, len(error_texts)), random_state=42)
    labels = kmeans.fit_predict(embeddings)

    explanations = {}
    for cluster_id in set(labels):
        cluster_texts = [error_texts[i] for i in range(len(labels)) if labels[i] == cluster_id]
        templates_in_cluster = [
            error_meta[i]["template"] for i in range(len(labels))
            if labels[i] == cluster_id and error_meta[i]["template"]
        ]

        if templates_in_cluster:
            reason_text = clean_text_for_reason(templates_in_cluster[0])
        else:
            reason_text = clean_text_for_reason(" ".join(cluster_texts))

        for i in range(len(labels)):
            if labels[i] == cluster_id:
                explanations[error_meta[i]["template_id"]] = reason_text

    outpath = os.path.join(MODELS_DIR, output_file)
    with open(outpath, "w") as f:
        json.dump(explanations, f, indent=2)
    print(f"✅ Saved explanations to {outpath}")

    miner_state_path = os.path.join(MODELS_DIR, "template_miner_state.json")
    miner.save_state(miner_state_path)
    print(f"✅ Saved miner state to {miner_state_path}")


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python -m src.train <file1.log> <file2.log> ... OR <folder_path>")
        sys.exit(1)

    paths = sys.argv[1:]
    log_files = get_log_files(paths)
    if not log_files:
        print("❌ No .log files found in the provided paths!")
        sys.exit(1)

    train_model(log_files)
