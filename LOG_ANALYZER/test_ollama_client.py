# test_ollama_client.py
from src.ollama_client import ollama_available, ask_ollama

def main():
    print(" Checking Ollama availability...")
    available = ollama_available()
    print(f"Ollama available: {available}")

    if not available:
        print(" Ollama is not available — make sure it’s installed and running.")
        print("You can verify by running:  ollama list")
        return

    print("\n Sending test prompt to model (llama2)...")
    try:
        response = ask_ollama("llama2", "Explain what log analysis means in one sentence.")
        if response:
            print("\n Response from Ollama:\n")
            print(response)
        else:
            print("\n⚠ No response received from Ollama. Try running manually:\n")
            print("   ollama run llama2 \"Explain what log analysis means in one sentence.\"")
    except Exception as e:
        print(f" Error during request: {e}")

if __name__ == "__main__":
    main()

