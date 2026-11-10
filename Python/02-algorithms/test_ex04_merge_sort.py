"""Tests for ex04_merge_sort. Run from this folder: python -m unittest -v"""

import random
import unittest

from ex04_merge_sort import copy_range, merge, merge_sort


class MergeTest(unittest.TestCase):
    def test_interleaves_two_sorted_lists(self) -> None:
        self.assertEqual(merge([1, 4, 9], [2, 3, 10]), [1, 2, 3, 4, 9, 10])

    def test_one_side_empty(self) -> None:
        self.assertEqual(merge([], [1, 2]), [1, 2])
        self.assertEqual(merge([1, 2], []), [1, 2])

    def test_one_side_exhausted_first(self) -> None:
        self.assertEqual(merge([1, 2], [3, 4, 5]), [1, 2, 3, 4, 5])


class CopyRangeTest(unittest.TestCase):
    def test_matches_slicing(self) -> None:
        values = [5, 6, 7, 8]
        self.assertEqual(copy_range(values, 1, 3), values[1:3])
        self.assertEqual(copy_range(values, 0, 0), [])


class MergeSortTest(unittest.TestCase):
    def test_example_from_main(self) -> None:
        self.assertEqual(merge_sort([38, 27, 43, 3, 9, 82, 10]), [3, 9, 10, 27, 38, 43, 82])

    def test_empty_and_single(self) -> None:
        self.assertEqual(merge_sort([]), [])
        self.assertEqual(merge_sort([1]), [1])

    def test_already_sorted_and_reversed(self) -> None:
        self.assertEqual(merge_sort([1, 2, 3, 4]), [1, 2, 3, 4])
        self.assertEqual(merge_sort([4, 3, 2, 1]), [1, 2, 3, 4])

    def test_duplicates_and_negatives(self) -> None:
        self.assertEqual(merge_sort([3, -1, 3, 0, -1]), [-1, -1, 0, 3, 3])

    def test_does_not_change_the_input(self) -> None:
        values = [3, 1, 2]
        merge_sort(values)
        self.assertEqual(values, [3, 1, 2])

    def test_returns_a_new_list_even_for_one_element(self) -> None:
        values = [1]
        self.assertIsNot(merge_sort(values), values)

    def test_matches_builtin_sorted_on_random_input(self) -> None:
        # A fixed seed keeps the test repeatable.
        generator = random.Random(42)
        for trial in range(50):
            size = generator.randint(0, 40)
            values: list[int] = []
            for position in range(size):
                values.append(generator.randint(-20, 20))
            self.assertEqual(merge_sort(values), sorted(values), values)


if __name__ == "__main__":
    unittest.main()
