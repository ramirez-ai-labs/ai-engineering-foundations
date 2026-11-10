import java.util.function.LongSupplier;

/**
 * Test clock: stands in for System.nanoTime(), and time moves only when a test says so.
 *
 * The time-based classes accept a LongSupplier, so tests pass one of these and
 * call advanceMillis() instead of sleeping. Tests stay fast and never flake.
 */
final class FakeClock implements LongSupplier {

    private long nanos = 1_000_000_000L;

    @Override
    public long getAsLong() {
        return nanos;
    }

    void advanceMillis(long millis) {
        nanos += millis * 1_000_000L;
    }
}
