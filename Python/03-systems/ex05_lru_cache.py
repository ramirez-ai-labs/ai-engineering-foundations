"""LRU (Least Recently Used) cache built from scratch: a dict plus a doubly linked list.

When the cache is full and a new key arrives, the key that was used least
recently is evicted. Both get() and put() are O(1):
- the dict maps key -> list node, so finding an entry is O(1)
- the doubly linked list keeps entries in recency order, and because each node
  knows both neighbors, unlinking it and moving it to the front is O(1)

Neither structure is enough alone. A dict cannot tell you which key is oldest
without scanning, and a linked list cannot find a key without walking it.

Sentinel nodes: the list always contains two empty marker nodes, head and tail.
Real entries live between them, so every real node has a non-None prev and next,
and insert/unlink never need special cases for an empty list or the ends.

  head <-> most recent <-> ... <-> least recent <-> tail

AI engineering connection: inference servers cache attention key/value tensors
for repeated prompt prefixes (such as a shared system prompt) and evict them
with LRU when GPU memory runs low. Embedding and response caches use the same
policy to keep hot entries in memory.

Python shortcuts: collections.OrderedDict with move_to_end() and
popitem(last=False) gives you this in a few lines, and functools.lru_cache
memoizes a function. The Java track's Ex05LRUCache.java builds the same
structure by hand; its Javadoc shows the LinkedHashMap shortcut, which is the
Java equivalent of OrderedDict.

Limitations: not thread-safe, string keys and values only, no TTL. Combining
this with ex02_in_memory_cache.py's lazy expiry is a good next exercise.
"""

# Lets the _Node class use its own name in its type hints.
from __future__ import annotations


class _Node:
    """One cache entry, linked to its neighbors in recency order."""

    def __init__(self, key: str, value: str) -> None:
        self.key = key
        self.value = value
        self.prev: _Node | None = None
        self.next: _Node | None = None


class LRUCache:
    def __init__(self, capacity: int) -> None:
        if capacity <= 0:
            raise ValueError("capacity must be positive")
        self._capacity = capacity
        self._nodes: dict[str, _Node] = {}

        # Sentinels hold no data. head.next is the most recently used entry and
        # tail.prev is the least recently used one.
        self._head = _Node("", "")
        self._tail = _Node("", "")
        self._head.next = self._tail
        self._tail.prev = self._head

    def get(self, key: str) -> str | None:
        """Return the value for key, or None if missing. A hit counts as a use."""
        if key not in self._nodes:
            return None
        node = self._nodes[key]
        self._move_to_front(node)
        return node.value

    def put(self, key: str, value: str) -> None:
        """Insert or update key, evicting the least recently used key if full."""
        if key in self._nodes:
            # Updating an existing key never evicts, but it does count as a use.
            node = self._nodes[key]
            node.value = value
            self._move_to_front(node)
            return

        if len(self._nodes) == self._capacity:
            self._evict_least_recent()

        node = _Node(key, value)
        self._nodes[key] = node
        self._add_to_front(node)

    def size(self) -> int:
        return len(self._nodes)

    def keys_most_recent_first(self) -> list[str]:
        """Keys in recency order, for inspecting the cache. Does not count as a use."""
        keys: list[str] = []
        current = self._head.next
        while current is not self._tail:
            keys.append(current.key)
            current = current.next
        return keys

    def keys_least_recent_first(self) -> list[str]:
        """Keys walked from the tail via prev pointers. Tests use it to check both directions."""
        keys: list[str] = []
        current = self._tail.prev
        while current is not self._head:
            keys.append(current.key)
            current = current.prev
        return keys

    def _add_to_front(self, node: _Node) -> None:
        # Splice node in between head and the current first entry.
        first = self._head.next
        node.prev = self._head
        node.next = first
        first.prev = node
        self._head.next = node

    def _unlink(self, node: _Node) -> None:
        # Connect the two neighbors to each other, skipping over node.
        # Sentinels guarantee both neighbors exist.
        node.prev.next = node.next
        node.next.prev = node.prev
        node.prev = None
        node.next = None

    def _move_to_front(self, node: _Node) -> None:
        self._unlink(node)
        self._add_to_front(node)

    def _evict_least_recent(self) -> None:
        least_recent = self._tail.prev
        self._unlink(least_recent)
        # The node stores its own key so we can also remove it from the dict.
        del self._nodes[least_recent.key]


def main() -> None:
    cache = LRUCache(capacity=3)
    cache.put("a", "alpha")
    cache.put("b", "beta")
    cache.put("c", "gamma")
    print(f"After inserting a, b, c: {cache.keys_most_recent_first()}")  # c, b, a

    # Reading "a" makes it the most recently used.
    cache.get("a")
    print(f"After get(a):            {cache.keys_most_recent_first()}")  # a, c, b

    # The cache is full, so inserting "d" evicts the least recently used: "b".
    cache.put("d", "delta")
    print(f"After inserting d:       {cache.keys_most_recent_first()}")  # d, a, c
    print(f"get(b): {cache.get('b')}")  # None

    print()
    print("Key lesson: 'b' was evicted, not 'a', even though 'a' was inserted first.")
    print("Eviction follows the last use, not the insertion time. That is LRU, not FIFO.")


if __name__ == "__main__":
    main()
