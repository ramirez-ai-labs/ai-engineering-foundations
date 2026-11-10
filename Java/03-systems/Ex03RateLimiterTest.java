/** Tests for Ex03RateLimiter. Run: javac *.java && java Ex03RateLimiterTest */
public class Ex03RateLimiterTest {

    static int countAllowed(Ex03RateLimiter limiter, int attempts) {
        int allowed = 0;
        for (int i = 0; i < attempts; i++) {
            if (limiter.allow()) {
                allowed++;
            }
        }
        return allowed;
    }

    static void run() throws InterruptedException {
        SystemsCheck.throwsIllegalArgument(() -> new Ex03RateLimiter(0, 1000), "rejects zero maxRequests");
        SystemsCheck.throwsIllegalArgument(() -> new Ex03RateLimiter(-1, 1000), "rejects negative maxRequests");
        SystemsCheck.throwsIllegalArgument(() -> new Ex03RateLimiter(1, 0), "rejects zero window");

        // Regression: windowMs * 1_000_000 used to overflow to a negative window,
        // so every request started a new window and the limiter allowed all 100.
        Ex03RateLimiter hugeWindow = new Ex03RateLimiter(2, Long.MAX_VALUE / 1000, new FakeClock());
        SystemsCheck.equal(2, countAllowed(hugeWindow, 100), "huge window still enforces the limit (fails closed)");
        Ex03RateLimiter maxWindow = new Ex03RateLimiter(2, Long.MAX_VALUE, new FakeClock());
        SystemsCheck.equal(2, countAllowed(maxWindow, 100), "Long.MAX_VALUE window still enforces the limit");

        FakeClock clock = new FakeClock();
        Ex03RateLimiter limiter = new Ex03RateLimiter(3, 10_000, clock);
        SystemsCheck.equal(3, countAllowed(limiter, 5), "allows up to the limit then blocks");
        clock.advanceMillis(9_999);
        SystemsCheck.isFalse(limiter.allow(), "still blocked later in the same window");
        clock.advanceMillis(1);
        SystemsCheck.equal(3, countAllowed(limiter, 5), "new window resets the count");

        // Documents the known fixed-window weakness, so a fix shows up as a
        // deliberate test change. Wait until just before the window ends...
        clock = new FakeClock();
        limiter = new Ex03RateLimiter(3, 10_000, clock);
        clock.advanceMillis(9_900);
        int firstBurst = countAllowed(limiter, 3);
        // ...then a moment later a new window opens.
        clock.advanceMillis(200);
        int secondBurst = countAllowed(limiter, 3);
        SystemsCheck.equal(6, firstBurst + secondBurst, "boundary burst allows twice the limit in 0.2s");

        // A long real window, so every thread lands in the same window.
        Ex03RateLimiter shared = new Ex03RateLimiter(10, 60_000);
        SystemsCheck.equal(10, ConcurrencyHelper.countAllowedAcrossThreads(shared::allow, 8, 20),
                "threads never exceed the limit");
    }

    public static void main(String[] args) throws InterruptedException {
        run();
        SystemsCheck.finish();
    }
}
