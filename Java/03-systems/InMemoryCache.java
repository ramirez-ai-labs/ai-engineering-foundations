import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory cache - key/value store with optional TTL (time-to-live).
 *
 * In AI systems: cache embeddings, model outputs, or API responses to avoid
 * repeated computation or network calls. In production you'd use Redis/Memcached;
 * this illustrates the core idea.
 *
 * This demo uses lazy expiration:
 * - expired entries are removed when you call get()
 * - size() also cleans up expired entries before reporting
 */
public class InMemoryCache<K, V> {

    private final Map<K, V> store = new HashMap<>();
    private final Map<K, Long> expiry = new HashMap<>();
    private final long ttlMs; // 0 = no expiry

    public InMemoryCache(long ttlMs) {
        this.ttlMs = ttlMs;
    }

    public void put(K key, V value) {
        store.put(key, value);
        if (ttlMs > 0) {
            expiry.put(key, System.currentTimeMillis() + ttlMs);
        } else {
            expiry.remove(key);
        }
    }

    public V get(K key) {
        V value = store.get(key);
        if (value == null) return null;
        if (ttlMs > 0) {
            Long exp = expiry.get(key);
            if (exp != null && System.currentTimeMillis() > exp) {
                store.remove(key);
                expiry.remove(key);
                return null;
            }
        }
        return value;
    }

    public void remove(K key) {
        store.remove(key);
        expiry.remove(key);
    }

    public int size() {
        removeExpiredEntries();
        return store.size();
    }

    private void removeExpiredEntries() {
        if (ttlMs <= 0) {
            return;
        }

        long now = System.currentTimeMillis();
        for (Map.Entry<K, Long> entry : new HashMap<>(expiry).entrySet()) {
            if (now > entry.getValue()) {
                K key = entry.getKey();
                store.remove(key);
                expiry.remove(key);
            }
        }
    }

    public static void main(String[] args) {
        // Cache with 2-second TTL.
        InMemoryCache<String, String> cache = new InMemoryCache<>(2000);
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        System.out.println("get(user:1): " + cache.get("user:1"));
        System.out.println("get(missing): " + cache.get("missing"));
        System.out.println("size: " + cache.size());

        // Sleep long enough for both entries to expire.
        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sleep interrupted while waiting for TTL expiration.");
        }

        System.out.println("after TTL, get(user:2): " + cache.get("user:2"));
        System.out.println("size after TTL cleanup: " + cache.size());

        cache.remove("user:1");
        System.out.println("after remove, get(user:1): " + cache.get("user:1"));
    }
}
