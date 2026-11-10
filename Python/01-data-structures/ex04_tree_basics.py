"""Binary tree basics - structure, traversals, and binary search trees (BST).

Trees show up in decision trees, parse trees, and (conceptually) in how we
reason about recursive structures. BST gives O(log n) search when balanced.

Depth-first traversals (in-, pre-, post-order) use recursion, which is an
implicit stack. Level-order traversal is breadth-first, so it uses a queue,
exactly like BFS in ex05_graph_dfs_and_bfs.py.
"""

# Lets the TreeNode class use its own name in its type hints.
from __future__ import annotations

from collections import deque


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


def postorder(node: TreeNode | None, out: list[int]) -> None:
    """Post-order: left -> right -> node. Children finish before their parent,
    so it suits deleting a tree or computing values from subtrees (like height)."""
    if node is None:
        return
    postorder(node.left, out)
    postorder(node.right, out)
    # Record the current node only after both subtrees are done.
    out.append(node.value)


def level_order(root: TreeNode | None) -> list[int]:
    """Level-order (breadth-first): visit the tree one level at a time, top to bottom.

    A queue holds the nodes waiting to be visited. A tree has no cycles, so
    unlike graph BFS there is no visited set.
    """
    order: list[int] = []
    if root is None:
        return order

    queue: deque[TreeNode] = deque()
    queue.append(root)
    while len(queue) > 0:
        current = queue.popleft()
        order.append(current.value)
        # Children join the back of the queue, so the whole current level is
        # visited before any node on the next level.
        if current.left is not None:
            queue.append(current.left)
        if current.right is not None:
            queue.append(current.right)
    return order


def height(node: TreeNode | None) -> int:
    """Number of nodes on the longest root-to-leaf path - O(n).

    An empty tree has height 0 and a single node has height 1. (Some texts count
    edges instead, which makes those -1 and 0.) This is post-order thinking:
    each node needs the answer from both children first.
    """
    if node is None:
        return 0
    left_height = height(node.left)
    right_height = height(node.right)
    # Python shortcut: 1 + max(left_height, right_height)
    if left_height > right_height:
        return 1 + left_height
    return 1 + right_height


def insert_bst(node: TreeNode | None, value: int) -> TreeNode:
    """Insert value into a BST - O(height). Returns the root of the subtree.

    Duplicates are ignored, so every value appears once. Inserting already
    sorted values builds a tree that is really a linked list (height n), which
    is why production code uses self-balancing trees (red-black, AVL, B-trees).
    """
    # Base case: we found the empty spot where the new value belongs.
    if node is None:
        return TreeNode(value)

    if value < node.value:
        node.left = insert_bst(node.left, value)
    elif value > node.value:
        node.right = insert_bst(node.right, value)
    return node


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

    post_order: list[int] = []
    postorder(root, post_order)
    print(f"Post-order: {post_order}")

    print(f"Level-order: {level_order(root)}")
    print(f"Height: {height(root)}")

    print(f"search_bst(3): {search_bst(root, 3)}")
    print(f"search_bst(7): {search_bst(root, 7)}")

    # Building the same tree with insert_bst: insertion order decides the shape.
    built: TreeNode | None = None
    for value in [4, 2, 6, 1, 3, 5]:
        built = insert_bst(built, value)
    print(f"Built by insert, level-order: {level_order(built)}")

    # Sorted input degenerates into a chain: height equals the node count.
    chain: TreeNode | None = None
    for value in [1, 2, 3, 4, 5, 6]:
        chain = insert_bst(chain, value)
    print(f"Height after inserting sorted values: {height(chain)}")


if __name__ == "__main__":
    main()
