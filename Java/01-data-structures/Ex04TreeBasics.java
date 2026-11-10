import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Binary tree basics - structure, traversals, and binary search trees (BST).
 *
 * Trees show up in decision trees, parse trees, and (conceptually) in how we
 * reason about recursive structures. BST gives O(log n) search when balanced.
 *
 * Depth-first traversals (in-, pre-, post-order) use recursion, which is an
 * implicit stack. Level-order traversal is breadth-first, so it uses a queue,
 * exactly like BFS in Ex05GraphDFSAndBFS.java.
 */
public class Ex04TreeBasics {

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

    /**
     * Post-order: left -> right -> node. Children finish before their parent,
     * so it suits deleting a tree or computing values from subtrees (like height).
     */
    static void postorder(TreeNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        postorder(node.left, out);
        postorder(node.right, out);
        // Record the current node only after both subtrees are done.
        out.add(node.value);
    }

    /**
     * Level-order (breadth-first): visit the tree one level at a time, top to bottom.
     *
     * A queue holds the nodes waiting to be visited. A tree has no cycles, so
     * unlike graph BFS there is no visited set.
     */
    static List<Integer> levelOrder(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        if (root == null) {
            return order;
        }

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            order.add(curr.value);
            // Children join the back of the queue, so the whole current level is
            // visited before any node on the next level.
            if (curr.left != null) {
                queue.offer(curr.left);
            }
            if (curr.right != null) {
                queue.offer(curr.right);
            }
        }
        return order;
    }

    /**
     * Number of nodes on the longest root-to-leaf path - O(n).
     *
     * An empty tree has height 0 and a single node has height 1. (Some texts count
     * edges instead, which makes those -1 and 0.) This is post-order thinking:
     * each node needs the answer from both children first.
     */
    static int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Insert value into a BST - O(height). Returns the root of the subtree.
     *
     * Duplicates are ignored, so every value appears once. Inserting already
     * sorted values builds a tree that is really a linked list (height n), which
     * is why production code uses self-balancing trees (java.util.TreeMap is a
     * red-black tree).
     */
    static TreeNode insertBST(TreeNode node, int value) {
        // Base case: we found the empty spot where the new value belongs.
        if (node == null) {
            return new TreeNode(value);
        }

        if (value < node.value) {
            node.left = insertBST(node.left, value);
        } else if (value > node.value) {
            node.right = insertBST(node.right, value);
        }
        return node;
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

        List<Integer> post = new ArrayList<>();
        postorder(root, post);
        System.out.println("Post-order: " + post);

        System.out.println("Level-order: " + levelOrder(root));
        System.out.println("Height: " + height(root));

        System.out.println("searchBST(3): " + searchBST(root, 3));
        System.out.println("searchBST(7): " + searchBST(root, 7));

        // Building the same tree with insertBST: insertion order decides the shape.
        TreeNode built = null;
        for (int value : new int[] { 4, 2, 6, 1, 3, 5 }) {
            built = insertBST(built, value);
        }
        System.out.println("Built by insert, level-order: " + levelOrder(built));

        // Sorted input degenerates into a chain: height equals the node count.
        TreeNode chain = null;
        for (int value : new int[] { 1, 2, 3, 4, 5, 6 }) {
            chain = insertBST(chain, value);
        }
        System.out.println("Height after inserting sorted values: " + height(chain));
    }
}
