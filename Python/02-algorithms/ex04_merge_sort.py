"""Merge sort - divide and conquer, O(n log n), stable.

Recursion: split in half, sort each half, merge. Good for understanding
recurrence relations and why we get n log n.

This version returns a new list (O(n) extra space) rather than changing its
input. Python's built-in ``sorted`` uses Timsort, a merge-sort hybrid.
"""


def copy_range(arr: list[int], start: int, end: int) -> list[int]:
    """Return a new list holding arr[start] up to, but not including, arr[end].

    Python shortcut: arr[start:end]
    """
    result: list[int] = []
    for index in range(start, end):
        result.append(arr[index])
    return result


def merge(left: list[int], right: list[int]) -> list[int]:
    """Merge two sorted lists into one sorted list."""
    merged: list[int] = []
    left_index = 0
    right_index = 0

    while left_index < len(left) and right_index < len(right):
        # <= keeps equal elements in original order, which makes the sort stable.
        if left[left_index] <= right[right_index]:
            merged.append(left[left_index])
            left_index += 1
        else:
            merged.append(right[right_index])
            right_index += 1

    # One side is exhausted; copy whatever remains on the other.
    while left_index < len(left):
        merged.append(left[left_index])
        left_index += 1
    while right_index < len(right):
        merged.append(right[right_index])
        right_index += 1
    return merged


def merge_sort(arr: list[int]) -> list[int]:
    """Return a sorted copy of arr."""
    # Base case: a list with zero or one items is already sorted.
    if len(arr) <= 1:
        return copy_range(arr, 0, len(arr))

    mid = len(arr) // 2
    left_half = copy_range(arr, 0, mid)
    right_half = copy_range(arr, mid, len(arr))

    sorted_left = merge_sort(left_half)
    sorted_right = merge_sort(right_half)
    return merge(sorted_left, sorted_right)


def main() -> None:
    arr = [38, 27, 43, 3, 9, 82, 10]
    print(f"Before: {arr}")
    print(f"After:  {merge_sort(arr)}")


if __name__ == "__main__":
    main()
