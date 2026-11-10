import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Knowledge graph traversal using BFS.
 *
 * A knowledge graph is a labeled graph where nodes are entities (concepts, documents,
 * people, topics) and edges are typed relationships between them.
 *
 * This demo uses a small AI engineering topic graph:
 *
 *   Embeddings ── used_in ──► RAG
 *   RAG ── requires ──► Chunking
 *   RAG ── evaluated_by ──► Recall@k
 *   RAG ── evaluated_by ──► Groundedness
 *   Embeddings ── stored_in ──► VectorDatabase
 *   VectorDatabase ── indexed_by ──► HNSW
 *   Transformers ── produce ──► Embeddings
 *   Transformers ── use ──► Attention
 *   Attention ── is_a ──► WeightedLookup
 *
 * BFS from a query node finds all related concepts within N hops. This is the
 * same traversal used in graph-based retrieval systems (e.g. Microsoft GraphRAG,
 * Wikidata-grounded QA) to expand retrieval beyond direct semantic matches.
 *
 * CS foundations connection:
 *   This is the same BFS algorithm from 01-data-structures/GraphDFSAndBFS.java,
 *   applied to a named, directed, labeled graph instead of an anonymous adjacency list.
 *
 * Production connection:
 *   In enterprise knowledge graphs, BFS within N hops retrieves a subgraph of
 *   related entities. That subgraph is then serialized as additional context for
 *   the LLM — effectively a structured alternative to pure vector retrieval.
 *   Systems like Neo4j, Amazon Neptune, or Wikidata's SPARQL endpoint run the same
 *   reachability logic at scale.
 */
public class KnowledgeGraphBFS {

    static class Edge {
        final String relation;
        final String target;

        Edge(String relation, String target) {
            this.relation = relation;
            this.target = target;
        }
    }

    static class ReachedNode {
        final String node;
        final int hops;
        final String path; // human-readable traversal path

        ReachedNode(String node, int hops, String path) {
            this.node = node;
            this.hops = hops;
            this.path = path;
        }
    }

    static Map<String, List<Edge>> buildGraph() {
        Map<String, List<Edge>> graph = new HashMap<>();

        addEdge(graph, "Embeddings",     "used_in",      "RAG");
        addEdge(graph, "RAG",            "requires",     "Chunking");
        addEdge(graph, "RAG",            "evaluated_by", "Recall@k");
        addEdge(graph, "RAG",            "evaluated_by", "Groundedness");
        addEdge(graph, "Embeddings",     "stored_in",    "VectorDatabase");
        addEdge(graph, "VectorDatabase", "indexed_by",   "HNSW");
        addEdge(graph, "Transformers",   "produce",      "Embeddings");
        addEdge(graph, "Transformers",   "use",          "Attention");
        addEdge(graph, "Attention",      "is_a",         "WeightedLookup");

        return graph;
    }

    static void addEdge(Map<String, List<Edge>> graph, String from, String relation, String to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(relation, to));
    }

    /**
     * BFS from startNode up to maxHops hops. Returns all reachable nodes in order of
     * discovery, along with the hop count and the traversal path that reached them.
     *
     * The start node itself is not included. Each node appears once, with the
     * fewest hops that reach it. A start node that is not in the graph has no
     * outgoing edges, so the result is empty.
     */
    static List<ReachedNode> bfsWithinHops(Map<String, List<Edge>> graph, String startNode, int maxHops) {
        if (maxHops < 0) {
            throw new IllegalArgumentException("maxHops must be zero or greater");
        }

        List<ReachedNode> reached = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Deque<ReachedNode> queue = new ArrayDeque<>();

        visited.add(startNode);
        queue.offer(new ReachedNode(startNode, 0, startNode));

        while (!queue.isEmpty()) {
            ReachedNode current = queue.poll();

            if (current.hops > 0) {
                reached.add(current);
            }

            if (current.hops >= maxHops) {
                continue;
            }

            List<Edge> edges = graph.containsKey(current.node) ? graph.get(current.node) : new ArrayList<Edge>();
            for (Edge edge : edges) {
                if (!visited.contains(edge.target)) {
                    visited.add(edge.target);
                    String nextPath = current.path + " --[" + edge.relation + "]--> " + edge.target;
                    queue.offer(new ReachedNode(edge.target, current.hops + 1, nextPath));
                }
            }
        }

        return reached;
    }

    public static void main(String[] args) {
        Map<String, List<Edge>> graph = buildGraph();

        String queryNode = "Embeddings";
        int maxHops = 2;

        System.out.println("Knowledge graph BFS from: " + queryNode);
        System.out.println("Max hops: " + maxHops);
        System.out.println();

        List<ReachedNode> subgraph = bfsWithinHops(graph, queryNode, maxHops);

        System.out.printf("%-20s | %5s | %s%n", "Node", "Hops", "Path");
        System.out.println("--------------------------------------------------------------------------------");
        for (ReachedNode n : subgraph) {
            System.out.printf("%-20s | %5d | %s%n", n.node, n.hops, n.path);
        }

        System.out.println();
        System.out.println("Retrieved subgraph (" + subgraph.size() + " nodes within " + maxHops + " hops):");
        for (ReachedNode n : subgraph) {
            System.out.println("  " + n.node);
        }

        System.out.println();
        System.out.println("Production interpretation:");
        System.out.println("  These " + subgraph.size() + " nodes and their relationships would be serialized");
        System.out.println("  as structured context and injected into the LLM prompt alongside");
        System.out.println("  (or instead of) chunks retrieved by vector similarity.");
        System.out.println("  Graph traversal catches related concepts that may not be");
        System.out.println("  semantically close in embedding space but are logically connected.");
    }
}
