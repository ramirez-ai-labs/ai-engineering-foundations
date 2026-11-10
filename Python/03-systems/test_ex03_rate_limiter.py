"""Tests for ex03_rate_limiter. Run from this folder: python -m unittest -v

Window tests replace time.monotonic with a fake clock so they run instantly.
"""

import threading
import unittest
from unittest.mock import patch

from ex03_rate_limiter import RateLimiter


class FakeClock:
    """Stands in for time.monotonic(); time moves only when a test says so."""

    def __init__(self) -> None:
        self.now = 1000.0

    def __call__(self) -> float:
        return self.now

    def advance(self, seconds: float) -> None:
        self.now += seconds


def count_allowed(limiter: RateLimiter, attempts: int) -> int:
    allowed = 0
    for attempt in range(attempts):
        if limiter.allow():
            allowed += 1
    return allowed


class ValidationTest(unittest.TestCase):
    def test_rejects_non_positive_settings(self) -> None:
        for max_requests, window in [(0, 1.0), (-1, 1.0), (1, 0.0), (1, -5.0)]:
            with self.assertRaises(ValueError):
                RateLimiter(max_requests, window)

    def test_rejects_nan_and_infinite_window(self) -> None:
        # A NaN window slips past "<= 0" and never resets, so the limiter would
        # block every request after the first N, forever.
        for window in [float("nan"), float("inf")]:
            with self.assertRaises(ValueError):
                RateLimiter(2, window)


class FixedWindowTest(unittest.TestCase):
    def setUp(self) -> None:
        self.clock = FakeClock()
        patcher = patch("time.monotonic", self.clock)
        patcher.start()
        self.addCleanup(patcher.stop)
        self.limiter = RateLimiter(max_requests=3, window_seconds=10)

    def test_allows_up_to_the_limit_then_blocks(self) -> None:
        self.assertEqual(count_allowed(self.limiter, 5), 3)

    def test_still_blocked_later_in_the_same_window(self) -> None:
        count_allowed(self.limiter, 3)
        self.clock.advance(9.9)
        self.assertFalse(self.limiter.allow())

    def test_new_window_resets_the_count(self) -> None:
        count_allowed(self.limiter, 3)
        self.clock.advance(10)
        self.assertEqual(count_allowed(self.limiter, 5), 3)

    def test_boundary_burst_allows_twice_the_limit(self) -> None:
        # Documents the known fixed-window weakness, so a fix shows up as a
        # deliberate test change. Wait until just before the window ends...
        self.clock.advance(9.9)
        first_burst = count_allowed(self.limiter, 3)
        # ...then a moment later a new window opens.
        self.clock.advance(0.2)
        second_burst = count_allowed(self.limiter, 3)
        # 6 requests got through in 0.2 seconds, with a limit of 3 per 10.
        self.assertEqual(first_burst + second_burst, 6)


class ConcurrencyTest(unittest.TestCase):
    def test_threads_never_exceed_the_limit(self) -> None:
        # A long real window, so every thread lands in the same window.
        limiter = RateLimiter(max_requests=10, window_seconds=60)
        results: list[bool] = []
        results_lock = threading.Lock()

        def worker() -> None:
            for attempt in range(20):
                outcome = limiter.allow()
                with results_lock:
                    results.append(outcome)

        threads: list[threading.Thread] = []
        for index in range(8):
            threads.append(threading.Thread(target=worker))
        for thread in threads:
            thread.start()
        for thread in threads:
            thread.join()

        self.assertEqual(len(results), 160)
        self.assertEqual(results.count(True), 10)


if __name__ == "__main__":
    unittest.main()
