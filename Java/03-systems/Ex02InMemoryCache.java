import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

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
 *
 * Clock choice: TTLs use System.nanoTime(), a monotonic clock, rather than
 * System.currentTimeMillis(), which is wall-clock time. If NTP or an operator
 * moves the system clock, wall-clock TTLs expire early or live far too long;
 * monotonic time only moves forward. nanoTime values are only meaningful as
 * differences, so expiry is checked with (now - expiresAt > 0), which also stays
 * correct if the counter overflows.
 *
 * The clock is a constructor argument so tests can move time by hand instead of
 * sleeping. Normal code uses the one-argument constructor.
 *
 * Limitations: it is not thread-safe, and it has no maximum size, so a real cache
 * would also need an eviction policy such as LRU (see Ex05LRUCache.java) to keep
 * memory bounded.
 */
public class Ex02InMemoryCache<K, V> {

    private final Map<K, V> store = new HashMap<>();
    // Two maps share the same keys: one for values, one for expiry times.
    private final Map<K, Long> expiresAtNanos = new HashMap<>();
    private final long ttlNanos; // 0 = no expiry
    private final LongSupplier nanoClock;

    /** ttlMs <= 0 means entries never expire. */
    public Ex02InMemoryCache(long ttlMs) {
        this(ttlMs, System::nanoTime);
    }

    /** Same as above, reading time from nanoClock (used by tests). */
    Ex02InMemoryCache(long ttlMs, LongSupplier nanoClock) {
        // toNanos saturates at Long.MAX_VALUE; ttlMs * 1_000_000 could overflow
        // to a negative value and silently turn a huge TTL into "never expires".
        this.ttlNanos = ttlMs > 0 ? TimeUnit.MILLISECONDS.toNanos(ttlMs) : 0;
        this.nanoClock = nanoClock;
    }

    public void put(K key, V value) {
        store.put(key, value);
        if (ttlNanos > 0) {
            expiresAtNanos.put(key, nanoClock.getAsLong() + ttlNanos);
        }
    }

    public V get(K key) {
        if (!store.containsKey(key)) {
            return null;
        }
        if (isExpired(key, nanoClock.getAsLong())) {
            remove(key);
            return null;
        }
        return store.get(key);
    }

    public void remove(K key) {
        store.remove(key);
        expiresAtNanos.remove(key);
    }

    public int size() {
        removeExpiredEntries();
        return store.size();
    }

    private boolean isExpired(K key, long now) {
        Long expiresAt = expiresAtNanos.get(key);
        // Keys with no expiry time never expire.
        if (expiresAt == null) {
            return false;
        }
        return now - expiresAt > 0;
    }

    private void removeExpiredEntries() {
        long now = nanoClock.getAsLong();
        // Copy the keys first: removing from a HashMap while iterating it throws
        // ConcurrentModificationException.
        for (K key : new HashMap<>(store).keySet()) {
            if (isExpired(key, now)) {
                remove(key);
            }
        }
    }

    public static void main(String[] args) {
        // Cache with 2-second TTL.
        Ex02InMemoryCache<String, String> cache = new Ex02InMemoryCache<>(2000);
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        System.out.println("get(user:1): " + cache.get("user:1"));
        System.out.println("get(missing): " + cache.get("missing"));
        System.out.println("size: " + cache.size());

        // Remove user:1 while it is still live, so this shows remove() working.
        cache.remove("user:1");
        System.out.println("after remove, get(user:1): " + cache.get("user:1"));
        System.out.println("size after remove: " + cache.size());

        // Sleep long enough for the remaining entry to expire.
        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sleep interrupted while waiting for TTL expiration.");
        }

        System.out.println("after TTL, get(user:2): " + cache.get("user:2"));
        System.out.println("size after TTL cleanup: " + cache.size());
    }
}
