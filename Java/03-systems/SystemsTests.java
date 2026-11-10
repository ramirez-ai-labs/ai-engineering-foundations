/** Runs every test in this folder. Run: javac *.java && java AllTests */
public class AllTests {
    public static void main(String[] args) throws InterruptedException {
        Ex02InMemoryCacheTest.run();
        Ex03RateLimiterTest.run();
        Ex04TokenBucketTest.run();
        Ex05LRUCacheTest.run();
        Check.finish();
    }
}
