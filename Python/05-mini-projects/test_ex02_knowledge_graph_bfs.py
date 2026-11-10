"""Tests for ex02_knowledge_graph_bfs. Run from this folder: python -m unittest -v"""

import unittest

from ex02_knowledge_graph_bfs import Edge, ReachedNode, add_edge, bfs_within_hops, build_graph


def node_names(reached: list[ReachedNode]) -> list[str]:
    names: list[str] = []
    for item in reached:
        names.append(item.node)
    return names


class BfsWithinHopsTest(unittest.TestCase):
    def setUp(self) -> None:
        self.graph = build_graph()

    def test_one_hop_returns_direct_neighbors(self) -> None:
        reached = bfs_within_hops(self.graph, "Embeddings", 1)
        self.assertEqual(node_names(reached), ["RAG", "VectorDatabase"])

    def test_two_hops_in_discovery_order(self) -> None:
        reached = bfs_within_hops(self.graph, "Embeddings", 2)
        self.assertEqual(
            node_names(reached),
            ["RAG", "VectorDatabase", "Chunking", "Recall@k", "Groundedness", "HNSW"],
        )

    def test_hop_counts(self) -> None:
        for item in bfs_within_hops(self.graph, "Embeddings", 2):
            # RAG and VectorDatabase are Embeddings' direct neighbors.
            if item.node == "RAG" or item.node == "VectorDatabase":
                self.assertEqual(item.hops, 1, item.node)
            else:
                self.assertEqual(item.hops, 2, item.node)

    def test_path_records_relations(self) -> None:
        reached = bfs_within_hops(self.graph, "Embeddings", 2)
        paths: dict[str, str] = {}
        for item in reached:
            paths[item.node] = item.path
        self.assertEqual(
            paths["HNSW"],
            "Embeddings --[stored_in]--> VectorDatabase --[indexed_by]--> HNSW",
        )

    def test_edges_are_directed(self) -> None:
        # Transformers points TO Embeddings, so it is not reachable FROM it.
        reached = bfs_within_hops(self.graph, "Embeddings", 10)
        self.assertNotIn("Transformers", node_names(reached))

    def test_large_hop_limit_reaches_everything_downstream(self) -> None:
        reached = bfs_within_hops(self.graph, "Transformers", 10)
        self.assertEqual(len(reached), 9)

    def test_start_node_is_excluded(self) -> None:
        self.assertNotIn("Embeddings", node_names(bfs_within_hops(self.graph, "Embeddings", 3)))

    def test_zero_hops_returns_nothing(self) -> None:
        self.assertEqual(bfs_within_hops(self.graph, "Embeddings", 0), [])

    def test_leaf_and_unknown_nodes_return_nothing(self) -> None:
        self.assertEqual(bfs_within_hops(self.graph, "HNSW", 3), [])
        self.assertEqual(bfs_within_hops(self.graph, "NotInGraph", 3), [])

    def test_negative_hops_raises(self) -> None:
        with self.assertRaises(ValueError):
            bfs_within_hops(self.graph, "Embeddings", -1)


class CycleTest(unittest.TestCase):
    def test_cycle_visits_each_node_once_with_fewest_hops(self) -> None:
        graph: dict[str, list[Edge]] = {}
        add_edge(graph, "A", "next", "B")
        add_edge(graph, "B", "next", "C")
        add_edge(graph, "C", "next", "A")
        add_edge(graph, "A", "shortcut", "C")
        reached = bfs_within_hops(graph, "A", 5)
        self.assertEqual(node_names(reached), ["B", "C"])
        # C is reached directly via the shortcut, not in two hops via B.
        self.assertEqual(reached[1].hops, 1)


if __name__ == "__main__":
    unittest.main()
