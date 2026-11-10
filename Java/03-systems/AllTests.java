/** Runs every test in this folder. Run: javac *.java && java AllTests */
public class AllTests {
    public static void main(String[] args) throws InterruptedException {
        InMemoryCacheTest.run();
        LRUCacheTest.run();
        RateLimiterTest.run();
        TokenBucketTest.run();
        Check.finish();
    }
}
