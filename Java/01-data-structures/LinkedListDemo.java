/**
 * Linked list - nodes connected by references. O(1) insert/delete at head (or tail with ref).
 * No random access; traversal is O(n).
 *
 * In practice you often use ArrayList for lists; linked lists appear in interviews and
 * in some queue/deque implementations.
 */
public class LinkedListDemo {

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

    /** Traverse and print */
    static void print(Node head) {
        // We use a separate cursor so we do not lose the original head reference.
        Node curr = head;
        while (curr != null) {
            // Print the current node's value first.
            System.out.print(curr.value);
            // Print the arrow only when there is another node after this one.
            if (curr.next != null) {
                System.out.print(" -> ");
            }
            curr = curr.next;
        }
        System.out.println();
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
    }
}
