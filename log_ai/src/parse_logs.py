import re
from datetime import datetime

LINE_REGEX = re.compile(r'^\[(.*?)\]\s+\[(.*?)\]\s+(\w+):\s+(.*)$')

def read_file_lines(path):
    with open(path, 'r', errors='ignore') as f:
        return f.readlines()

def split_multiline(lines):
    """
    Group stack traces / multi-line entries: new entry starts with a line matching LINE_REGEX.
    """
    events = []
    buffer = []
    for line in lines:
        if LINE_REGEX.match(line):
            if buffer:
                events.append(''.join(buffer).rstrip())
            buffer = [line]
        else:
            buffer.append(line)
    if buffer:
        events.append(''.join(buffer).rstrip())
    return events

def parse_line(line):
    """
    Return dict: timestamp, service, level, message (raw)
    """
    m = LINE_REGEX.match(line.strip())
    if not m:
        return {'timestamp': None, 'service': None, 'level': 'OTHER', 'raw': line.strip()}
    ts, service, level, msg = m.groups()
    level_norm = level.upper()
    return {'timestamp': ts, 'service': service, 'level': level_norm, 'raw': msg.strip()}

def parse_events(raw_events):
    """
    raw_events: list of raw multiline event strings
    returns: list of dicts with parsed fields + original raw full line
    """
    parsed = []
    for r in raw_events:
        first_line = r.splitlines()[0] if r else r
        p = parse_line(first_line)
        p['raw'] = r
        parsed.append(p)
    return parsed
