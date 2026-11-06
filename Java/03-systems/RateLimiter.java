import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple rate limiter - fixed window: allow at most N requests per window.
 *
 * In AI systems: limit calls to LLM APIs, embedding APIs, or your own service
 * to avoid throttling and control cost. Production systems often use token bucket
 * or sliding window; this is the basic idea.
 */
public class RateLimiter {

    private final int maxRequests;
    private final long windowMs;
    private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());
    private final AtomicLong count = new AtomicLong(0);

    /** Allow at most maxRequests per windowMs milliseconds. */
    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
    }

    /** Returns true if request is allowed, false if rate limited. */
    public boolean allow() {
        long now = System.currentTimeMillis();
        long start = windowStart.get();
        if (now - start >= windowMs) {
            // New window
            if (windowStart.compareAndSet(start, now)) {
                count.set(0);
            }
        }
        long c = count.incrementAndGet();
        return c <= maxRequests;
    }

    public static void main(String[] args) {
        // Allow 2 requests per 1 second window.
        RateLimiter limiter = new RateLimiter(2, 1000);
        System.out.println("Request 1: " + limiter.allow()); // true
        System.out.println("Request 2: " + limiter.allow()); // true
        System.out.println("Request 3: " + limiter.allow()); // false (rate limited)

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sleep interrupted while waiting for the next window.");
        }

        System.out.println("Request 4 after new window: " + limiter.allow()); // true
    }
}
