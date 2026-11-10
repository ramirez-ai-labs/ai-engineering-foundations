"""Knowledge graph traversal using BFS.

A knowledge graph is a labeled, directed graph: nodes are entities (concepts,
documents, people, topics) and edges are typed relationships between them.

This demo uses a small AI engineering topic graph:

  Embeddings     --used_in-->      RAG
  RAG            --requires-->     Chunking
  RAG            --evaluated_by--> Recall@k
  RAG            --evaluated_by--> Groundedness
  Embeddings     --stored_in-->    VectorDatabase
  VectorDatabase --indexed_by-->   HNSW
  Transformers   --produce-->      Embeddings
  Transformers   --use-->          Attention
  Attention      --is_a-->         WeightedLookup

BFS from a query node finds every concept within N hops. Graph-based retrieval
systems (e.g. Microsoft GraphRAG, Wikidata-grounded QA) use the same traversal to
expand retrieval beyond direct semantic matches.

CS foundations connection: this is the BFS from
01-data-structures/graph_dfs_and_bfs.py, applied to named nodes and labeled,
one-way edges instead of numbered nodes in an undirected adjacency list. The
only additions are a hop limit and a record of how each node was reached.

Production connection: the retrieved subgraph is serialized as extra context
for the LLM, a structured alternative or complement to vector retrieval. Graph
databases such as Neo4j and Amazon Neptune run the same reachability logic at
scale. The hop limit matters there: a popular node can have thousands of
neighbors, so each extra hop can multiply the context you pull in.
"""

from collections import deque


class Edge:
    def __init__(self, relation: str, target: str) -> None:
        self.relation = relation
        self.target = target


class ReachedNode:
    def __init__(self, node: str, hops: int, path: str) -> None:
        self.node = node
        self.hops = hops
        # Human-readable route from the start node, e.g. "A --[rel]--> B".
        self.path = path


def add_edge(graph: dict[str, list[Edge]], source: str, relation: str, target: str) -> None:
    # Python shortcut: graph.setdefault(source, []).append(Edge(relation, target))
    if source not in graph:
        graph[source] = []
    graph[source].append(Edge(relation, target))


def build_graph() -> dict[str, list[Edge]]:
    graph: dict[str, list[Edge]] = {}
    add_edge(graph, "Embeddings", "used_in", "RAG")
    add_edge(graph, "RAG", "requires", "Chunking")
    add_edge(graph, "RAG", "evaluated_by", "Recall@k")
    add_edge(graph, "RAG", "evaluated_by", "Groundedness")
    add_edge(graph, "Embeddings", "stored_in", "VectorDatabase")
    add_edge(graph, "VectorDatabase", "indexed_by", "HNSW")
    add_edge(graph, "Transformers", "produce", "Embeddings")
    add_edge(graph, "Transformers", "use", "Attention")
    add_edge(graph, "Attention", "is_a", "WeightedLookup")
    return graph


def bfs_within_hops(
    graph: dict[str, list[Edge]],
    start_node: str,
    max_hops: int,
) -> list[ReachedNode]:
    """Every node reachable from start_node in 1..max_hops hops, in discovery order.

    The start node itself is not included. Each node appears once, with the
    fewest hops that reach it. A start node that is not in the graph has no
    outgoing edges, so the result is empty.
    """
    if max_hops < 0:
        raise ValueError("max_hops must be zero or greater")

    reached: list[ReachedNode] = []
    # Mark as visited when enqueued, as in plain BFS.
    visited: set[str] = set()
    visited.add(start_node)
    queue: deque[ReachedNode] = deque()
    queue.append(ReachedNode(start_node, 0, start_node))

    while len(queue) > 0:
        current = queue.popleft()
        if current.hops > 0:
            reached.append(current)

        # Stop expanding at the hop limit, but keep draining the queue.
        if current.hops >= max_hops:
            continue

        # Nodes with no outgoing edges (leaves) are not keys in the graph.
        edges: list[Edge] = []
        if current.node in graph:
            edges = graph[current.node]

        for edge in edges:
            if edge.target not in visited:
                visited.add(edge.target)
                next_path = current.path + " --[" + edge.relation + "]--> " + edge.target
                queue.append(ReachedNode(edge.target, current.hops + 1, next_path))
    return reached


def main() -> None:
    graph = build_graph()
    query_node = "Embeddings"
    max_hops = 2

    print(f"Knowledge graph BFS from: {query_node}")
    print(f"Max hops: {max_hops}")
    print()

    subgraph = bfs_within_hops(graph, query_node, max_hops)

    print(f"{'Node':<20} | {'Hops':>4} | Path")
    print("-" * 80)
    for reached in subgraph:
        print(f"{reached.node:<20} | {reached.hops:>4} | {reached.path}")

    print()
    print(f"Retrieved subgraph ({len(subgraph)} nodes within {max_hops} hops).")
    print("Transformers is missing: edges are one-way, and it points TO Embeddings.")
    print()
    print("Production interpretation:")
    print("  These nodes and their relationships would be serialized as structured")
    print("  context and added to the LLM prompt alongside (or instead of) chunks")
    print("  retrieved by vector similarity. Graph traversal catches concepts that")
    print("  are logically connected even when they are not close in embedding space.")


if __name__ == "__main__":
    main()
