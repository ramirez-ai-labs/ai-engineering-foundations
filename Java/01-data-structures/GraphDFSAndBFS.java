import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph representation and traversal: DFS and BFS.
 *
 * Graphs model relationships (e.g. knowledge graphs, state spaces, dependency DAGs).
 * DFS: stack (or recursion); BFS: queue. Both visit each node once with proper bookkeeping.
 *
 * Both traversals are O(V + E): every node is visited once and every adjacency
 * list is scanned once.
 *
 * Why the two DFS versions print different orders on the same graph:
 * - recursive DFS explores neighbors in list order, so it goes 0 -> 1 first
 * - iterative DFS pushes every neighbor, then pops the LAST one pushed, so it
 *   goes 0 -> 2 first
 * Both are valid depth-first orders. To make the iterative version match the
 * recursive one, push neighbors in reverse order.
 */
public class GraphDFSAndBFS {

    // Adjacency list: node index -> list of neighbor indices
    static List<List<Integer>> buildGraph(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        // Start with n empty neighbor lists.
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            // Because this graph is undirected, every edge appears in both lists.
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]); // undirected
        }
        return adj;
    }

    /** DFS using explicit stack */
    static List<Integer> dfs(List<List<Integer>> adj, int start) {
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();

        // DFS explores by going deep first, so we use a stack.
        stack.push(start);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            // A node may be pushed more than once, so skip it if already processed.
            if (visited.contains(u)) {
                continue;
            }
            visited.add(u);
            order.add(u);

            // Push neighbors so they can be explored later. A stack is last-in,
            // first-out, so the neighbor pushed last is explored first.
            for (int v : adj.get(u)) {
                if (!visited.contains(v)) {
                    stack.push(v);
                }
            }
        }
        return order;
    }

    /**
     * DFS using recursion - the call stack plays the role of the explicit stack.
     *
     * Shorter to write, but each level of depth is a stack frame, and a deep
     * enough graph throws StackOverflowError. Long paths in real graphs are why
     * production code prefers the iterative version.
     */
    static List<Integer> dfsRecursive(List<List<Integer>> adj, int start) {
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsVisit(adj, start, visited, order);
        return order;
    }

    private static void dfsVisit(List<List<Integer>> adj, int u, Set<Integer> visited, List<Integer> order) {
        visited.add(u);
        order.add(u);
        for (int v : adj.get(u)) {
            // Go as deep as possible down this neighbor before trying the next one.
            if (!visited.contains(v)) {
                dfsVisit(adj, v, visited, order);
            }
        }
    }

    /** BFS using queue - gives level-by-level order, shortest path in an unweighted graph */
    static List<Integer> bfs(List<List<Integer>> adj, int start) {
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();

        // BFS explores level by level, so it uses a queue.
        queue.offer(start);
        // Mark as visited when enqueued so we do not add the same node repeatedly.
        visited.add(start);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.offer(v);
                }
            }
        }
        return order;
    }

    /**
     * Shortest path (fewest edges) from start to goal, using BFS.
     *
     * Returns the path as a list of nodes, or an empty list if goal is unreachable.
     * BFS reaches nodes in order of distance, so the first time it reaches goal is
     * along a shortest path. This only holds for unweighted graphs; weighted
     * graphs need Dijkstra's algorithm.
     */
    static List<Integer> shortestPath(List<List<Integer>> adj, int start, int goal) {
        if (start == goal) {
            List<Integer> single = new ArrayList<>();
            single.add(start);
            return single;
        }

        // parent[node] is the node we came from when we first reached it.
        // Following parents back from goal rebuilds the path.
        Map<Integer, Integer> parent = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adj.get(u)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    parent.put(v, u);
                    if (v == goal) {
                        return buildPath(parent, start, goal);
                    }
                    queue.offer(v);
                }
            }
        }
        return new ArrayList<>();
    }

    private static List<Integer> buildPath(Map<Integer, Integer> parent, int start, int goal) {
        // Walk backwards from goal to start, then reverse into start-to-goal order.
        List<Integer> path = new ArrayList<>();
        int node = goal;
        while (node != start) {
            path.add(node);
            node = parent.get(node);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        // 0 -- 1
        // |    |
        // 2 -- 3
        int n = 4;
        int[][] edges = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 2, 3 } };
        List<List<Integer>> adj = buildGraph(n, edges);

        // The traversals visit the same graph in different orders.
        System.out.println("DFS (iterative) from 0: " + dfs(adj, 0)); // [0, 2, 3, 1]
        System.out.println("DFS (recursive) from 0: " + dfsRecursive(adj, 0)); // [0, 1, 3, 2]
        System.out.println("BFS from 0: " + bfs(adj, 0));
        System.out.println("Shortest path 0 -> 3: " + shortestPath(adj, 0, 3));
    }
}
