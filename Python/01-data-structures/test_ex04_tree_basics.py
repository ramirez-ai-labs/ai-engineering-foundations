"""Tests for ex04_tree_basics. Run from this folder: python -m unittest -v"""

import unittest
from typing import Callable

from ex04_tree_basics import (
    TreeNode,
    height,
    inorder,
    insert_bst,
    level_order,
    postorder,
    preorder,
    search_bst,
)


def build_bst(values: list[int]) -> TreeNode | None:
    root: TreeNode | None = None
    for value in values:
        root = insert_bst(root, value)
    return root


def collect(
    traversal: Callable[[TreeNode | None, list[int]], None],
    root: TreeNode | None,
) -> list[int]:
    """Run one of the out-list traversals and return the list it filled."""
    out: list[int] = []
    traversal(root, out)
    return out


# The tree from main():     4
#                         /   \
#                        2     6
#                       / \   /
#                      1   3 5
SAMPLE_VALUES = [4, 2, 6, 1, 3, 5]


class TraversalTest(unittest.TestCase):
    def setUp(self) -> None:
        self.root = build_bst(SAMPLE_VALUES)

    def test_inorder_is_sorted_for_a_bst(self) -> None:
        self.assertEqual(collect(inorder, self.root), [1, 2, 3, 4, 5, 6])

    def test_preorder_visits_node_before_children(self) -> None:
        self.assertEqual(collect(preorder, self.root), [4, 2, 1, 3, 6, 5])

    def test_postorder_visits_children_before_node(self) -> None:
        self.assertEqual(collect(postorder, self.root), [1, 3, 2, 5, 6, 4])

    def test_level_order_goes_level_by_level(self) -> None:
        self.assertEqual(level_order(self.root), [4, 2, 6, 1, 3, 5])

    def test_empty_tree(self) -> None:
        self.assertEqual(collect(inorder, None), [])
        self.assertEqual(collect(preorder, None), [])
        self.assertEqual(collect(postorder, None), [])
        self.assertEqual(level_order(None), [])

    def test_single_node(self) -> None:
        node = TreeNode(1)
        self.assertEqual(collect(inorder, node), [1])
        self.assertEqual(collect(postorder, node), [1])
        self.assertEqual(level_order(node), [1])


class HeightTest(unittest.TestCase):
    def test_empty_tree_is_zero(self) -> None:
        self.assertEqual(height(None), 0)

    def test_single_node_is_one(self) -> None:
        self.assertEqual(height(TreeNode(1)), 1)

    def test_sample_tree(self) -> None:
        self.assertEqual(height(build_bst(SAMPLE_VALUES)), 3)

    def test_uneven_subtrees_use_the_taller_side(self) -> None:
        # The left side is a chain 5 -> 2 -> 1 -> 0; the right side is just 8.
        self.assertEqual(height(build_bst([5, 2, 8, 1, 0])), 4)

    def test_sorted_inserts_degenerate_into_a_chain(self) -> None:
        self.assertEqual(height(build_bst([1, 2, 3, 4, 5])), 5)


class InsertBstTest(unittest.TestCase):
    def test_insert_into_empty_tree_creates_root(self) -> None:
        root = insert_bst(None, 7)
        self.assertEqual(root.value, 7)

    def test_inorder_stays_sorted_for_any_insert_order(self) -> None:
        root = build_bst([50, 30, 70, 20, 40, 60, 80, 35])
        self.assertEqual(collect(inorder, root), [20, 30, 35, 40, 50, 60, 70, 80])

    def test_duplicates_are_ignored(self) -> None:
        root = build_bst([2, 1, 3, 2, 1])
        self.assertEqual(collect(inorder, root), [1, 2, 3])

    def test_smaller_goes_left_larger_goes_right(self) -> None:
        root = build_bst([10, 5, 15])
        self.assertEqual(root.left.value, 5)
        self.assertEqual(root.right.value, 15)


class SearchBstTest(unittest.TestCase):
    def setUp(self) -> None:
        self.root = build_bst(SAMPLE_VALUES)

    def test_finds_every_inserted_value(self) -> None:
        for value in SAMPLE_VALUES:
            self.assertTrue(search_bst(self.root, value), value)

    def test_missing_values(self) -> None:
        self.assertFalse(search_bst(self.root, 0))
        self.assertFalse(search_bst(self.root, 7))

    def test_empty_tree(self) -> None:
        self.assertFalse(search_bst(None, 1))


if __name__ == "__main__":
    unittest.main()
