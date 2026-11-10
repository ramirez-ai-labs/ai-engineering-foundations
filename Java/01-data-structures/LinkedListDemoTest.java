/** Tests for LinkedListDemo. Run: javac *.java && java LinkedListDemoTest */
public class LinkedListDemoTest {

    static LinkedListDemo.Node buildList(int... values) {
        LinkedListDemo.Node head = null;
        for (int i = values.length - 1; i >= 0; i--) {
            head = LinkedListDemo.insertAtHead(head, values[i]);
        }
        return head;
    }

    static String text(LinkedListDemo.Node head) {
        return LinkedListDemo.toText(head);
    }

    static void run() {
        // Traversal and length
        Check.equal("1 -> 2 -> 3", text(LinkedListDemo.buildSample()), "buildSample renders in order");
        Check.equal("", text(null), "empty list renders as empty string");
        Check.equal("7", text(new LinkedListDemo.Node(7)), "single node has no arrow");
        Check.equal(0, LinkedListDemo.length(null), "length of empty list");
        Check.equal(3, LinkedListDemo.length(LinkedListDemo.buildSample()), "length of sample");

        // Insert at head
        Check.equal("0 -> 1 -> 2 -> 3", text(LinkedListDemo.insertAtHead(LinkedListDemo.buildSample(), 0)),
                "insertAtHead puts the new node first");
        Check.equal("5", text(LinkedListDemo.insertAtHead(null, 5)), "insertAtHead into empty list");

        // Reverse
        Check.equal("4 -> 3 -> 2 -> 1", text(LinkedListDemo.reverse(buildList(1, 2, 3, 4))), "reverse");
        Check.isNull(LinkedListDemo.reverse(null), "reverse empty list");
        Check.equal("1", text(LinkedListDemo.reverse(buildList(1))), "reverse single node");
        LinkedListDemo.Node oldHead = buildList(1, 2, 3);
        LinkedListDemo.reverse(oldHead);
        Check.isNull(oldHead.next, "reverse is in place: old head becomes the tail");
        Check.equal("1 -> 2 -> 3", text(LinkedListDemo.reverse(LinkedListDemo.reverse(buildList(1, 2, 3)))),
                "reversing twice restores order");

        // Remove value
        Check.equal("2 -> 3", text(LinkedListDemo.removeValue(buildList(1, 2, 3), 1)), "remove head");
        Check.equal("1 -> 3", text(LinkedListDemo.removeValue(buildList(1, 2, 3), 2)), "remove middle");
        Check.equal("1 -> 2", text(LinkedListDemo.removeValue(buildList(1, 2, 3), 3)), "remove tail");
        Check.equal("1 -> 2 -> 3", text(LinkedListDemo.removeValue(buildList(1, 2, 3), 9)),
                "remove missing value leaves list unchanged");
        Check.equal("1 -> 2", text(LinkedListDemo.removeValue(buildList(2, 1, 2), 2)), "removes only the first match");
        Check.isNull(LinkedListDemo.removeValue(buildList(1), 1), "remove the only node");
        Check.isNull(LinkedListDemo.removeValue(null, 1), "remove from empty list");

        // Find middle
        Check.equal(3, LinkedListDemo.findMiddle(buildList(1, 2, 3, 4, 5)).value, "middle of odd length");
        Check.equal(3, LinkedListDemo.findMiddle(buildList(1, 2, 3, 4)).value, "even length returns second middle");
        Check.equal(1, LinkedListDemo.findMiddle(buildList(1)).value, "middle of single node");
        Check.equal(2, LinkedListDemo.findMiddle(buildList(1, 2)).value, "middle of two nodes");
        Check.isNull(LinkedListDemo.findMiddle(null), "middle of empty list");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
