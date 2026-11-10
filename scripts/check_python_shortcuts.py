"""Fail if the Python track uses a language shortcut that hides the logic being taught.

The Python track is written for learners, so it spells out loops, conditions,
and assignments instead of using one-line shortcuts (see Python/README.md).
This script enforces that rule for every .py file under Python/, tests included.

A line that needs a shortcut on purpose (for example, a demo of a shortcut's
pitfall) must say why with a trailing comment:

    shared_rows = [[0] * 3] * 3  # shortcut-ok: this pitfall is the lesson

Run from the repository root (exits with status 1 if anything is found):

    python scripts/check_python_shortcuts.py
"""

import ast
import sys
from pathlib import Path

# Built-ins whose job is to hide a loop. Flagged only when called with arguments,
# so list() and dict() with no arguments (empty containers) are still allowed.
HIDDEN_LOOP_BUILTINS = {
    "all", "any", "dict", "enumerate", "filter", "list", "map", "max", "min",
    "reversed", "set", "sorted", "sum", "tuple", "zip",
}

# Methods that hide a loop over a container or string.
HIDDEN_LOOP_METHODS = {
    "copy", "count", "extend", "index", "items", "keys", "reverse",
    "setdefault", "sort", "update", "values",
}

ALLOW_MARKER = "# shortcut-ok:"


def describe_shortcut(node: ast.AST) -> str | None:
    """Return a description if node is a shortcut, otherwise None."""
    if isinstance(node, (ast.ListComp, ast.SetComp, ast.DictComp, ast.GeneratorExp)):
        return "comprehension or generator expression; write the loop out"
    if isinstance(node, ast.IfExp):
        return "conditional expression (a if c else b); use a full if/else"
    if isinstance(node, ast.Lambda):
        return "lambda; define a named function"
    if isinstance(node, ast.NamedExpr):
        return "walrus operator (:=); assign on its own line"
    if isinstance(node, ast.Assign):
        if len(node.targets) > 1:
            return "chained assignment (a = b = c); one assignment per line"
        if isinstance(node.targets[0], (ast.Tuple, ast.List)):
            return "tuple unpacking; assign each name on its own line"
    if isinstance(node, ast.For) and isinstance(node.target, (ast.Tuple, ast.List)):
        return "tuple unpacking in a for loop; index the item instead"
    if isinstance(node, ast.Subscript) and isinstance(node.slice, ast.Slice):
        return "slicing; copy the range with a loop"
    if isinstance(node, ast.Compare):
        if len(node.ops) > 1:
            return "chained comparison (a < b < c); use two comparisons joined by and"
        is_membership = isinstance(node.ops[0], (ast.In, ast.NotIn))
        if is_membership and isinstance(node.comparators[0], (ast.Tuple, ast.List)):
            return "membership in a literal (x in (a, b)); compare with == and or"
    if isinstance(node, ast.BinOp) and isinstance(node.op, ast.Mult):
        if isinstance(node.left, (ast.List, ast.Tuple)):
            return "sequence repetition ([x] * n); build it with a loop"
        if isinstance(node.left, ast.Constant) and isinstance(node.left.value, str):
            return "string repetition (\"-\" * n); build it with a loop"
    if isinstance(node, ast.Call):
        function = node.func
        if isinstance(function, ast.Name):
            if function.id in HIDDEN_LOOP_BUILTINS and len(node.args) > 0:
                return f"built-in {function.id}() hides a loop; write the loop out"
        if isinstance(function, ast.Attribute):
            if function.attr in HIDDEN_LOOP_METHODS:
                return f".{function.attr}() hides a loop; write the loop out"
            # str.join takes one argument; Thread.join() takes none, so it is not flagged.
            if function.attr == "join" and len(node.args) > 0:
                return ".join() hides a loop; build the string with a loop"
    return None


def check_file(path: Path, shown_as: str) -> list[str]:
    source = path.read_text(encoding="utf-8")
    lines = source.splitlines()
    findings: list[str] = []
    seen: set[tuple[int, str]] = set()
    for node in ast.walk(ast.parse(source, filename=str(path))):
        description = describe_shortcut(node)
        if description is None:
            continue
        line_number = node.lineno
        if ALLOW_MARKER in lines[line_number - 1]:
            continue
        key = (line_number, description)
        if key in seen:
            continue
        seen.add(key)
        findings.append(f"{shown_as}:{line_number}: {description}")
    return findings


def main() -> int:
    root = Path(__file__).resolve().parent.parent
    python_track = root / "Python"
    files: list[Path] = []
    for path in python_track.rglob("*.py"):
        files.append(path)
    # Sorted so the report order is stable from run to run.
    files.sort()  # shortcut-ok: this script is tooling, not a lesson

    all_findings: list[str] = []
    for path in files:
        shown_as = path.relative_to(root).as_posix()
        for finding in check_file(path, shown_as):
            all_findings.append(finding)

    for finding in all_findings:
        print(finding)
    print(f"Checked {len(files)} Python files: {len(all_findings)} shortcut(s) found.")
    if len(all_findings) > 0:
        print(f'Rewrite them explicitly, or mark a deliberate one with "{ALLOW_MARKER} <reason>".')
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
