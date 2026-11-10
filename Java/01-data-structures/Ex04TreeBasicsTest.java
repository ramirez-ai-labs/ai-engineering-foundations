import java.util.ArrayList;
import java.util.List;

/** Tests for Ex04TreeBasics. Run: javac *.java && java Ex04TreeBasicsTest */
public class Ex04TreeBasicsTest {

    // The tree from main():     4
    //                         /   \
    //                        2     6
    //                       / \   /
    //                      1   3 5
    static final int[] SAMPLE_VALUES = { 4, 2, 6, 1, 3, 5 };

    static Ex04TreeBasics.TreeNode buildBST(int... values) {
        Ex04TreeBasics.TreeNode root = null;
        for (int value : values) {
            root = Ex04TreeBasics.insertBST(root, value);
        }
        return root;
    }

    static List<Integer> inorder(Ex04TreeBasics.TreeNode root) {
        List<Integer> out = new ArrayList<>();
        Ex04TreeBasics.inorder(root, out);
        return out;
    }

    static List<Integer> preorder(Ex04TreeBasics.TreeNode root) {
        List<Integer> out = new ArrayList<>();
        Ex04TreeBasics.preorder(root, out);
        return out;
    }

    static List<Integer> postorder(Ex04TreeBasics.TreeNode root) {
        List<Integer> out = new ArrayList<>();
        Ex04TreeBasics.postorder(root, out);
        return out;
    }

    static void run() {
        Ex04TreeBasics.TreeNode root = buildBST(SAMPLE_VALUES);

        // Traversals
        Check.equal(List.of(1, 2, 3, 4, 5, 6), inorder(root), "in-order is sorted for a BST");
        Check.equal(List.of(4, 2, 1, 3, 6, 5), preorder(root), "pre-order visits node before children");
        Check.equal(List.of(1, 3, 2, 5, 6, 4), postorder(root), "post-order visits children before node");
        Check.equal(List.of(4, 2, 6, 1, 3, 5), Ex04TreeBasics.levelOrder(root), "level-order goes level by level");
        Check.equal(List.of(), inorder(null), "in-order of empty tree");
        Check.equal(List.of(), postorder(null), "post-order of empty tree");
        Check.equal(List.of(), Ex04TreeBasics.levelOrder(null), "level-order of empty tree");
        Check.equal(List.of(1), Ex04TreeBasics.levelOrder(new Ex04TreeBasics.TreeNode(1)), "level-order of single node");

        // Height
        Check.equal(0, Ex04TreeBasics.height(null), "height of empty tree is 0");
        Check.equal(1, Ex04TreeBasics.height(new Ex04TreeBasics.TreeNode(1)), "height of single node is 1");
        Check.equal(3, Ex04TreeBasics.height(root), "height of sample tree");
        // The left side is a chain 5 -> 2 -> 1 -> 0; the right side is just 8.
        Check.equal(4, Ex04TreeBasics.height(buildBST(5, 2, 8, 1, 0)), "height uses the taller side");
        Check.equal(5, Ex04TreeBasics.height(buildBST(1, 2, 3, 4, 5)), "sorted inserts degenerate into a chain");

        // Insert
        Check.equal(7, Ex04TreeBasics.insertBST(null, 7).value, "insert into empty tree creates root");
        Check.equal(List.of(20, 30, 35, 40, 50, 60, 70, 80), inorder(buildBST(50, 30, 70, 20, 40, 60, 80, 35)),
                "in-order stays sorted for any insert order");
        Check.equal(List.of(1, 2, 3), inorder(buildBST(2, 1, 3, 2, 1)), "duplicates are ignored");
        Ex04TreeBasics.TreeNode small = buildBST(10, 5, 15);
        Check.equal(5, small.left.value, "smaller value goes left");
        Check.equal(15, small.right.value, "larger value goes right");

        // Search
        for (int value : SAMPLE_VALUES) {
            Check.isTrue(Ex04TreeBasics.searchBST(root, value), "search finds " + value);
        }
        Check.isFalse(Ex04TreeBasics.searchBST(root, 0), "search misses 0");
        Check.isFalse(Ex04TreeBasics.searchBST(root, 7), "search misses 7");
        Check.isFalse(Ex04TreeBasics.searchBST(null, 1), "search in empty tree");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
