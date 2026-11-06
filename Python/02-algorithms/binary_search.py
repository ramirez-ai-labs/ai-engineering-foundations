"""Binary search - O(log n) search in a sorted array.

Prerequisite: the list must be sorted. Used in many "find boundary" and
"search in sorted space" problems. The stdlib ``bisect`` module implements
the boundary-finding variants.
"""


def search(nums: list[int], target: int) -> int:
    """Return the index of target, or -1 if not found."""
    left = 0
    right = len(nums) - 1

    while left <= right:
        # Python ints do not overflow, so (left + right) // 2 is safe here.
        # The Java version uses left + (right - left) / 2 to avoid int overflow.
        mid = (left + right) // 2
        if nums[mid] == target:
            return mid
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1


def main() -> None:
    sorted_nums = [1, 3, 5, 7, 9, 11]
    print(f"search(5): {search(sorted_nums, 5)}")  # 2
    print(f"search(9): {search(sorted_nums, 9)}")  # 4
    print(f"search(4): {search(sorted_nums, 4)}")  # -1


if __name__ == "__main__":
    main()
