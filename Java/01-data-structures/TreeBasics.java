import java.util.ArrayList;
import java.util.List;

/**
 * Binary tree basics - structure and traversals.
 *
 * Trees show up in decision trees, parse trees, and (conceptually) in how we
 * reason about recursive structures. BST gives O(log n) search when balanced.
 */
public class TreeBasics {

    // A tree node can point to a left child and a right child.
    static class TreeNode {
        int value;
        TreeNode left, right;

        TreeNode(int value) {
            this.value = value;
            this.left = this.right = null;
        }
    }

    /** In-order: left -> node -> right. For BST this gives sorted order. */
    static void inorder(TreeNode node, List<Integer> out) {
        // Base case: an empty subtree contributes nothing.
        if (node == null) {
            return;
        }

        // Recursively visit the left subtree, then this node, then the right subtree.
        inorder(node.left, out);
        out.add(node.value);
        inorder(node.right, out);
    }

    /** Pre-order: node -> left -> right. Useful for copying or prefix expressions. */
    static void preorder(TreeNode node, List<Integer> out) {
        // Base case: an empty subtree contributes nothing.
        if (node == null) {
            return;
        }

        // In pre-order we record the current node before exploring children.
        out.add(node.value);
        preorder(node.left, out);
        preorder(node.right, out);
    }

    /** Search in a BST - O(height), O(log n) if balanced */
    static boolean searchBST(TreeNode node, int target) {
        // Base case: target is not in an empty subtree.
        if (node == null) {
            return false;
        }
        // If the current node matches, the search is done.
        if (node.value == target) {
            return true;
        }

        // BST rule:
        // smaller values live on the left, larger values live on the right.
        // That rule lets us skip half of the remaining tree each step.
        if (target < node.value) {
            return searchBST(node.left, target);
        }
        return searchBST(node.right, target);
    }

    public static void main(String[] args) {
        // Build:      4
        //           /   \
        //          2     6
        //         / \   /
        //        1  3  5
        TreeNode root = new TreeNode(4); // root node
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);

        List<Integer> in = new ArrayList<>(); // collect traversal output here
        inorder(root, in);
        System.out.println("In-order (sorted): " + in);

        List<Integer> pre = new ArrayList<>();
        preorder(root, pre);
        System.out.println("Pre-order: " + pre);

        System.out.println("searchBST(3): " + searchBST(root, 3));
        System.out.println("searchBST(7): " + searchBST(root, 7));
    }
}
