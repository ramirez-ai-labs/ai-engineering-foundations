/** Tests for RateLimiter. Run: javac *.java && java RateLimiterTest */
public class RateLimiterTest {

    static int countAllowed(RateLimiter limiter, int attempts) {
        int allowed = 0;
        for (int i = 0; i < attempts; i++) {
            if (limiter.allow()) {
                allowed++;
            }
        }
        return allowed;
    }

    static void run() throws InterruptedException {
        Check.throwsIllegalArgument(() -> new RateLimiter(0, 1000), "rejects zero maxRequests");
        Check.throwsIllegalArgument(() -> new RateLimiter(-1, 1000), "rejects negative maxRequests");
        Check.throwsIllegalArgument(() -> new RateLimiter(1, 0), "rejects zero window");

        FakeClock clock = new FakeClock();
        RateLimiter limiter = new RateLimiter(3, 10_000, clock);
        Check.equal(3, countAllowed(limiter, 5), "allows up to the limit then blocks");
        clock.advanceMillis(9_999);
        Check.isFalse(limiter.allow(), "still blocked later in the same window");
        clock.advanceMillis(1);
        Check.equal(3, countAllowed(limiter, 5), "new window resets the count");

        // Documents the known fixed-window weakness, so a fix shows up as a
        // deliberate test change. Wait until just before the window ends...
        clock = new FakeClock();
        limiter = new RateLimiter(3, 10_000, clock);
        clock.advanceMillis(9_900);
        int firstBurst = countAllowed(limiter, 3);
        // ...then a moment later a new window opens.
        clock.advanceMillis(200);
        int secondBurst = countAllowed(limiter, 3);
        Check.equal(6, firstBurst + secondBurst, "boundary burst allows twice the limit in 0.2s");

        // A long real window, so every thread lands in the same window.
        RateLimiter shared = new RateLimiter(10, 60_000);
        Check.equal(10, ConcurrencyHelper.countAllowedAcrossThreads(shared::allow, 8, 20),
                "threads never exceed the limit");
    }

    public static void main(String[] args) throws InterruptedException {
        run();
        Check.finish();
    }
}
