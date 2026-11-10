import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Tests for Ex02KnowledgeGraphBFS. Run: javac *.java && java Ex02KnowledgeGraphBFSTest */
public class Ex02KnowledgeGraphBFSTest {

    static List<String> names(List<Ex02KnowledgeGraphBFS.ReachedNode> reached) {
        List<String> names = new ArrayList<>();
        for (Ex02KnowledgeGraphBFS.ReachedNode node : reached) {
            names.add(node.node);
        }
        return names;
    }

    static void run() {
        Map<String, List<Ex02KnowledgeGraphBFS.Edge>> graph = Ex02KnowledgeGraphBFS.buildGraph();

        MiniProjectsCheck.equal(List.of("RAG", "VectorDatabase"), names(Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 1)),
                "one hop returns direct neighbors");

        List<Ex02KnowledgeGraphBFS.ReachedNode> twoHops = Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 2);
        MiniProjectsCheck.equal(List.of("RAG", "VectorDatabase", "Chunking", "Recall@k", "Groundedness", "HNSW"), names(twoHops),
                "two hops in discovery order");
        for (Ex02KnowledgeGraphBFS.ReachedNode node : twoHops) {
            int expectedHops = node.node.equals("RAG") || node.node.equals("VectorDatabase") ? 1 : 2;
            MiniProjectsCheck.equal(expectedHops, node.hops, "hop count for " + node.node);
            if (node.node.equals("HNSW")) {
                MiniProjectsCheck.equal("Embeddings --[stored_in]--> VectorDatabase --[indexed_by]--> HNSW", node.path,
                        "path records relations");
            }
        }

        // Transformers points TO Embeddings, so it is not reachable FROM it.
        MiniProjectsCheck.isFalse(names(Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 10)).contains("Transformers"),
                "edges are directed");
        MiniProjectsCheck.equal(9, Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "Transformers", 10).size(),
                "large hop limit reaches everything downstream");
        MiniProjectsCheck.isFalse(names(Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 3)).contains("Embeddings"),
                "start node is excluded");
        MiniProjectsCheck.equal(0, Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 0).size(), "zero hops returns nothing");
        MiniProjectsCheck.equal(0, Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "HNSW", 3).size(), "leaf node returns nothing");
        MiniProjectsCheck.equal(0, Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "NotInGraph", 3).size(), "unknown node returns nothing");
        MiniProjectsCheck.throwsIllegalArgument(() -> Ex02KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", -1),
                "negative hops throws");

        // A cycle: each node is reported once, with the fewest hops.
        Map<String, List<Ex02KnowledgeGraphBFS.Edge>> cycle = new HashMap<>();
        Ex02KnowledgeGraphBFS.addEdge(cycle, "A", "next", "B");
        Ex02KnowledgeGraphBFS.addEdge(cycle, "B", "next", "C");
        Ex02KnowledgeGraphBFS.addEdge(cycle, "C", "next", "A");
        Ex02KnowledgeGraphBFS.addEdge(cycle, "A", "shortcut", "C");
        List<Ex02KnowledgeGraphBFS.ReachedNode> reached = Ex02KnowledgeGraphBFS.bfsWithinHops(cycle, "A", 5);
        MiniProjectsCheck.equal(List.of("B", "C"), names(reached), "cycle visits each node once");
        // C is reached directly via the shortcut, not in two hops via B.
        MiniProjectsCheck.equal(1, reached.get(1).hops, "cycle reports the fewest hops");
    }

    public static void main(String[] args) {
        run();
        MiniProjectsCheck.finish();
    }
}
