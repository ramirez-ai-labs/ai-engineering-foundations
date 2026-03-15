import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Graph representation and traversal: DFS and BFS.
 *
 * Graphs model relationships (e.g. knowledge graphs, state spaces, dependency DAGs).
 * DFS: stack (or recursion); BFS: queue. Both visit each node once with proper bookkeeping.
 */
public class GraphDFSAndBFS {

    // Adjacency list: node index -> list of neighbor indices
    static List<List<Integer>> buildGraph(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
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
        stack.push(start);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (visited.contains(u)) continue;
            visited.add(u);
            order.add(u);
            for (int v : adj.get(u)) {
                if (!visited.contains(v)) stack.push(v);
            }
        }
        return order;
    }

    /** BFS using queue — gives level-by-level order, shortest path in unweighted graph */
    static List<Integer> bfs(List<List<Integer>> adj, int start) {
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
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

    public static void main(String[] args) {
        // 0 -- 1
        // |    |
        // 2 -- 3
        int n = 4;
        int[][] edges = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 2, 3 } };
        List<List<Integer>> adj = buildGraph(n, edges);

        System.out.println("DFS from 0: " + dfs(adj, 0));
        System.out.println("BFS from 0: " + bfs(adj, 0));
    }
}
