# 02 - Algorithms

The patterns you reuse most, and how to reason about their cost. In AI systems, search becomes retrieval, sorting becomes top-k ranking, and Big-O reasoning tells you whether a pipeline will scale beyond a toy input.

## Files In This Folder

Work through the files in this order, so you learn the runtime vocabulary before applying it.

| File | What It Teaches | Why It Matters |
|------|------------------|----------------|
| `ex01_time_complexity_notes.py` | O(1), O(n), and O(n^2) examples | teaches runtime reasoning from the shape of the code |
| `ex02_binary_search.py` | binary search on sorted input | teaches logarithmic search and boundary reasoning |
| `ex03_two_sum.py` | brute force vs hash map | shows how the right data structure improves runtime |
| `ex04_merge_sort.py` | divide-and-conquer sorting | teaches recursion, merging, and O(n log n) analysis |

## How To Run

From this folder:

```bash
python ex01_time_complexity_notes.py
python ex02_binary_search.py
python ex03_two_sum.py
python ex04_merge_sort.py
python -m unittest -v
```

The Two Sum tests run every case against both versions, and the merge sort tests compare against `sorted()` on random input with a fixed seed.

## What To Pay Attention To

- **Binary search:** the input must be sorted, and the `left <= right` condition matters. Python ints do not overflow, so `(left + right) // 2` is safe here.
- **Two Sum:** the dict version is faster because it trades memory for speed.
- **Merge sort:** each level does linear work and there are log n levels. Using `<=` in the merge step is what makes it stable.
- **Python traps:** `x in some_list` is O(n) while `x in some_set` is O(1) on average. `list.pop(0)` is O(n) while `deque.popleft()` is O(1).

## Beginner Mistakes To Avoid

- using binary search on unsorted input
- memorizing Big-O labels without tracing the loops
- optimizing before understanding the simple solution

## Practice Prompts

- `ex01_time_complexity_notes.py`: add an O(log n) function
- `ex02_binary_search.py`: return the insertion position when the target is missing
- `ex03_two_sum.py`: return all unique pairs instead of one pair
- `ex04_merge_sort.py`: sort an already sorted list and a list with duplicates

## Exit Criteria

Move on when you can explain why one algorithm is faster than another, name each algorithm's preconditions, and walk through an example by hand.

The [Java chapter README](../../Java/02-algorithms/README.md) has a longer walkthrough of how these patterns connect to AI engineering.
