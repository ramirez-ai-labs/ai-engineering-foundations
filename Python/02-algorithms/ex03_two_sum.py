"""Two Sum - find two indices such that nums[i] + nums[j] == target.

Brute force: O(n^2). With a hash map (store complement): O(n).
Classic interview problem and pattern: "use a map to remember what you've seen."
"""


def two_sum_brute(nums: list[int], target: int) -> tuple[int, int]:
    """O(n^2) - two nested loops."""
    for i in range(len(nums)):
        for j in range(i + 1, len(nums)):
            if nums[i] + nums[j] == target:
                return (i, j)
    return (-1, -1)


def two_sum(nums: list[int], target: int) -> tuple[int, int]:
    """O(n) - one pass with a dict: value -> index; look for (target - value)."""
    seen: dict[int, int] = {}
    for i in range(len(nums)):
        value = nums[i]
        need = target - value
        if need in seen:
            return (seen[need], i)
        seen[value] = i
    return (-1, -1)


def main() -> None:
    nums = [2, 7, 11, 15]
    target = 9
    print(f"Brute: {two_sum_brute(nums, target)}")
    print(f"Map:   {two_sum(nums, target)}")


if __name__ == "__main__":
    main()
