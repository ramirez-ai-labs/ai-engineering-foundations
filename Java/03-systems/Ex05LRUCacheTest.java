import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Tests for Ex05LRUCache. Run: javac *.java && java Ex05LRUCacheTest */
public class Ex05LRUCacheTest {

    static Ex05LRUCache<String, String> filled(int capacity, String... keys) {
        Ex05LRUCache<String, String> cache = new Ex05LRUCache<>(capacity);
        for (String key : keys) {
            cache.put(key, key.toUpperCase());
        }
        return cache;
    }

    static void run() {
        SystemsCheck.throwsIllegalArgument(() -> new Ex05LRUCache<String, String>(0), "rejects zero capacity");
        SystemsCheck.throwsIllegalArgument(() -> new Ex05LRUCache<String, String>(-1), "rejects negative capacity");

        // Basic operations
        Ex05LRUCache<String, String> cache = new Ex05LRUCache<>(2);
        SystemsCheck.isNull(cache.get("missing"), "get missing returns null");
        SystemsCheck.equal(List.of(), cache.keysMostRecentFirst(), "empty cache has no keys");
        cache.put("a", "old");
        cache.put("a", "new");
        SystemsCheck.equal("new", cache.get("a"), "put existing key updates value");
        SystemsCheck.equal(1, cache.size(), "update keeps one entry");

        // Eviction
        cache = filled(3, "a", "b", "c", "d");
        SystemsCheck.isNull(cache.get("a"), "evicts the oldest key when none were read");
        SystemsCheck.equal(List.of("d", "c", "b"), cache.keysMostRecentFirst(), "recency order after eviction");

        cache = filled(3, "a", "b", "c");
        cache.get("a");
        cache.put("d", "D");
        SystemsCheck.isNull(cache.get("b"), "get protects a key: b is evicted instead of a");
        SystemsCheck.equal("A", cache.get("a"), "recently read key survives");

        cache = filled(3, "a", "b", "c");
        cache.put("a", "A2");
        SystemsCheck.equal(3, cache.size(), "update does not evict");
        cache.put("d", "D");
        SystemsCheck.isNull(cache.get("b"), "update counts as a use");
        SystemsCheck.equal("A2", cache.get("a"), "updated key survives");

        cache = filled(2, "a", "b");
        cache.get("zzz");
        SystemsCheck.equal(List.of("b", "a"), cache.keysMostRecentFirst(), "a missing get does not change order");

        Ex05LRUCache<String, String> bounded = new Ex05LRUCache<>(3);
        boolean neverOver = true;
        for (int i = 0; i < 100; i++) {
            bounded.put(String.valueOf(i), "v");
            if (bounded.size() > 3) {
                neverOver = false;
            }
        }
        SystemsCheck.isTrue(neverOver, "size never exceeds capacity");
        SystemsCheck.equal(List.of("99", "98", "97"), bounded.keysMostRecentFirst(), "keeps the most recent keys");

        cache = filled(1, "a", "b");
        SystemsCheck.isNull(cache.get("a"), "capacity one evicts on every new key");
        SystemsCheck.equal("B", cache.get("b"), "capacity one keeps the newest key");

        cache = filled(2, "a", "b", "c");
        cache.put("a", "back");
        SystemsCheck.equal("back", cache.get("a"), "an evicted key can be added again");
        SystemsCheck.equal(List.of("a", "c"), cache.keysMostRecentFirst(), "order after re-adding");

        // A broken prev pointer would pass every forward-only check above.
        cache = filled(4, "a", "b", "c", "d", "e");
        cache.get("c");
        cache.put("b", "B2");
        List<String> forwards = new ArrayList<>(cache.keysMostRecentFirst());
        Collections.reverse(forwards);
        SystemsCheck.equal(forwards, cache.keysLeastRecentFirst(), "walking backwards matches walking forwards");
    }

    public static void main(String[] args) {
        run();
        SystemsCheck.finish();
    }
}
