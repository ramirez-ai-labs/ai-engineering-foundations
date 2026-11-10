import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Tests for KnowledgeGraphBFS. Run: javac *.java && java KnowledgeGraphBFSTest */
public class KnowledgeGraphBFSTest {

    static List<String> names(List<KnowledgeGraphBFS.ReachedNode> reached) {
        List<String> names = new ArrayList<>();
        for (KnowledgeGraphBFS.ReachedNode node : reached) {
            names.add(node.node);
        }
        return names;
    }

    static void run() {
        Map<String, List<KnowledgeGraphBFS.Edge>> graph = KnowledgeGraphBFS.buildGraph();

        Check.equal(List.of("RAG", "VectorDatabase"), names(KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 1)),
                "one hop returns direct neighbors");

        List<KnowledgeGraphBFS.ReachedNode> twoHops = KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 2);
        Check.equal(List.of("RAG", "VectorDatabase", "Chunking", "Recall@k", "Groundedness", "HNSW"), names(twoHops),
                "two hops in discovery order");
        for (KnowledgeGraphBFS.ReachedNode node : twoHops) {
            int expectedHops = node.node.equals("RAG") || node.node.equals("VectorDatabase") ? 1 : 2;
            Check.equal(expectedHops, node.hops, "hop count for " + node.node);
            if (node.node.equals("HNSW")) {
                Check.equal("Embeddings --[stored_in]--> VectorDatabase --[indexed_by]--> HNSW", node.path,
                        "path records relations");
            }
        }

        // Transformers points TO Embeddings, so it is not reachable FROM it.
        Check.isFalse(names(KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 10)).contains("Transformers"),
                "edges are directed");
        Check.equal(9, KnowledgeGraphBFS.bfsWithinHops(graph, "Transformers", 10).size(),
                "large hop limit reaches everything downstream");
        Check.isFalse(names(KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 3)).contains("Embeddings"),
                "start node is excluded");
        Check.equal(0, KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", 0).size(), "zero hops returns nothing");
        Check.equal(0, KnowledgeGraphBFS.bfsWithinHops(graph, "HNSW", 3).size(), "leaf node returns nothing");
        Check.equal(0, KnowledgeGraphBFS.bfsWithinHops(graph, "NotInGraph", 3).size(), "unknown node returns nothing");
        Check.throwsIllegalArgument(() -> KnowledgeGraphBFS.bfsWithinHops(graph, "Embeddings", -1),
                "negative hops throws");

        // A cycle: each node is reported once, with the fewest hops.
        Map<String, List<KnowledgeGraphBFS.Edge>> cycle = new HashMap<>();
        KnowledgeGraphBFS.addEdge(cycle, "A", "next", "B");
        KnowledgeGraphBFS.addEdge(cycle, "B", "next", "C");
        KnowledgeGraphBFS.addEdge(cycle, "C", "next", "A");
        KnowledgeGraphBFS.addEdge(cycle, "A", "shortcut", "C");
        List<KnowledgeGraphBFS.ReachedNode> reached = KnowledgeGraphBFS.bfsWithinHops(cycle, "A", 5);
        Check.equal(List.of("B", "C"), names(reached), "cycle visits each node once");
        // C is reached directly via the shortcut, not in two hops via B.
        Check.equal(1, reached.get(1).hops, "cycle reports the fewest hops");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
