import java.util.function.LongSupplier;

/**
 * Simple rate limiter - fixed window: allow at most N requests per window.
 *
 * In AI systems: limit calls to LLM APIs, embedding APIs, or your own service
 * to avoid throttling and control cost. Production systems often use token bucket
 * (see Ex04TokenBucket.java) or sliding window; this is the basic idea.
 *
 * Known fixed-window weakness: a client can send up to 2N requests across a
 * window boundary (N at the end of one window, N at the start of the next).
 *
 * Thread safety: "check the window, maybe reset, increment" must happen as ONE
 * step, so allow() is synchronized. An earlier version used two AtomicLongs
 * (window start and count). Each was atomic on its own, but swapping the window
 * start and zeroing the count were separate steps. A request admitted by another
 * thread in between was erased by the reset, so the new window could admit more
 * than N. The gap is a few instructions wide, so stress tests rarely catch it,
 * which is exactly what makes this class of bug dangerous. Atomics are enough
 * only when the shared state fits in a single variable.
 *
 * Clock choice: System.nanoTime() is monotonic. With System.currentTimeMillis(),
 * a system clock stepped backward (NTP correction, VM resume) makes
 * (now - windowStart) negative, so the window never resets and every request is
 * rejected until the clock catches up.
 */
public class Ex03RateLimiter {

    private final int maxRequests;
    private final long windowNanos;
    private final LongSupplier nanoClock;
    private long windowStart;
    private int count = 0;

    /** Allow at most maxRequests per windowMs milliseconds. */
    public Ex03RateLimiter(int maxRequests, long windowMs) {
        this(maxRequests, windowMs, System::nanoTime);
    }

    /** Same as above, reading time from nanoClock (used by tests). */
    Ex03RateLimiter(int maxRequests, long windowMs, LongSupplier nanoClock) {
        if (maxRequests <= 0 || windowMs <= 0) {
            throw new IllegalArgumentException("maxRequests and windowMs must be positive");
        }
        this.maxRequests = maxRequests;
        this.windowNanos = windowMs * 1_000_000L;
        this.nanoClock = nanoClock;
        // Monotonic time, so a system clock change cannot reset or stall a window.
        this.windowStart = nanoClock.getAsLong();
    }

    /** Returns true if request is allowed, false if rate limited. */
    public synchronized boolean allow() {
        long now = nanoClock.getAsLong();
        if (now - windowStart >= windowNanos) {
            // New window.
            windowStart = now;
            count = 0;
        }
        if (count < maxRequests) {
            count++;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        // Allow 2 requests per 1 second window.
        Ex03RateLimiter limiter = new Ex03RateLimiter(2, 1000);
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
