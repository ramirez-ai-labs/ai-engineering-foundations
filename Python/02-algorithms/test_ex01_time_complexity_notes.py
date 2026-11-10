"""Tests for ex01_time_complexity_notes. Run from this folder: python -m unittest -v"""

import unittest

from ex01_time_complexity_notes import count_pairs, get_first, total


class TimeComplexityNotesTest(unittest.TestCase):
    def test_get_first(self) -> None:
        self.assertEqual(get_first([7, 8]), 7)
        self.assertEqual(get_first([]), -1)

    def test_total(self) -> None:
        self.assertEqual(total([1, 2, 3, 4, 5]), 15)
        self.assertEqual(total([]), 0)
        self.assertEqual(total([-3, 3]), 0)

    def test_count_pairs_is_n_choose_2(self) -> None:
        for n in range(8):
            # count_pairs only looks at the length, so any n items will do.
            values: list[int] = []
            for item in range(n):
                values.append(item)
            expected = n * (n - 1) // 2
            self.assertEqual(count_pairs(values), expected, n)


if __name__ == "__main__":
    unittest.main()
