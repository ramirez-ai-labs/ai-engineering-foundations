# Python Track

This track teaches the same chapters as `Java/`, for beginners learning the material and for engineers refreshing it.

## Requirements

Python 3.10 or newer. Everything uses the standard library, so there is nothing to install.

## How The Code Is Written

The examples deliberately avoid Python shortcuts, because a one-liner can hide the logic you are here to learn. A built-in like `sum()` or `heapq.nlargest()` is the right choice in production, but it does not show how the work gets done.

So the code uses explicit loops, full `if/else` statements, one assignment per line, and plain classes with `__init__`.

Where it helps, a comment names what you would normally write once you know the concept:

```python
# Python shortcut: for index, number in enumerate(numbers)
for index in range(len(numbers)):
    print(f"[{index}]={numbers[index]}", end=" ")
```

A few features stay because they are the lesson or the safe choice:

- `collections.deque`: the queue data structure being taught
- `dict.get`: part of the hash map lesson
- `with self._lock`: guarantees a lock is released, which is easy to get wrong by hand
- type hints on function signatures, empty containers, and attributes that start as `None`, where the value alone does not show the type

## Chapters

Work through these in order. Each folder's README explains how to run its code.

1. [01-data-structures](01-data-structures/README.md): how information is stored
2. [02-algorithms](02-algorithms/README.md): how to operate on it efficiently
3. [03-systems](03-systems/README.md): how to serve it reliably
4. [04-ai-foundations](04-ai-foundations/README.md): how these ideas appear in AI systems
5. [05-mini-projects](05-mini-projects/README.md): how the pieces fit together

## How To Study Each File

1. Run the file and read its output.
2. Read the code line by line and predict the output.
3. Re-implement it without looking.
4. Rewrite it using the shortcuts named in the comments, and confirm the output stays the same.
