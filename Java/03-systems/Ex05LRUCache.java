import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LRU (Least Recently Used) cache built from scratch: a HashMap plus a doubly linked list.
 *
 * When the cache is full and a new key arrives, the key that was used least
 * recently is evicted. Both get() and put() are O(1):
 * - the map goes from key to list node, so finding an entry is O(1)
 * - the doubly linked list keeps entries in recency order, and because each node
 *   knows both neighbors, unlinking it and moving it to the front is O(1)
 *
 * Neither structure is enough alone. A map cannot tell you which key is oldest
 * without scanning, and a linked list cannot find a key without walking it.
 *
 * Sentinel nodes: the list always contains two empty marker nodes, head and tail.
 * Real entries live between them, so every real node has a non-null prev and
 * next, and insert/unlink never need special cases for an empty list or the ends.
 *
 *   head <-> most recent <-> ... <-> least recent <-> tail
 *
 * AI engineering connection: inference servers cache attention key/value tensors
 * for repeated prompt prefixes (such as a shared system prompt) and evict them
 * with LRU when GPU memory runs low. vLLM and TRT-LLM apply the same policy at
 * the GPU memory level. Embedding and response caches use it to keep hot entries
 * in memory.
 *
 * Java shortcut: LinkedHashMap already maintains this linked list internally.
 *
 *   new LinkedHashMap<K, V>(16, 0.75f, true) {   // true = access order
 *       protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
 *           return size() > capacity;
 *       }
 *   };
 *
 * Use that in production code. Interviews usually ask for the version below.
 *
 * Limitations: not thread-safe, no TTL. Combining this with Ex02InMemoryCache.java's
 * lazy expiry is a good next exercise.
 */
public class Ex05LRUCache<K, V> {

    /** One cache entry, linked to its neighbors in recency order. */
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node<K, V>> nodes = new HashMap<>();
    // Sentinels hold no data. head.next is the most recently used entry and
    // tail.prev is the least recently used one.
    private final Node<K, V> head = new Node<>(null, null);
    private final Node<K, V> tail = new Node<>(null, null);

    public Ex05LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    /** Return the value for key, or null if missing. A hit counts as a use. */
    public V get(K key) {
        Node<K, V> node = nodes.get(key);
        if (node == null) {
            return null;
        }
        moveToFront(node);
        return node.value;
    }

    /** Insert or update key, evicting the least recently used key if full. */
    public void put(K key, V value) {
        Node<K, V> existing = nodes.get(key);
        if (existing != null) {
            // Updating an existing key never evicts, but it does count as a use.
            existing.value = value;
            moveToFront(existing);
            return;
        }

        if (nodes.size() == capacity) {
            evictLeastRecent();
        }

        Node<K, V> node = new Node<>(key, value);
        nodes.put(key, node);
        addToFront(node);
    }

    public int size() {
        return nodes.size();
    }

    /** Keys in recency order, for inspecting the cache. Does not count as a use. */
    public List<K> keysMostRecentFirst() {
        List<K> keys = new ArrayList<>();
        Node<K, V> curr = head.next;
        while (curr != tail) {
            keys.add(curr.key);
            curr = curr.next;
        }
        return keys;
    }

    /** Keys walked from the tail via prev pointers. Tests use it to check both directions. */
    List<K> keysLeastRecentFirst() {
        List<K> keys = new ArrayList<>();
        Node<K, V> curr = tail.prev;
        while (curr != head) {
            keys.add(curr.key);
            curr = curr.prev;
        }
        return keys;
    }

    private void addToFront(Node<K, V> node) {
        // Splice node in between head and the current first entry.
        Node<K, V> first = head.next;
        node.prev = head;
        node.next = first;
        first.prev = node;
        head.next = node;
    }

    private void unlink(Node<K, V> node) {
        // Connect the two neighbors to each other, skipping over node.
        // Sentinels guarantee both neighbors exist.
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
    }

    private void moveToFront(Node<K, V> node) {
        unlink(node);
        addToFront(node);
    }

    private void evictLeastRecent() {
        Node<K, V> leastRecent = tail.prev;
        unlink(leastRecent);
        // The node stores its own key so we can also remove it from the map.
        nodes.remove(leastRecent.key);
    }

    public static void main(String[] args) {
        Ex05LRUCache<String, String> cache = new Ex05LRUCache<>(3);
        cache.put("a", "alpha");
        cache.put("b", "beta");
        cache.put("c", "gamma");
        System.out.println("After inserting a, b, c: " + cache.keysMostRecentFirst()); // c, b, a

        // Reading "a" makes it the most recently used.
        cache.get("a");
        System.out.println("After get(a):            " + cache.keysMostRecentFirst()); // a, c, b

        // The cache is full, so inserting "d" evicts the least recently used: "b".
        cache.put("d", "delta");
        System.out.println("After inserting d:       " + cache.keysMostRecentFirst()); // d, a, c
        System.out.println("get(b): " + cache.get("b")); // null

        System.out.println();
        System.out.println("Key lesson: \"b\" was evicted, not \"a\", even though \"a\" was inserted first.");
        System.out.println("Eviction follows the last use, not the insertion time. That is LRU, not FIFO.");
    }
}
