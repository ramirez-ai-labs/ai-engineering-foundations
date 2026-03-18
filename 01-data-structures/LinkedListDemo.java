/**
 * Linked list - nodes connected by references. O(1) insert/delete at head (or tail with ref).
 * No random access; traversal is O(n).
 *
 * In practice you often use ArrayList for lists; linked lists appear in interviews and
 * in some queue/deque implementations.
 */
public class LinkedListDemo {

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
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        return head;
    }

    /** Traverse and print */
    static void print(Node head) {
        Node curr = head;
        while (curr != null) {
            System.out.print(curr.value + (curr.next != null ? " -> " : ""));
            curr = curr.next;
        }
        System.out.println();
    }

    /** O(n) length */
    static int length(Node head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }

    /** Insert at head - O(1) */
    static Node insertAtHead(Node head, int value) {
        Node newHead = new Node(value);
        newHead.next = head;
        return newHead;
    }

    public static void main(String[] args) {
        Node head = buildSample();
        System.out.print("List: ");
        print(head);
        System.out.println("Length: " + length(head));

        head = insertAtHead(head, 0);
        System.out.print("After insert 0 at head: ");
        print(head);
    }
}
