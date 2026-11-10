"""Tests for ex05_graph_dfs_and_bfs. Run from this folder: python -m unittest -v"""

import unittest

from ex05_graph_dfs_and_bfs import bfs, build_graph, dfs, dfs_recursive, shortest_path

# 0 -- 1
# |    |
# 2 -- 3
SQUARE_EDGES = [(0, 1), (0, 2), (1, 3), (2, 3)]


class BuildGraphTest(unittest.TestCase):
    def test_undirected_edges_appear_in_both_lists(self) -> None:
        adjacency = build_graph(4, SQUARE_EDGES)
        self.assertEqual(adjacency, [[1, 2], [0, 3], [0, 3], [1, 2]])

    def test_isolated_nodes_have_empty_lists(self) -> None:
        self.assertEqual(build_graph(3, []), [[], [], []])


class TraversalOrderTest(unittest.TestCase):
    def setUp(self) -> None:
        self.adjacency = build_graph(4, SQUARE_EDGES)

    def test_iterative_dfs_explores_last_pushed_neighbor_first(self) -> None:
        self.assertEqual(dfs(self.adjacency, 0), [0, 2, 3, 1])

    def test_recursive_dfs_explores_neighbors_in_list_order(self) -> None:
        self.assertEqual(dfs_recursive(self.adjacency, 0), [0, 1, 3, 2])

    def test_bfs_visits_level_by_level(self) -> None:
        self.assertEqual(bfs(self.adjacency, 0), [0, 1, 2, 3])

    def test_start_from_another_node(self) -> None:
        self.assertEqual(bfs(self.adjacency, 3), [3, 1, 2, 0])


class TraversalCoverageTest(unittest.TestCase):
    """Properties every traversal must have, whatever order it picks."""

    TRAVERSALS = [dfs, dfs_recursive, bfs]

    def test_cycle_does_not_cause_repeats(self) -> None:
        triangle = build_graph(3, [(0, 1), (1, 2), (2, 0)])
        for traversal in self.TRAVERSALS:
            order = traversal(triangle, 0)
            self.assertEqual(sorted(order), [0, 1, 2], traversal.__name__)

    def test_only_reachable_nodes_are_visited(self) -> None:
        # Two components: 0-1 and 2-3.
        split = build_graph(4, [(0, 1), (2, 3)])
        for traversal in self.TRAVERSALS:
            self.assertEqual(sorted(traversal(split, 0)), [0, 1], traversal.__name__)

    def test_single_isolated_node(self) -> None:
        for traversal in self.TRAVERSALS:
            self.assertEqual(traversal(build_graph(1, []), 0), [0], traversal.__name__)


class ShortestPathTest(unittest.TestCase):
    def test_finds_fewest_edges(self) -> None:
        # 0-1-2-3-4 is a long way round; the edge 0-4 is the shortcut.
        adjacency = build_graph(5, [(0, 1), (1, 2), (2, 3), (3, 4), (0, 4)])
        self.assertEqual(shortest_path(adjacency, 0, 3), [0, 4, 3])

    def test_square_graph(self) -> None:
        adjacency = build_graph(4, SQUARE_EDGES)
        self.assertEqual(len(shortest_path(adjacency, 0, 3)), 3)

    def test_start_equals_goal(self) -> None:
        self.assertEqual(shortest_path(build_graph(2, [(0, 1)]), 1, 1), [1])

    def test_unreachable_goal_returns_empty(self) -> None:
        adjacency = build_graph(4, [(0, 1), (2, 3)])
        self.assertEqual(shortest_path(adjacency, 0, 3), [])

    def test_path_starts_and_ends_correctly(self) -> None:
        adjacency = build_graph(6, [(0, 1), (1, 2), (2, 3), (3, 4), (4, 5)])
        self.assertEqual(shortest_path(adjacency, 0, 5), [0, 1, 2, 3, 4, 5])


if __name__ == "__main__":
    unittest.main()
