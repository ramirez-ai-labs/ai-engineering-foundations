"""Tests for ex03_linked_list_demo. Run from this folder: python -m unittest -v"""

import unittest

from ex03_linked_list_demo import (
    Node,
    build_sample,
    find_middle,
    insert_at_head,
    length,
    remove_value,
    reverse,
    to_string,
)


def build_list(values: list[int]) -> Node | None:
    """Build a linked list holding values in order (test helper)."""
    head: Node | None = None
    for index in range(len(values) - 1, -1, -1):
        head = insert_at_head(head, values[index])
    return head


class TraversalTest(unittest.TestCase):
    def test_build_sample_renders_in_order(self) -> None:
        self.assertEqual(to_string(build_sample()), "1 -> 2 -> 3")

    def test_empty_list_renders_as_empty_string(self) -> None:
        self.assertEqual(to_string(None), "")

    def test_single_node_has_no_arrow(self) -> None:
        self.assertEqual(to_string(Node(7)), "7")

    def test_length(self) -> None:
        self.assertEqual(length(None), 0)
        self.assertEqual(length(Node(1)), 1)
        self.assertEqual(length(build_sample()), 3)


class InsertAtHeadTest(unittest.TestCase):
    def test_new_node_becomes_head(self) -> None:
        head = insert_at_head(build_sample(), 0)
        self.assertEqual(head.value, 0)
        self.assertEqual(to_string(head), "0 -> 1 -> 2 -> 3")

    def test_insert_into_empty_list(self) -> None:
        self.assertEqual(to_string(insert_at_head(None, 5)), "5")


class ReverseTest(unittest.TestCase):
    def test_reverses_order(self) -> None:
        self.assertEqual(to_string(reverse(build_list([1, 2, 3, 4]))), "4 -> 3 -> 2 -> 1")

    def test_empty_list(self) -> None:
        self.assertIsNone(reverse(None))

    def test_single_node(self) -> None:
        self.assertEqual(to_string(reverse(Node(1))), "1")

    def test_old_head_becomes_tail(self) -> None:
        old_head = build_list([1, 2, 3])
        reverse(old_head)
        # Reversal is in place: the old head node now ends the list.
        self.assertIsNone(old_head.next)

    def test_reversing_twice_restores_order(self) -> None:
        head = build_list([1, 2, 3])
        self.assertEqual(to_string(reverse(reverse(head))), "1 -> 2 -> 3")


class RemoveValueTest(unittest.TestCase):
    def test_remove_head(self) -> None:
        self.assertEqual(to_string(remove_value(build_list([1, 2, 3]), 1)), "2 -> 3")

    def test_remove_middle(self) -> None:
        self.assertEqual(to_string(remove_value(build_list([1, 2, 3]), 2)), "1 -> 3")

    def test_remove_tail(self) -> None:
        self.assertEqual(to_string(remove_value(build_list([1, 2, 3]), 3)), "1 -> 2")

    def test_missing_value_leaves_list_unchanged(self) -> None:
        self.assertEqual(to_string(remove_value(build_list([1, 2, 3]), 9)), "1 -> 2 -> 3")

    def test_removes_only_first_match(self) -> None:
        self.assertEqual(to_string(remove_value(build_list([2, 1, 2]), 2)), "1 -> 2")

    def test_remove_only_node(self) -> None:
        self.assertIsNone(remove_value(Node(1), 1))

    def test_empty_list(self) -> None:
        self.assertIsNone(remove_value(None, 1))


class FindMiddleTest(unittest.TestCase):
    def test_odd_length(self) -> None:
        self.assertEqual(find_middle(build_list([1, 2, 3, 4, 5])).value, 3)

    def test_even_length_returns_second_middle(self) -> None:
        self.assertEqual(find_middle(build_list([1, 2, 3, 4])).value, 3)

    def test_single_node(self) -> None:
        self.assertEqual(find_middle(Node(1)).value, 1)

    def test_two_nodes(self) -> None:
        self.assertEqual(find_middle(build_list([1, 2])).value, 2)

    def test_empty_list(self) -> None:
        self.assertIsNone(find_middle(None))


if __name__ == "__main__":
    unittest.main()
