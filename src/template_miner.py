import os
from drain3 import TemplateMiner
from drain3.template_miner_config import TemplateMinerConfig
from drain3.file_persistence import FilePersistence

MODELS_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), 'models')
STATE_FILE = os.path.join(MODELS_DIR, 'template_miner_state.json')

def make_miner(state_file=STATE_FILE):
    """
    Creates a TemplateMiner instance with FilePersistence.
    State will automatically be saved/loaded from STATE_FILE.
    """
    os.makedirs(os.path.dirname(state_file), exist_ok=True)

    cfg = TemplateMinerConfig()
    cfg.load({})  

    persistence = FilePersistence(state_file)

    miner = TemplateMiner(persistence, config=cfg)

    print(f"Drain3 miner initialized with persistence at {state_file}")
    return miner


def process_messages(miner, messages):
    """
    messages: list of raw message strings
    returns list of dicts: {'raw', 'template', 'template_id'}
    """
    results = []
    for msg in messages:
        r = miner.add_log_message(msg)
        template = r.get('template') if isinstance(r, dict) else getattr(r, 'template', None)
        cluster_id = r.get('cluster_id') if isinstance(r, dict) else getattr(r, 'cluster_id', None)
        results.append({'raw': msg, 'template': template, 'template_id': str(cluster_id)})
    return results


def save_state(miner, state_file=STATE_FILE):
    """
    With FilePersistence, saving is automatic.
    This function exists only for backward compatibility.
    """
    print(f"Drain3 state is already auto-saved in {state_file}")
