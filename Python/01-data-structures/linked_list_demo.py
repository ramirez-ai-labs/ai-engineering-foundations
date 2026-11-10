"""Linked list - nodes connected by references. O(1) insert/delete at head (or tail with ref).

No random access; traversal is O(n). This file covers the operations interviews
ask about most: traversal, head insertion, removal, reversal, and finding the
middle with slow and fast pointers.

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


def reverse(head: Node | None) -> Node | None:
    """Reverse the list in place - O(n) time, O(1) extra space. Returns the new head.

    Walk the list once, pointing each node back at the node before it.
    """
    previous: Node | None = None
    current = head
    while current is not None:
        # Save the rest of the list before overwriting current.next,
        # otherwise we lose our only reference to it.
        next_node = current.next
        current.next = previous
        previous = current
        current = next_node
    # When current falls off the end, previous is the old tail: the new head.
    return previous


def remove_value(head: Node | None, value: int) -> Node | None:
    """Remove the first node holding value - O(n). Returns the (possibly new) head.

    Returns the list unchanged if value is not present.
    """
    if head is None:
        return None

    # Removing the head is special: there is no previous node to relink, so
    # the second node becomes the new head. (A "dummy" node placed before the
    # head is a common trick that removes this special case.)
    if head.value == value:
        return head.next

    previous = head
    current = head.next
    while current is not None:
        if current.value == value:
            # Skip over current; nothing points to it any more.
            previous.next = current.next
            return head
        previous = current
        current = current.next
    return head


def find_middle(head: Node | None) -> Node | None:
    """Find the middle node with slow and fast pointers - O(n), one pass.

    The fast pointer moves two steps for each step of the slow pointer, so when
    fast reaches the end, slow is halfway. For an even length this returns the
    second of the two middle nodes. Returns None for an empty list.
    """
    slow = head
    fast = head
    while fast is not None and fast.next is not None:
        # slow trails fast, so it can never be None inside this loop.
        slow = slow.next
        fast = fast.next.next
    return slow


def main() -> None:
    # Build a small linked list manually so the pointer structure is visible.
    head = build_sample()
    print(f"List: {to_string(head)}")
    print(f"Length: {length(head)}")

    # Inserting at the head is constant time because we only change one pointer.
    head = insert_at_head(head, 0)
    print(f"After insert 0 at head: {to_string(head)}")

    middle = find_middle(head)
    if middle is not None:
        print(f"Middle node: {middle.value}")  # 2 (second middle of 0 1 2 3)

    head = remove_value(head, 2)
    print(f"After remove 2: {to_string(head)}")

    head = reverse(head)
    print(f"After reverse: {to_string(head)}")


if __name__ == "__main__":
    main()
