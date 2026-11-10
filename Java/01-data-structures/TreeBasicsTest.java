import java.util.ArrayList;
import java.util.List;

/** Tests for TreeBasics. Run: javac *.java && java TreeBasicsTest */
public class TreeBasicsTest {

    // The tree from main():     4
    //                         /   \
    //                        2     6
    //                       / \   /
    //                      1   3 5
    static final int[] SAMPLE_VALUES = { 4, 2, 6, 1, 3, 5 };

    static TreeBasics.TreeNode buildBST(int... values) {
        TreeBasics.TreeNode root = null;
        for (int value : values) {
            root = TreeBasics.insertBST(root, value);
        }
        return root;
    }

    static List<Integer> inorder(TreeBasics.TreeNode root) {
        List<Integer> out = new ArrayList<>();
        TreeBasics.inorder(root, out);
        return out;
    }

    static List<Integer> preorder(TreeBasics.TreeNode root) {
        List<Integer> out = new ArrayList<>();
        TreeBasics.preorder(root, out);
        return out;
    }

    static List<Integer> postorder(TreeBasics.TreeNode root) {
        List<Integer> out = new ArrayList<>();
        TreeBasics.postorder(root, out);
        return out;
    }

    static void run() {
        TreeBasics.TreeNode root = buildBST(SAMPLE_VALUES);

        // Traversals
        Check.equal(List.of(1, 2, 3, 4, 5, 6), inorder(root), "in-order is sorted for a BST");
        Check.equal(List.of(4, 2, 1, 3, 6, 5), preorder(root), "pre-order visits node before children");
        Check.equal(List.of(1, 3, 2, 5, 6, 4), postorder(root), "post-order visits children before node");
        Check.equal(List.of(4, 2, 6, 1, 3, 5), TreeBasics.levelOrder(root), "level-order goes level by level");
        Check.equal(List.of(), inorder(null), "in-order of empty tree");
        Check.equal(List.of(), postorder(null), "post-order of empty tree");
        Check.equal(List.of(), TreeBasics.levelOrder(null), "level-order of empty tree");
        Check.equal(List.of(1), TreeBasics.levelOrder(new TreeBasics.TreeNode(1)), "level-order of single node");

        // Height
        Check.equal(0, TreeBasics.height(null), "height of empty tree is 0");
        Check.equal(1, TreeBasics.height(new TreeBasics.TreeNode(1)), "height of single node is 1");
        Check.equal(3, TreeBasics.height(root), "height of sample tree");
        // The left side is a chain 5 -> 2 -> 1 -> 0; the right side is just 8.
        Check.equal(4, TreeBasics.height(buildBST(5, 2, 8, 1, 0)), "height uses the taller side");
        Check.equal(5, TreeBasics.height(buildBST(1, 2, 3, 4, 5)), "sorted inserts degenerate into a chain");

        // Insert
        Check.equal(7, TreeBasics.insertBST(null, 7).value, "insert into empty tree creates root");
        Check.equal(List.of(20, 30, 35, 40, 50, 60, 70, 80), inorder(buildBST(50, 30, 70, 20, 40, 60, 80, 35)),
                "in-order stays sorted for any insert order");
        Check.equal(List.of(1, 2, 3), inorder(buildBST(2, 1, 3, 2, 1)), "duplicates are ignored");
        TreeBasics.TreeNode small = buildBST(10, 5, 15);
        Check.equal(5, small.left.value, "smaller value goes left");
        Check.equal(15, small.right.value, "larger value goes right");

        // Search
        for (int value : SAMPLE_VALUES) {
            Check.isTrue(TreeBasics.searchBST(root, value), "search finds " + value);
        }
        Check.isFalse(TreeBasics.searchBST(root, 0), "search misses 0");
        Check.isFalse(TreeBasics.searchBST(root, 7), "search misses 7");
        Check.isFalse(TreeBasics.searchBST(null, 1), "search in empty tree");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
