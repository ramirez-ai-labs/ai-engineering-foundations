"""Graph representation and traversal: DFS and BFS.

Graphs model relationships (e.g. knowledge graphs, state spaces, dependency DAGs).
DFS: stack (or recursion); BFS: queue. Both visit each node once with proper bookkeeping.

Both traversals are O(V + E): every node is visited once and every adjacency
list is scanned once.

Why the two DFS versions print different orders on the same graph:
- recursive DFS explores neighbors in list order, so it goes 0 -> 1 first
- iterative DFS pushes every neighbor, then pops the LAST one pushed, so it goes
  0 -> 2 first
Both are valid depth-first orders. To make the iterative version match the
recursive one, push neighbors in reverse order.
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


def dfs_recursive(adjacency: list[list[int]], start: int) -> list[int]:
    """DFS using recursion - the call stack plays the role of the explicit stack.

    Shorter to write, but each level of depth is a Python stack frame, and
    Python stops at about 1000 frames by default (RecursionError). Long paths in
    real graphs are why production code prefers the iterative version.
    """
    order: list[int] = []
    visited: set[int] = set()
    _dfs_visit(adjacency, start, visited, order)
    return order


def _dfs_visit(
    adjacency: list[list[int]],
    current: int,
    visited: set[int],
    order: list[int],
) -> None:
    visited.add(current)
    order.append(current)
    for neighbor in adjacency[current]:
        # Go as deep as possible down this neighbor before trying the next one.
        if neighbor not in visited:
            _dfs_visit(adjacency, neighbor, visited, order)


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


def shortest_path(adjacency: list[list[int]], start: int, goal: int) -> list[int]:
    """Shortest path (fewest edges) from start to goal, using BFS.

    Returns the path as a list of nodes, or an empty list if goal is unreachable.
    BFS reaches nodes in order of distance, so the first time it reaches goal is
    along a shortest path. This only holds for unweighted graphs; weighted
    graphs need Dijkstra's algorithm.
    """
    if start == goal:
        return [start]

    # parent[node] is the node we came from when we first reached it. Following
    # parents back from goal rebuilds the path.
    parent: dict[int, int] = {}
    visited: set[int] = set()
    visited.add(start)
    queue: deque[int] = deque()
    queue.append(start)

    while len(queue) > 0:
        current = queue.popleft()
        for neighbor in adjacency[current]:
            if neighbor not in visited:
                visited.add(neighbor)
                parent[neighbor] = current
                if neighbor == goal:
                    return _build_path(parent, start, goal)
                queue.append(neighbor)
    return []


def _build_path(parent: dict[int, int], start: int, goal: int) -> list[int]:
    # Walk backwards from goal to start, then reverse into start-to-goal order.
    backwards: list[int] = []
    node = goal
    while node != start:
        backwards.append(node)
        node = parent[node]
    backwards.append(start)

    # Python shortcut: backwards.reverse() or backwards[::-1]
    path: list[int] = []
    for index in range(len(backwards) - 1, -1, -1):
        path.append(backwards[index])
    return path


def main() -> None:
    # 0 -- 1
    # |    |
    # 2 -- 3
    node_count = 4
    edges = [(0, 1), (0, 2), (1, 3), (2, 3)]
    adjacency = build_graph(node_count, edges)

    # The traversals visit the same graph in different orders.
    print(f"DFS (iterative) from 0: {dfs(adjacency, 0)}")  # [0, 2, 3, 1]
    print(f"DFS (recursive) from 0: {dfs_recursive(adjacency, 0)}")  # [0, 1, 3, 2]
    print(f"BFS from 0: {bfs(adjacency, 0)}")
    print(f"Shortest path 0 -> 3: {shortest_path(adjacency, 0, 3)}")


if __name__ == "__main__":
    main()
