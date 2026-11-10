/** Tests for Ex02InMemoryCache. Run: javac *.java && java Ex02InMemoryCacheTest */
public class Ex02InMemoryCacheTest {

    static void run() {
        basicOperations();
        ttl();
    }

    static void basicOperations() {
        Ex02InMemoryCache<String, String> cache = new Ex02InMemoryCache<>(0);
        cache.put("k", "v");
        SystemsCheck.equal("v", cache.get("k"), "put then get");
        SystemsCheck.isNull(cache.get("missing"), "missing key returns null");

        cache.put("k", "new");
        SystemsCheck.equal("new", cache.get("k"), "put overwrites");
        SystemsCheck.equal(1, cache.size(), "overwrite keeps one entry");

        cache.remove("k");
        SystemsCheck.isNull(cache.get("k"), "remove deletes the entry");
        SystemsCheck.equal(0, cache.size(), "size after remove");

        cache.remove("missing");
        SystemsCheck.equal(0, cache.size(), "removing a missing key is a no-op");
    }

    static void ttl() {
        FakeClock clock = new FakeClock();
        Ex02InMemoryCache<String, String> cache = new Ex02InMemoryCache<>(10_000, clock);
        cache.put("k", "v");
        clock.advanceMillis(9_999);
        SystemsCheck.equal("v", cache.get("k"), "entry is live before TTL");
        clock.advanceMillis(1);
        // The check is now - expiresAt > 0, so the boundary instant is still live.
        SystemsCheck.equal("v", cache.get("k"), "entry still live exactly at TTL");
        clock.advanceMillis(1);
        SystemsCheck.isNull(cache.get("k"), "entry expires after TTL");

        clock = new FakeClock();
        cache = new Ex02InMemoryCache<>(10_000, clock);
        cache.put("k", "v1");
        clock.advanceMillis(8_000);
        cache.put("k", "v2");
        clock.advanceMillis(8_000);
        SystemsCheck.equal("v2", cache.get("k"), "put again resets the TTL");

        clock = new FakeClock();
        cache = new Ex02InMemoryCache<>(10_000, clock);
        cache.put("old", "v");
        clock.advanceMillis(6_000);
        cache.put("new", "v");
        clock.advanceMillis(6_000);
        // "old" is 12s old (expired), "new" is 6s old (live).
        SystemsCheck.equal(1, cache.size(), "size ignores expired entries");
        SystemsCheck.equal("v", cache.get("new"), "live entry survives cleanup");

        clock = new FakeClock();
        cache = new Ex02InMemoryCache<>(0, clock);
        cache.put("k", "v");
        clock.advanceMillis(1_000_000_000L);
        SystemsCheck.equal("v", cache.get("k"), "zero TTL never expires");

        // Regression: ttlMs * 1_000_000 used to overflow for huge TTLs. With
        // saturation, a huge TTL behaves as "effectively forever", on purpose.
        clock = new FakeClock();
        cache = new Ex02InMemoryCache<>(Long.MAX_VALUE, clock);
        cache.put("k", "v");
        clock.advanceMillis(365L * 24 * 60 * 60 * 1000);
        SystemsCheck.equal("v", cache.get("k"), "huge TTL entry is still live a year later");
    }

    public static void main(String[] args) {
        run();
        SystemsCheck.finish();
    }
}
