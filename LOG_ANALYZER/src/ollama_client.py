# src/ollama_client.py
import subprocess
import shutil

def ollama_available():
    """Check if the ollama binary is available in PATH."""
    return shutil.which("ollama") is not None


def ask_ollama(model_name: str, prompt: str, timeout: int = 30):
    """
    Runs: ollama run <model_name> "<prompt>"
    Returns the model's text response as a string, or None if an error occurs.
    """
    if not ollama_available():
        print("❌ Ollama not found in PATH.")
        return None

    try:
        # Run the Ollama model directly with prompt as argument
        proc = subprocess.run(
            ["ollama", "run", model_name, prompt],
            capture_output=True,
            text=True,
            timeout=timeout
        )

        if proc.returncode != 0:
            print(f"⚠️ Ollama error: {proc.stderr.strip()}")
            return None

        return proc.stdout.strip()

    except subprocess.TimeoutExpired:
        print("Ollama request timed out.")
        return None
    except Exception as e:
        print(f"Exception in ask_ollama: {e}")
        return None
