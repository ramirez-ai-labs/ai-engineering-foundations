/** Tests for TokenBucket. Run: javac *.java && java TokenBucketTest */
public class TokenBucketTest {

    static int countAllowed(TokenBucket bucket, int attempts) {
        int allowed = 0;
        for (int i = 0; i < attempts; i++) {
            if (bucket.allow()) {
                allowed++;
            }
        }
        return allowed;
    }

    static void run() throws InterruptedException {
        validation();
        refill();
        cost();
        concurrency();
    }

    static void validation() {
        Check.throwsIllegalArgument(() -> new TokenBucket(0, 1), "rejects zero capacity");
        Check.throwsIllegalArgument(() -> new TokenBucket(-1, 1), "rejects negative capacity");
        Check.throwsIllegalArgument(() -> new TokenBucket(1, 0), "rejects zero refill rate");
        TokenBucket bucket = new TokenBucket(5, 1);
        Check.throwsIllegalArgument(() -> bucket.allow(0), "rejects zero cost");
        // Such a request could never succeed, so it is an error, not a "no".
        Check.throwsIllegalArgument(() -> bucket.allow(6), "rejects cost larger than capacity");
    }

    static void refill() {
        // Bursts of 4, refilling 2 tokens per second.
        FakeClock clock = new FakeClock();
        TokenBucket bucket = new TokenBucket(4, 2, clock);
        Check.equal(4, countAllowed(bucket, 10), "starts full, so a burst is allowed");

        clock.advanceMillis(1_500); // 1.5 s at 2 per second = 3 tokens
        Check.equal(3, countAllowed(bucket, 10), "partial refill");

        clock.advanceMillis(250); // 0.5 tokens: not enough yet
        Check.isFalse(bucket.allow(), "half a token is not enough");
        clock.advanceMillis(250); // 1.0 token in total
        Check.isTrue(bucket.allow(), "fractional tokens accumulate");

        clock.advanceMillis(3_600_000);
        Check.equal(4, countAllowed(bucket, 100), "long idle never exceeds capacity");

        // Keep requesting after the burst: throughput is capped at the refill
        // rate, with no window edge to exploit.
        int allowed = 0;
        for (int tick = 0; tick < 10; tick++) {
            clock.advanceMillis(500);
            allowed += countAllowed(bucket, 5);
        }
        Check.equal(10, allowed, "no boundary burst: 5 s of refill at 2 per second");
    }

    static void cost() {
        TokenBucket bucket = new TokenBucket(4, 2, new FakeClock());
        Check.isTrue(bucket.allow(3), "cost spends multiple tokens");
        Check.isFalse(bucket.allow(2), "not enough tokens left for cost 2");
        // The failed request did not take the remaining token.
        Check.isTrue(bucket.allow(1), "a rejected request spends nothing");
    }

    static void concurrency() throws InterruptedException {
        // A very slow refill, so effectively only the starting tokens exist.
        TokenBucket bucket = new TokenBucket(10, 0.0001);
        Check.equal(10, ConcurrencyHelper.countAllowedAcrossThreads(bucket::allow, 8, 20),
                "threads never spend more than capacity");
    }

    public static void main(String[] args) throws InterruptedException {
        run();
        Check.finish();
    }
}
