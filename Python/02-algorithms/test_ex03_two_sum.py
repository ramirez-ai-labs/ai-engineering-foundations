"""Tests for ex03_two_sum. Run from this folder: python -m unittest -v

Both versions must give the same answers, so every test runs against both.
"""

import unittest

from ex03_two_sum import two_sum, two_sum_brute

IMPLEMENTATIONS = [two_sum_brute, two_sum]


class TwoSumTest(unittest.TestCase):
    def check(self, nums: list[int], target: int, expected: tuple[int, int]) -> None:
        for implementation in IMPLEMENTATIONS:
            self.assertEqual(implementation(nums, target), expected, implementation.__name__)

    def test_classic_example(self) -> None:
        self.check([2, 7, 11, 15], 9, (0, 1))

    def test_pair_at_the_end(self) -> None:
        self.check([1, 2, 3, 4], 7, (2, 3))

    def test_does_not_use_the_same_element_twice(self) -> None:
        # 3 + 3 = 6, but there is only one 3, so the answer must be 2 + 4.
        self.check([3, 2, 4], 6, (1, 2))

    def test_duplicate_values(self) -> None:
        self.check([3, 3], 6, (0, 1))

    def test_negative_numbers_and_zero(self) -> None:
        self.check([-4, 1, 0, 4], 0, (0, 3))

    def test_no_solution(self) -> None:
        self.check([1, 2, 3], 100, (-1, -1))

    def test_empty_and_single_element(self) -> None:
        self.check([], 5, (-1, -1))
        self.check([5], 5, (-1, -1))

    def test_indices_point_at_values_that_sum_to_target(self) -> None:
        nums = [10, -2, 7, 5, 8, 1]
        for implementation in IMPLEMENTATIONS:
            first, second = implementation(nums, 13)
            self.assertLess(first, second)
            self.assertEqual(nums[first] + nums[second], 13)


if __name__ == "__main__":
    unittest.main()
