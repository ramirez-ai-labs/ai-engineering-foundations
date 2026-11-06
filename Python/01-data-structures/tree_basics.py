"""Binary tree basics - structure and traversals.

Trees show up in decision trees, parse trees, and (conceptually) in how we
reason about recursive structures. BST gives O(log n) search when balanced.
"""

# Lets the TreeNode class use its own name in its type hints.
from __future__ import annotations


class TreeNode:
    """A tree node can point to a left child and a right child."""

    def __init__(self, value: int) -> None:
        self.value = value
        self.left: TreeNode | None = None
        self.right: TreeNode | None = None


def inorder(node: TreeNode | None, out: list[int]) -> None:
    """In-order: left -> node -> right. For a BST this gives sorted order."""
    # Base case: an empty subtree contributes nothing.
    if node is None:
        return
    inorder(node.left, out)
    out.append(node.value)
    inorder(node.right, out)


def preorder(node: TreeNode | None, out: list[int]) -> None:
    """Pre-order: node -> left -> right. Useful for copying or prefix expressions."""
    if node is None:
        return
    # Record the current node before exploring children.
    out.append(node.value)
    preorder(node.left, out)
    preorder(node.right, out)


def search_bst(node: TreeNode | None, target: int) -> bool:
    """Search in a BST - O(height), O(log n) if balanced."""
    # Base case: target is not in an empty subtree.
    if node is None:
        return False
    if node.value == target:
        return True

    # BST rule: smaller values live on the left, larger values on the right.
    # That rule lets us skip one subtree at every step.
    if target < node.value:
        return search_bst(node.left, target)
    return search_bst(node.right, target)


def main() -> None:
    # Build:      4
    #           /   \
    #          2     6
    #         / \   /
    #        1  3  5
    root = TreeNode(4)
    root.left = TreeNode(2)
    root.right = TreeNode(6)
    root.left.left = TreeNode(1)
    root.left.right = TreeNode(3)
    root.right.left = TreeNode(5)

    in_order: list[int] = []
    inorder(root, in_order)
    print(f"In-order (sorted): {in_order}")

    pre_order: list[int] = []
    preorder(root, pre_order)
    print(f"Pre-order: {pre_order}")

    print(f"search_bst(3): {search_bst(root, 3)}")
    print(f"search_bst(7): {search_bst(root, 7)}")


if __name__ == "__main__":
    main()
