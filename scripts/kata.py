"""Practice a Python lesson from a blank: copy it with every function body removed, then test your rewrite.

The lesson in Python/ is never modified. `start` writes a blanked copy of the
module and its test file to practice/<chapter>/, which is gitignored, so a
half-finished rewrite cannot be committed by accident. Signatures, docstrings,
imports, constants, and class fields (dataclass/NamedTuple annotations) are
kept, because they are the spec. Function bodies, including __init__, become
`raise NotImplementedError("<its name>")`, and the comments inside them are dropped, because
they are hints. Code under practice/ is not checked by
check_python_shortcuts.py, so write idiomatic Python there.

Run from the repository root:

    python scripts/kata.py list                                     # lessons and practice status
    python scripts/kata.py start Python/03-systems/ex05_lru_cache.py
    python scripts/kata.py start ex05_lru_cache --hard              # also delete private helpers
    python scripts/kata.py test ex05_lru_cache                      # run the lesson's tests on your copy
    python scripts/kata.py diff ex05_lru_cache                      # compare with the lesson, after you pass

A lesson can be named by its path (in Python/ or practice/) or by its module
name if that name is unique. `start` refuses to overwrite a practice file you
are working on unless you pass --force.
"""

from __future__ import annotations

import argparse
import ast
import difflib
import shutil
import subprocess
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent
LESSONS_ROOT = REPO_ROOT / "Python"
PRACTICE_ROOT = REPO_ROOT / "practice"


def lessons() -> list[Path]:
    """Every lesson module that has a test beside it, in reading order."""
    found = []
    for module in sorted(LESSONS_ROOT.glob("0*/ex*.py")):
        if (module.parent / f"test_{module.name}").exists():
            found.append(module)
    return found


def resolve_lesson(name: str) -> Path:
    """Map a path or module name to the lesson file in Python/. Exits on bad input."""
    stem = Path(name).stem
    if stem.startswith("test_"):
        stem = stem[len("test_"):]
    candidate = Path(name)
    if candidate.suffix == ".py" or "/" in name or "\\" in name:
        resolved = (REPO_ROOT / candidate).resolve() if not candidate.is_absolute() else candidate.resolve()
        for root in (LESSONS_ROOT, PRACTICE_ROOT):
            if resolved.is_relative_to(root):
                relative = resolved.relative_to(root)
                if len(relative.parts) != 2:
                    break
                lesson = LESSONS_ROOT / relative.parts[0] / f"{stem}.py"
                if lesson in lessons():
                    return lesson
        sys.exit(f"error: {name} is not a tested lesson under Python/<chapter>/. Try: python scripts/kata.py list")

    matches = [lesson for lesson in lessons() if lesson.stem == stem]
    if len(matches) == 1:
        return matches[0]
    if not matches:
        sys.exit(f"error: no tested lesson named {stem}. Try: python scripts/kata.py list")
    options = ", ".join(str(m.relative_to(REPO_ROOT).as_posix()) for m in matches)
    sys.exit(f"error: {stem} is ambiguous ({options}); pass the path instead")


def practice_path(lesson: Path) -> Path:
    return PRACTICE_ROOT / lesson.parent.name / lesson.name


def is_private(name: str) -> bool:
    return name.startswith("_") and not (name.startswith("__") and name.endswith("__"))


def _functions_to_edit(tree: ast.Module, hard: bool) -> tuple[list[tuple[ast.AST, str]], list[ast.AST]]:
    """Outermost functions to blank (with qualified names), and in hard mode the private helpers to delete."""
    blank: list[tuple[ast.AST, str]] = []
    delete: list[ast.AST] = []

    def visit(body: list[ast.stmt], prefix: str) -> None:
        for node in body:
            if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef)):
                if hard and is_private(node.name):
                    delete.append(node)
                else:
                    blank.append((node, prefix + node.name))
            elif isinstance(node, ast.ClassDef):
                visit(node.body, f"{prefix}{node.name}.")
            elif isinstance(node, ast.If):
                # Reaches functions under `if TYPE_CHECKING:` and similar; the
                # `if __name__ == "__main__":` guard only calls main(), which is blanked.
                visit(node.body, prefix)
                visit(node.orelse, prefix)

    visit(tree.body, "")
    return blank, delete


def blank_source(source: str, hard: bool = False) -> str:
    """Return source with each function body replaced by its docstring plus a NotImplementedError."""
    tree = ast.parse(source)
    lines = source.splitlines(keepends=True)
    blank, delete = _functions_to_edit(tree, hard)

    # (first line, last line, replacement lines), 1-indexed and inclusive.
    edits: list[tuple[int, int, list[str]]] = []
    for node, qualname in blank:
        blank_body = f'raise NotImplementedError("{qualname}")'
        first_stmt = node.body[0]
        keeps_docstring = (
            isinstance(first_stmt, ast.Expr)
            and isinstance(first_stmt.value, ast.Constant)
            and isinstance(first_stmt.value.value, str)
        )
        if first_stmt.lineno == node.lineno:
            # One-line function such as `def f(): return 1`: cut the line at the body.
            header = lines[node.lineno - 1]
            edits.append((node.lineno, node.end_lineno, [header[: first_stmt.col_offset] + blank_body + "\n"]))
            continue
        indent = " " * first_stmt.col_offset
        replacement = [f"{indent}{blank_body}\n"]
        if keeps_docstring:
            # Keep the docstring lines, replace everything after them. A
            # docstring-only body gives an empty range, so the raise is inserted.
            edits.append((first_stmt.end_lineno + 1, node.end_lineno, replacement))
        else:
            edits.append((first_stmt.lineno, node.end_lineno, replacement))

    for node in delete:
        start = min([node.lineno] + [d.lineno for d in node.decorator_list])
        # Also drop the comment lines directly above a deleted helper.
        while start > 1 and lines[start - 2].strip().startswith("#"):
            start -= 1
        edits.append((start, node.end_lineno, []))

    for first, last, replacement in sorted(edits, reverse=True):
        lines[first - 1 : last] = replacement

    result = "".join(lines)
    ast.parse(result)  # Fail loudly here rather than hand the learner a broken file.
    return result


def cmd_list(_: argparse.Namespace) -> None:
    for lesson in lessons():
        copy = practice_path(lesson)
        status = "in practice/" if copy.exists() else ""
        print(f"{lesson.relative_to(REPO_ROOT).as_posix():60} {status}")


def cmd_start(args: argparse.Namespace) -> None:
    lesson = resolve_lesson(args.lesson)
    target = practice_path(lesson)
    if target.exists() and not args.force:
        sys.exit(
            f"error: {target.relative_to(REPO_ROOT).as_posix()} already exists and may hold your work.\n"
            "Keep going with `test`, or pass --force to start over from a blank."
        )
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(blank_source(lesson.read_text(encoding="utf-8"), hard=args.hard), encoding="utf-8")
    shutil.copyfile(lesson.parent / f"test_{lesson.name}", target.parent / f"test_{lesson.name}")
    print(f"Blank copy: {target.relative_to(REPO_ROOT).as_posix()}")
    print(f"Tests:      {(target.parent / f'test_{lesson.name}').relative_to(REPO_ROOT).as_posix()} (read them for the spec)")
    print(f"When ready: python scripts/kata.py test {lesson.stem}")


def cmd_test(args: argparse.Namespace) -> None:
    lesson = resolve_lesson(args.lesson)
    target = practice_path(lesson)
    if not target.exists():
        sys.exit(f"error: no practice copy yet. Run: python scripts/kata.py start {lesson.stem}")
    # Copy the test fresh each run so editing it can't make a rewrite pass.
    shutil.copyfile(lesson.parent / f"test_{lesson.name}", target.parent / f"test_{lesson.name}")
    command = [sys.executable, "-m", "unittest", "-v", f"test_{lesson.stem}"]
    sys.exit(subprocess.run(command, cwd=target.parent, check=False).returncode)


def cmd_diff(args: argparse.Namespace) -> None:
    lesson = resolve_lesson(args.lesson)
    target = practice_path(lesson)
    if not target.exists():
        sys.exit(f"error: no practice copy yet. Run: python scripts/kata.py start {lesson.stem}")
    diff = difflib.unified_diff(
        lesson.read_text(encoding="utf-8").splitlines(keepends=True),
        target.read_text(encoding="utf-8").splitlines(keepends=True),
        fromfile=f"lesson/{lesson.name}",
        tofile=f"yours/{lesson.name}",
    )
    sys.stdout.writelines(diff)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    sub = parser.add_subparsers(dest="command", required=True)

    sub.add_parser("list", help="list tested lessons and which ones are in practice/").set_defaults(func=cmd_list)

    start = sub.add_parser("start", help="write a blanked copy of a lesson to practice/")
    start.add_argument("lesson", help="path or module name, e.g. ex05_lru_cache")
    start.add_argument("--hard", action="store_true", help="also delete private helpers (_name), not just their bodies")
    start.add_argument("--force", action="store_true", help="overwrite an existing practice copy")
    start.set_defaults(func=cmd_start)

    test = sub.add_parser("test", help="run the lesson's tests against your practice copy")
    test.add_argument("lesson")
    test.set_defaults(func=cmd_test)

    diff = sub.add_parser("diff", help="show how your copy differs from the lesson")
    diff.add_argument("lesson")
    diff.set_defaults(func=cmd_diff)

    args = parser.parse_args()
    args.func(args)


if __name__ == "__main__":
    main()
