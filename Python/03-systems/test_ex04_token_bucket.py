"""Tests for ex04_token_bucket. Run from this folder: python -m unittest -v

Refill tests replace time.monotonic with a fake clock so they run instantly.
"""

import threading
import unittest
from unittest.mock import patch

from ex04_token_bucket import TokenBucket


class FakeClock:
    """Stands in for time.monotonic(); time moves only when a test says so."""

    def __init__(self) -> None:
        self.now = 1000.0

    def __call__(self) -> float:
        return self.now

    def advance(self, seconds: float) -> None:
        self.now += seconds


def count_allowed(bucket: TokenBucket, attempts: int) -> int:
    allowed = 0
    for attempt in range(attempts):
        if bucket.allow():
            allowed += 1
    return allowed


class ValidationTest(unittest.TestCase):
    def test_rejects_non_positive_settings(self) -> None:
        for capacity, rate in [(0, 1.0), (-1, 1.0), (1, 0.0), (1, -2.0)]:
            with self.assertRaises(ValueError):
                TokenBucket(capacity, rate)

    def test_rejects_nan_and_infinite_settings(self) -> None:
        # NaN slips past "<= 0" because it fails every comparison; infinite
        # capacity would allow every request. Both must be rejected up front.
        nan = float("nan")
        inf = float("inf")
        for capacity, rate in [(nan, 1.0), (inf, 1.0), (1, nan), (1, inf)]:
            with self.assertRaises(ValueError):
                TokenBucket(capacity, rate)

    def test_rejects_non_positive_cost(self) -> None:
        bucket = TokenBucket(capacity=5, refill_per_second=1)
        with self.assertRaises(ValueError):
            bucket.allow(cost=0)

    def test_rejects_nan_cost(self) -> None:
        # Without the check, allow(nan) silently returns False forever.
        bucket = TokenBucket(capacity=5, refill_per_second=1)
        with self.assertRaises(ValueError):
            bucket.allow(cost=float("nan"))

    def test_rejects_cost_larger_than_capacity(self) -> None:
        # Such a request could never succeed, so it is an error, not a "no".
        bucket = TokenBucket(capacity=5, refill_per_second=1)
        with self.assertRaises(ValueError):
            bucket.allow(cost=6)


class RefillTest(unittest.TestCase):
    def setUp(self) -> None:
        self.clock = FakeClock()
        patcher = patch("time.monotonic", self.clock)
        patcher.start()
        self.addCleanup(patcher.stop)
        # Bursts of 4, refilling 2 tokens per second.
        self.bucket = TokenBucket(capacity=4, refill_per_second=2)

    def test_starts_full_so_a_burst_is_allowed(self) -> None:
        self.assertEqual(count_allowed(self.bucket, 10), 4)

    def test_partial_refill(self) -> None:
        count_allowed(self.bucket, 4)
        # 1.5 seconds at 2 per second = 3 tokens.
        self.clock.advance(1.5)
        self.assertEqual(count_allowed(self.bucket, 10), 3)

    def test_fractional_tokens_accumulate(self) -> None:
        count_allowed(self.bucket, 4)
        self.clock.advance(0.25)  # 0.5 tokens: not enough yet
        self.assertFalse(self.bucket.allow())
        self.clock.advance(0.25)  # 1.0 token in total
        self.assertTrue(self.bucket.allow())

    def test_long_idle_never_exceeds_capacity(self) -> None:
        count_allowed(self.bucket, 4)
        self.clock.advance(3600)
        self.assertEqual(count_allowed(self.bucket, 100), 4)

    def test_no_boundary_burst_unlike_fixed_window(self) -> None:
        # Spend the burst, then keep requesting: throughput is capped at the
        # refill rate, about 2 per second, with no window edge to exploit.
        count_allowed(self.bucket, 4)
        allowed = 0
        for tick in range(10):
            self.clock.advance(0.5)
            allowed += count_allowed(self.bucket, 5)
        # 5 seconds of refill at 2 per second.
        self.assertEqual(allowed, 10)

    def test_cost_spends_multiple_tokens(self) -> None:
        self.assertTrue(self.bucket.allow(cost=3))
        self.assertFalse(self.bucket.allow(cost=2))
        self.assertTrue(self.bucket.allow(cost=1))

    def test_rejected_request_spends_nothing(self) -> None:
        self.bucket.allow(cost=3)
        self.assertFalse(self.bucket.allow(cost=4))
        # The failed request did not take the remaining token.
        self.assertTrue(self.bucket.allow(cost=1))


class ConcurrencyTest(unittest.TestCase):
    def test_threads_never_spend_more_than_capacity(self) -> None:
        # A very slow refill, so effectively only the starting tokens exist.
        bucket = TokenBucket(capacity=10, refill_per_second=0.0001)
        results: list[bool] = []
        results_lock = threading.Lock()

        def worker() -> None:
            for attempt in range(20):
                outcome = bucket.allow()
                with results_lock:
                    results.append(outcome)

        threads: list[threading.Thread] = []
        for index in range(8):
            threads.append(threading.Thread(target=worker))
        for thread in threads:
            thread.start()
        for thread in threads:
            thread.join()

        self.assertEqual(results.count(True), 10)


if __name__ == "__main__":
    unittest.main()
