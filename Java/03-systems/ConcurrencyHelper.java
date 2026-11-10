import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

/** Test helper: call a limiter from many threads at once and count how many calls were allowed. */
final class ConcurrencyHelper {

    private ConcurrencyHelper() {
    }

    static int countAllowedAcrossThreads(BooleanSupplier allow, int threads, int callsPerThread)
            throws InterruptedException {
        AtomicInteger allowed = new AtomicInteger();
        // The latch releases every thread at the same moment, to maximize contention.
        CountDownLatch start = new CountDownLatch(1);
        List<Thread> workers = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            Thread worker = new Thread(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                for (int i = 0; i < callsPerThread; i++) {
                    if (allow.getAsBoolean()) {
                        allowed.incrementAndGet();
                    }
                }
            });
            workers.add(worker);
            worker.start();
        }
        start.countDown();
        for (Thread worker : workers) {
            worker.join();
        }
        return allowed.get();
    }
}
