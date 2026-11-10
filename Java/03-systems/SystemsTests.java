/** Runs every test in this folder. Run: javac *.java && java SystemsTests */
public class SystemsTests {
    public static void main(String[] args) throws InterruptedException {
        Ex02InMemoryCacheTest.run();
        Ex03RateLimiterTest.run();
        Ex04TokenBucketTest.run();
        Ex05LRUCacheTest.run();
        SystemsCheck.finish();
    }
}
