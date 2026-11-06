"""Big-O refresher - what we mean by time (and space) complexity.

O(1)       - constant: list index, dict lookup (average case)
O(log n)   - logarithmic: binary search
O(n)       - linear: single loop over input
O(n log n) - linearithmic: merge sort, heap sort, sorted()
O(n^2)     - quadratic: two nested loops over input
O(2^n)     - exponential: naive recursion (e.g. fib without memo)

We usually care about worst case; sometimes average (e.g. hash table).

Python-specific traps: ``x in some_list`` is O(n) while ``x in some_set`` is
O(1) average, and ``list.pop(0)`` is O(n) while ``deque.popleft()`` is O(1).
"""


def get_first(arr: list[int]) -> int:
    """O(1) - constant time. Returns -1 for an empty list."""
    if len(arr) == 0:
        return -1
    return arr[0]


def total(arr: list[int]) -> int:
    """O(n) - one pass.

    Python shortcut: sum(arr)
    """
    running_total = 0
    for value in arr:
        running_total += value
    return running_total


def count_pairs(arr: list[int]) -> int:
    """O(n^2) - two nested passes."""
    count = 0
    n = len(arr)
    for i in range(n):
        for j in range(i + 1, n):
            count += 1
    return count


def main() -> None:
    arr = [1, 2, 3, 4, 5]
    print(f"get_first: {get_first(arr)}")
    print(f"total: {total(arr)}")
    print(f"count_pairs (n choose 2): {count_pairs(arr)}")


if __name__ == "__main__":
    main()
