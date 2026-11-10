/**
 * Linked list - nodes connected by references. O(1) insert/delete at head (or tail with ref).
 * No random access; traversal is O(n). This file covers the operations interviews
 * ask about most: traversal, head insertion, removal, reversal, and finding the
 * middle with slow and fast pointers.
 *
 * In practice you often use ArrayList for lists; linked lists appear in interviews and
 * in some queue/deque implementations.
 */
public class Ex03LinkedListDemo {

    // Each node stores one value plus a reference to the next node.
    // The list starts at "head", and you follow next pointers until null.
    static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    /** Build list: 1 -> 2 -> 3 */
    static Node buildSample() {
        // Start with the head node.
        Node head = new Node(1);
        // Link the next nodes one by one.
        head.next = new Node(2);
        head.next.next = new Node(3);
        return head;
    }

    /** Traverse and render as "1 -> 2 -> 3". An empty list renders as "". */
    static String toText(Node head) {
        StringBuilder text = new StringBuilder();
        // We use a separate cursor so we do not lose the original head reference.
        Node curr = head;
        while (curr != null) {
            text.append(curr.value);
            // Add the arrow only when there is another node after this one.
            if (curr.next != null) {
                text.append(" -> ");
            }
            curr = curr.next;
        }
        return text.toString();
    }

    /** Traverse and print */
    static void print(Node head) {
        System.out.println(toText(head));
    }

    /** O(n) length */
    static int length(Node head) {
        int len = 0;
        // Walk forward one node at a time until we run out of nodes.
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }

    /** Insert at head - O(1) */
    static Node insertAtHead(Node head, int value) {
        // The new node becomes the new head and points to the old head.
        Node newHead = new Node(value);
        newHead.next = head;
        return newHead;
    }

    /**
     * Reverse the list in place - O(n) time, O(1) extra space. Returns the new head.
     * Walk the list once, pointing each node back at the node before it.
     */
    static Node reverse(Node head) {
        Node previous = null;
        Node curr = head;
        while (curr != null) {
            // Save the rest of the list before overwriting curr.next,
            // otherwise we lose our only reference to it.
            Node nextNode = curr.next;
            curr.next = previous;
            previous = curr;
            curr = nextNode;
        }
        // When curr falls off the end, previous is the old tail: the new head.
        return previous;
    }

    /**
     * Remove the first node holding value - O(n). Returns the (possibly new) head.
     * Returns the list unchanged if value is not present.
     */
    static Node removeValue(Node head, int value) {
        if (head == null) {
            return null;
        }

        // Removing the head is special: there is no previous node to relink, so
        // the second node becomes the new head. (A "dummy" node placed before the
        // head is a common trick that removes this special case.)
        if (head.value == value) {
            return head.next;
        }

        Node previous = head;
        Node curr = head.next;
        while (curr != null) {
            if (curr.value == value) {
                // Skip over curr; nothing points to it any more.
                previous.next = curr.next;
                return head;
            }
            previous = curr;
            curr = curr.next;
        }
        return head;
    }

    /**
     * Find the middle node with slow and fast pointers - O(n), one pass.
     *
     * The fast pointer moves two steps for each step of the slow pointer, so when
     * fast reaches the end, slow is halfway. For an even length this returns the
     * second of the two middle nodes. Returns null for an empty list.
     */
    static Node findMiddle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public static void main(String[] args) {
        // Build a small linked list manually so the pointer structure is visible.
        Node head = buildSample();
        System.out.print("List: ");
        print(head);
        System.out.println("Length: " + length(head));

        // Inserting at the head is constant time because we only change one pointer.
        head = insertAtHead(head, 0);
        System.out.print("After insert 0 at head: ");
        print(head);

        Node middle = findMiddle(head);
        System.out.println("Middle node: " + middle.value); // 2 (second middle of 0 1 2 3)

        head = removeValue(head, 2);
        System.out.println("After remove 2: " + toText(head));

        head = reverse(head);
        System.out.println("After reverse: " + toText(head));
    }
}
