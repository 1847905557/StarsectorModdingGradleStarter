import argparse

from common import ROOT, iter_text_files


def to_crlf(content: bytes) -> bytes:
    normalized = content.replace(b"\r\n", b"\n").replace(b"\r", b"\n")
    return normalized.replace(b"\n", b"\r\n")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()

    changed = []
    for path in iter_text_files():
        content = path.read_bytes()
        converted = to_crlf(content)
        if converted != content:
            if not args.check:
                path.write_bytes(converted)
            changed.append(path)

    for path in changed:
        print(f"[CRLF] {path.relative_to(ROOT)}")
    print(f"CRLF converted: {len(changed)}")
    return 1 if args.check and changed else 0


if __name__ == "__main__":
    raise SystemExit(main())
