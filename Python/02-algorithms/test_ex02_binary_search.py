"""Tests for ex02_binary_search. Run from this folder: python -m unittest -v"""

import unittest

from ex02_binary_search import search

SORTED_NUMS = [1, 3, 5, 7, 9, 11]


class BinarySearchTest(unittest.TestCase):
    def test_finds_every_element_at_its_index(self) -> None:
        for index in range(len(SORTED_NUMS)):
            self.assertEqual(search(SORTED_NUMS, SORTED_NUMS[index]), index)

    def test_missing_below_range(self) -> None:
        self.assertEqual(search(SORTED_NUMS, 0), -1)

    def test_missing_above_range(self) -> None:
        self.assertEqual(search(SORTED_NUMS, 12), -1)

    def test_missing_between_elements(self) -> None:
        self.assertEqual(search(SORTED_NUMS, 4), -1)

    def test_empty_list(self) -> None:
        self.assertEqual(search([], 1), -1)

    def test_single_element(self) -> None:
        self.assertEqual(search([5], 5), 0)
        self.assertEqual(search([5], 4), -1)

    def test_two_elements(self) -> None:
        # Catches off-by-one errors in the left <= right condition.
        self.assertEqual(search([1, 2], 1), 0)
        self.assertEqual(search([1, 2], 2), 1)

    def test_even_length_and_negatives(self) -> None:
        nums = [-8, -3, 0, 4]
        self.assertEqual(search(nums, -8), 0)
        self.assertEqual(search(nums, 4), 3)

    def test_duplicates_return_some_matching_index(self) -> None:
        # Plain binary search does not promise the FIRST match; bisect_left does.
        nums = [1, 2, 2, 2, 3]
        self.assertEqual(nums[search(nums, 2)], 2)


if __name__ == "__main__":
    unittest.main()
