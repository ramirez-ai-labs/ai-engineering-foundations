"""Linked list - nodes connected by references. O(1) insert/delete at head (or tail with ref).

No random access; traversal is O(n).

In practice you use ``list`` (dynamic array) or ``collections.deque``; linked lists
appear in interviews and inside some queue/deque implementations.
"""

# Lets the Node class use its own name in its type hints.
from __future__ import annotations


class Node:
    """Each node stores one value plus a reference to the next node.

    The list starts at ``head``, and you follow ``next`` until it is None.
    """

    def __init__(self, value: int) -> None:
        self.value = value
        self.next: Node | None = None


def build_sample() -> Node:
    """Build list: 1 -> 2 -> 3."""
    # Start with the head node, then link the next nodes one by one.
    head = Node(1)
    head.next = Node(2)
    head.next.next = Node(3)
    return head


def to_string(head: Node | None) -> str:
    """Traverse and render as '1 -> 2 -> 3'."""
    text = ""
    # Use a separate cursor so we do not lose the original head reference.
    current = head
    while current is not None:
        text += str(current.value)
        # Only add an arrow when another node follows this one.
        if current.next is not None:
            text += " -> "
        current = current.next
    return text


def length(head: Node | None) -> int:
    """O(n) length."""
    count = 0
    current = head
    # Walk forward one node at a time until we run out of nodes.
    while current is not None:
        count += 1
        current = current.next
    return count


def insert_at_head(head: Node | None, value: int) -> Node:
    """Insert at head - O(1). The new node points to the old head."""
    new_node = Node(value)
    new_node.next = head
    return new_node


def main() -> None:
    # Build a small linked list manually so the pointer structure is visible.
    head = build_sample()
    print(f"List: {to_string(head)}")
    print(f"Length: {length(head)}")

    # Inserting at the head is constant time because we only change one pointer.
    head = insert_at_head(head, 0)
    print(f"After insert 0 at head: {to_string(head)}")


if __name__ == "__main__":
    main()
