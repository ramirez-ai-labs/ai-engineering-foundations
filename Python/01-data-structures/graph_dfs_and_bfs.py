"""Graph representation and traversal: DFS and BFS.

Graphs model relationships (e.g. knowledge graphs, state spaces, dependency DAGs).
DFS: stack (or recursion); BFS: queue. Both visit each node once with proper bookkeeping.
"""

from collections import deque


def build_graph(node_count: int, edges: list[tuple[int, int]]) -> list[list[int]]:
    """Adjacency list: node index -> list of neighbor indices."""
    # Start with one empty neighbor list per node.
    adjacency: list[list[int]] = []
    for node in range(node_count):
        adjacency.append([])

    for edge in edges:
        first = edge[0]
        second = edge[1]
        # Because this graph is undirected, every edge appears in both lists.
        adjacency[first].append(second)
        adjacency[second].append(first)
    return adjacency


def dfs(adjacency: list[list[int]], start: int) -> list[int]:
    """DFS using an explicit stack (a plain list: append/pop from the end)."""
    order: list[int] = []
    visited: set[int] = set()
    stack = [start]

    while len(stack) > 0:
        current = stack.pop()
        # A node may be pushed more than once, so skip it if already processed.
        if current in visited:
            continue
        visited.add(current)
        order.append(current)

        # Push neighbors so they can be explored later. A stack is last-in,
        # first-out, so the neighbor pushed last is explored first.
        for neighbor in adjacency[current]:
            if neighbor not in visited:
                stack.append(neighbor)
    return order


def bfs(adjacency: list[list[int]], start: int) -> list[int]:
    """BFS using a queue - level-by-level order, shortest path in an unweighted graph."""
    order: list[int] = []
    # Mark as visited when enqueued so we do not add the same node repeatedly.
    visited: set[int] = set()
    visited.add(start)
    # deque gives O(1) popleft; list.pop(0) would be O(n).
    queue: deque[int] = deque()
    queue.append(start)

    while len(queue) > 0:
        current = queue.popleft()
        order.append(current)
        for neighbor in adjacency[current]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)
    return order


def main() -> None:
    # 0 -- 1
    # |    |
    # 2 -- 3
    node_count = 4
    edges = [(0, 1), (0, 2), (1, 3), (2, 3)]
    adjacency = build_graph(node_count, edges)

    # The two traversals visit the same graph in different orders.
    print(f"DFS from 0: {dfs(adjacency, 0)}")
    print(f"BFS from 0: {bfs(adjacency, 0)}")


if __name__ == "__main__":
    main()
