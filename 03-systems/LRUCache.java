import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU (Least Recently Used) cache with a fixed capacity.
 *
 * When the cache is full and a new entry arrives, the entry that was accessed
 * least recently is evicted to make room. This is the standard eviction policy
 * used when you want to keep "hot" data in memory and discard stale entries.
 *
 * Implementation: LinkedHashMap with accessOrder=true.
 *   - accessOrder=true means get() and put() move the accessed entry to the tail.
 *   - The eldest entry (head of the list) is the least recently used one.
 *   - removeEldestEntry() is called automatically after each put(); returning true
 *     triggers eviction.
 *
 * All operations are O(1): HashMap gives O(1) lookup, doubly-linked list gives
 * O(1) reordering of the accessed node.
 *
 * AI engineering connection — KV caching in LLM inference:
 *   Transformer models compute key-value attention tensors for each token in the
 *   prompt. When the same prefix appears across multiple requests (e.g. a system
 *   prompt used by all users), recomputing those tensors every time is expensive.
 *   Inference servers cache these KV tensors keyed by prefix hash, and evict them
 *   with LRU when GPU memory is under pressure. The same eviction logic that works
 *   for a Java HashMap here works at the GPU tensor level in vLLM, TRT-LLM, etc.
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;

    public LRUCache(int maxSize) {
        // initialCapacity=16, loadFactor=0.75, accessOrder=true
        super(16, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

    public static void main(String[] args) {
        LRUCache<String, String> cache = new LRUCache<>(3);

        cache.put("a", "alpha");
        cache.put("b", "beta");
        cache.put("c", "gamma");
        System.out.println("After inserting a, b, c: " + cache.keySet());
        // order (LRU → MRU): a, b, c

        // Access "a" — it moves to most-recently-used position.
        cache.get("a");
        System.out.println("After get(a):            " + cache.keySet());
        // order (LRU → MRU): b, c, a

        // Insert "d" — cache is full, so the LRU entry ("b") is evicted.
        cache.put("d", "delta");
        System.out.println("After inserting d:       " + cache.keySet());
        // "b" should be gone; "a" was recently used so it survives
        System.out.println("b evicted: " + !cache.containsKey("b"));
        System.out.println("a still present: " + cache.containsKey("a"));

        System.out.println();
        System.out.println("Key lesson: the entry that was accessed least recently");
        System.out.println("(\"b\") was evicted, not the oldest by insertion time (\"a\").");
        System.out.println("This is what makes it LRU rather than FIFO.");
    }
}
