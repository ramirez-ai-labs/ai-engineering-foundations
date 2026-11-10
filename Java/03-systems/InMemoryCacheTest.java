/** Tests for InMemoryCache. Run: javac *.java && java InMemoryCacheTest */
public class InMemoryCacheTest {

    static void run() {
        basicOperations();
        ttl();
    }

    static void basicOperations() {
        InMemoryCache<String, String> cache = new InMemoryCache<>(0);
        cache.put("k", "v");
        Check.equal("v", cache.get("k"), "put then get");
        Check.isNull(cache.get("missing"), "missing key returns null");

        cache.put("k", "new");
        Check.equal("new", cache.get("k"), "put overwrites");
        Check.equal(1, cache.size(), "overwrite keeps one entry");

        cache.remove("k");
        Check.isNull(cache.get("k"), "remove deletes the entry");
        Check.equal(0, cache.size(), "size after remove");

        cache.remove("missing");
        Check.equal(0, cache.size(), "removing a missing key is a no-op");
    }

    static void ttl() {
        FakeClock clock = new FakeClock();
        InMemoryCache<String, String> cache = new InMemoryCache<>(10_000, clock);
        cache.put("k", "v");
        clock.advanceMillis(9_999);
        Check.equal("v", cache.get("k"), "entry is live before TTL");
        clock.advanceMillis(1);
        // The check is now - expiresAt > 0, so the boundary instant is still live.
        Check.equal("v", cache.get("k"), "entry still live exactly at TTL");
        clock.advanceMillis(1);
        Check.isNull(cache.get("k"), "entry expires after TTL");

        clock = new FakeClock();
        cache = new InMemoryCache<>(10_000, clock);
        cache.put("k", "v1");
        clock.advanceMillis(8_000);
        cache.put("k", "v2");
        clock.advanceMillis(8_000);
        Check.equal("v2", cache.get("k"), "put again resets the TTL");

        clock = new FakeClock();
        cache = new InMemoryCache<>(10_000, clock);
        cache.put("old", "v");
        clock.advanceMillis(6_000);
        cache.put("new", "v");
        clock.advanceMillis(6_000);
        // "old" is 12s old (expired), "new" is 6s old (live).
        Check.equal(1, cache.size(), "size ignores expired entries");
        Check.equal("v", cache.get("new"), "live entry survives cleanup");

        clock = new FakeClock();
        cache = new InMemoryCache<>(0, clock);
        cache.put("k", "v");
        clock.advanceMillis(1_000_000_000L);
        Check.equal("v", cache.get("k"), "zero TTL never expires");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
