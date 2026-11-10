import java.util.ArrayList;
import java.util.List;

/** Tests for GraphDFSAndBFS. Run: javac *.java && java GraphDFSAndBFSTest */
public class GraphDFSAndBFSTest {

    // 0 -- 1
    // |    |
    // 2 -- 3
    static final int[][] SQUARE_EDGES = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 2, 3 } };

    static List<Integer> sorted(List<Integer> values) {
        List<Integer> copy = new ArrayList<>(values);
        copy.sort(null);
        return copy;
    }

    static void run() {
        List<List<Integer>> square = GraphDFSAndBFS.buildGraph(4, SQUARE_EDGES);

        // Building the graph
        Check.equal(List.of(List.of(1, 2), List.of(0, 3), List.of(0, 3), List.of(1, 2)), square,
                "undirected edges appear in both lists");
        Check.equal(List.of(List.of(), List.of(), List.of()), GraphDFSAndBFS.buildGraph(3, new int[0][]),
                "isolated nodes have empty lists");

        // Traversal order
        Check.equal(List.of(0, 2, 3, 1), GraphDFSAndBFS.dfs(square, 0),
                "iterative DFS explores the last pushed neighbor first");
        Check.equal(List.of(0, 1, 3, 2), GraphDFSAndBFS.dfsRecursive(square, 0),
                "recursive DFS explores neighbors in list order");
        Check.equal(List.of(0, 1, 2, 3), GraphDFSAndBFS.bfs(square, 0), "BFS visits level by level");
        Check.equal(List.of(3, 1, 2, 0), GraphDFSAndBFS.bfs(square, 3), "BFS from another start node");

        // Properties every traversal must have, whatever order it picks
        List<List<Integer>> triangle = GraphDFSAndBFS.buildGraph(3, new int[][] { { 0, 1 }, { 1, 2 }, { 2, 0 } });
        List<List<Integer>> split = GraphDFSAndBFS.buildGraph(4, new int[][] { { 0, 1 }, { 2, 3 } });
        List<List<Integer>> single = GraphDFSAndBFS.buildGraph(1, new int[0][]);
        for (String name : List.of("dfs", "dfsRecursive", "bfs")) {
            Check.equal(List.of(0, 1, 2), sorted(traverse(name, triangle)), name + ": cycle causes no repeats");
            Check.equal(List.of(0, 1), sorted(traverse(name, split)), name + ": only reachable nodes visited");
            Check.equal(List.of(0), traverse(name, single), name + ": single isolated node");
        }

        // Shortest path
        // 0-1-2-3-4 is a long way round; the edge 0-4 is the shortcut.
        List<List<Integer>> ring = GraphDFSAndBFS.buildGraph(5,
                new int[][] { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 0, 4 } });
        Check.equal(List.of(0, 4, 3), GraphDFSAndBFS.shortestPath(ring, 0, 3), "shortest path uses fewest edges");
        Check.equal(3, GraphDFSAndBFS.shortestPath(square, 0, 3).size(), "square graph path has 3 nodes");
        Check.equal(List.of(1), GraphDFSAndBFS.shortestPath(square, 1, 1), "start equals goal");
        Check.equal(List.of(), GraphDFSAndBFS.shortestPath(split, 0, 3), "unreachable goal returns empty list");
        List<List<Integer>> line = GraphDFSAndBFS.buildGraph(6,
                new int[][] { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 } });
        Check.equal(List.of(0, 1, 2, 3, 4, 5), GraphDFSAndBFS.shortestPath(line, 0, 5), "path along a line");
    }

    static List<Integer> traverse(String name, List<List<Integer>> adj) {
        if (name.equals("dfs")) {
            return GraphDFSAndBFS.dfs(adj, 0);
        }
        if (name.equals("dfsRecursive")) {
            return GraphDFSAndBFS.dfsRecursive(adj, 0);
        }
        return GraphDFSAndBFS.bfs(adj, 0);
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
