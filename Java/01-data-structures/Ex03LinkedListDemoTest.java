/** Tests for Ex03LinkedListDemo. Run: javac *.java && java Ex03LinkedListDemoTest */
public class Ex03LinkedListDemoTest {

    static Ex03LinkedListDemo.Node buildList(int... values) {
        Ex03LinkedListDemo.Node head = null;
        for (int i = values.length - 1; i >= 0; i--) {
            head = Ex03LinkedListDemo.insertAtHead(head, values[i]);
        }
        return head;
    }

    static String text(Ex03LinkedListDemo.Node head) {
        return Ex03LinkedListDemo.toText(head);
    }

    static void run() {
        // Traversal and length
        DataStructuresCheck.equal("1 -> 2 -> 3", text(Ex03LinkedListDemo.buildSample()), "buildSample renders in order");
        DataStructuresCheck.equal("", text(null), "empty list renders as empty string");
        DataStructuresCheck.equal("7", text(new Ex03LinkedListDemo.Node(7)), "single node has no arrow");
        DataStructuresCheck.equal(0, Ex03LinkedListDemo.length(null), "length of empty list");
        DataStructuresCheck.equal(3, Ex03LinkedListDemo.length(Ex03LinkedListDemo.buildSample()), "length of sample");

        // Insert at head
        DataStructuresCheck.equal("0 -> 1 -> 2 -> 3", text(Ex03LinkedListDemo.insertAtHead(Ex03LinkedListDemo.buildSample(), 0)),
                "insertAtHead puts the new node first");
        DataStructuresCheck.equal("5", text(Ex03LinkedListDemo.insertAtHead(null, 5)), "insertAtHead into empty list");

        // Reverse
        DataStructuresCheck.equal("4 -> 3 -> 2 -> 1", text(Ex03LinkedListDemo.reverse(buildList(1, 2, 3, 4))), "reverse");
        DataStructuresCheck.isNull(Ex03LinkedListDemo.reverse(null), "reverse empty list");
        DataStructuresCheck.equal("1", text(Ex03LinkedListDemo.reverse(buildList(1))), "reverse single node");
        Ex03LinkedListDemo.Node oldHead = buildList(1, 2, 3);
        Ex03LinkedListDemo.reverse(oldHead);
        DataStructuresCheck.isNull(oldHead.next, "reverse is in place: old head becomes the tail");
        DataStructuresCheck.equal("1 -> 2 -> 3", text(Ex03LinkedListDemo.reverse(Ex03LinkedListDemo.reverse(buildList(1, 2, 3)))),
                "reversing twice restores order");

        // Remove value
        DataStructuresCheck.equal("2 -> 3", text(Ex03LinkedListDemo.removeValue(buildList(1, 2, 3), 1)), "remove head");
        DataStructuresCheck.equal("1 -> 3", text(Ex03LinkedListDemo.removeValue(buildList(1, 2, 3), 2)), "remove middle");
        DataStructuresCheck.equal("1 -> 2", text(Ex03LinkedListDemo.removeValue(buildList(1, 2, 3), 3)), "remove tail");
        DataStructuresCheck.equal("1 -> 2 -> 3", text(Ex03LinkedListDemo.removeValue(buildList(1, 2, 3), 9)),
                "remove missing value leaves list unchanged");
        DataStructuresCheck.equal("1 -> 2", text(Ex03LinkedListDemo.removeValue(buildList(2, 1, 2), 2)), "removes only the first match");
        DataStructuresCheck.isNull(Ex03LinkedListDemo.removeValue(buildList(1), 1), "remove the only node");
        DataStructuresCheck.isNull(Ex03LinkedListDemo.removeValue(null, 1), "remove from empty list");

        // Find middle
        DataStructuresCheck.equal(3, Ex03LinkedListDemo.findMiddle(buildList(1, 2, 3, 4, 5)).value, "middle of odd length");
        DataStructuresCheck.equal(3, Ex03LinkedListDemo.findMiddle(buildList(1, 2, 3, 4)).value, "even length returns second middle");
        DataStructuresCheck.equal(1, Ex03LinkedListDemo.findMiddle(buildList(1)).value, "middle of single node");
        DataStructuresCheck.equal(2, Ex03LinkedListDemo.findMiddle(buildList(1, 2)).value, "middle of two nodes");
        DataStructuresCheck.isNull(Ex03LinkedListDemo.findMiddle(null), "middle of empty list");
    }

    public static void main(String[] args) {
        run();
        DataStructuresCheck.finish();
    }
}
