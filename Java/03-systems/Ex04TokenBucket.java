import java.util.function.LongSupplier;

/**
 * Token bucket rate limiter - allows short bursts while enforcing an average rate.
 *
 * The bucket holds up to capacity tokens and refills at refillPerSecond.
 * Each request spends tokens; if there are not enough, the request is rejected.
 * - capacity sets the largest burst a client can send at once
 * - refillPerSecond sets the sustained rate
 *
 * Compared with the fixed window in Ex03RateLimiter.java, there is no window
 * boundary, so a client can never get 2N requests through by timing them around
 * one. The bucket simply never holds more than capacity tokens.
 *
 * Refill is lazy: instead of a background timer adding tokens, each call works
 * out how many tokens accumulated since the last call. That keeps it O(1) per
 * request with no threads to manage.
 *
 * In AI systems: LLM providers rate-limit by requests AND by tokens per minute.
 * The cost argument models the second case, where a large prompt spends more of
 * the budget than a small one.
 *
 * Limitations: one process only. A shared limit across servers keeps the bucket
 * state in a store such as Redis and updates it atomically (e.g. a Lua script).
 */
public class Ex04TokenBucket {

    private static final double NANOS_PER_SECOND = 1_000_000_000.0;

    private final double capacity;
    private final double refillPerSecond;
    private final LongSupplier nanoClock;
    private double tokens;
    private long lastRefill;

    public Ex04TokenBucket(double capacity, double refillPerSecond) {
        this(capacity, refillPerSecond, System::nanoTime);
    }

    /** Same as above, reading time from nanoClock (used by tests). */
    Ex04TokenBucket(double capacity, double refillPerSecond, LongSupplier nanoClock) {
        if (!isPositiveFinite(capacity) || !isPositiveFinite(refillPerSecond)) {
            throw new IllegalArgumentException("capacity and refillPerSecond must be positive finite numbers");
        }
        this.capacity = capacity;
        this.refillPerSecond = refillPerSecond;
        this.nanoClock = nanoClock;
        // Start full, so a new client can burst immediately.
        this.tokens = capacity;
        this.lastRefill = nanoClock.getAsLong();
    }

    /** Spend one token if available. */
    public boolean allow() {
        return allow(1.0);
    }

    /** Spend cost tokens if available. Returns true if the request is allowed. */
    public boolean allow(double cost) {
        if (!isPositiveFinite(cost)) {
            throw new IllegalArgumentException("cost must be a positive finite number");
        }
        if (cost > capacity) {
            // The bucket can never hold this many tokens, so the request could
            // never succeed. Fail loudly instead of rejecting it forever.
            throw new IllegalArgumentException("cost exceeds bucket capacity");
        }

        // Refill, check, and spend must happen as one step, or two threads can
        // both see enough tokens and both spend them.
        synchronized (this) {
            refill(nanoClock.getAsLong());
            if (tokens >= cost) {
                tokens -= cost;
                return true;
            }
            return false;
        }
    }

    /**
     * "value <= 0" alone is not enough: NaN fails every comparison, so it would
     * slip through, and infinity passes "> 0" but makes the limit meaningless
     * (infinite capacity allows every request).
     */
    private static boolean isPositiveFinite(double value) {
        return Double.isFinite(value) && value > 0;
    }

    private void refill(long now) {
        double elapsedSeconds = (now - lastRefill) / NANOS_PER_SECOND;
        // Tokens beyond capacity are discarded: idle time cannot be saved up
        // into an unlimited burst.
        tokens = Math.min(capacity, tokens + elapsedSeconds * refillPerSecond);
        lastRefill = now;
    }

    public static void main(String[] args) throws InterruptedException {
        // Bursts of up to 3 requests, refilling 2 tokens per second on average.
        Ex04TokenBucket bucket = new Ex04TokenBucket(3, 2);
        for (int request = 1; request <= 4; request++) {
            System.out.println("Request " + request + ": " + bucket.allow()); // true x3, then false
        }

        // Half a second at 2 tokens per second refills one token. Sleep a little
        // longer, because sleep and clock resolution are not exact.
        Thread.sleep(600);
        System.out.println("Request 5 after 0.6s: " + bucket.allow()); // true
        System.out.println("Request 6: " + bucket.allow()); // false

        // A request can cost more than one token, like a long prompt.
        Thread.sleep(1500);
        System.out.println("Request costing 3 tokens after 1.5s: " + bucket.allow(3)); // true
    }
}
