"""Tests for ex02_in_memory_cache. Run from this folder: python -m unittest -v

TTL tests replace time.monotonic with a fake clock, so they run instantly and
never flake. Sleeping in tests makes them slow and timing-dependent.
"""

import unittest
from unittest.mock import patch

from ex02_in_memory_cache import InMemoryCache


class FakeClock:
    """Stands in for time.monotonic(); time moves only when a test says so."""

    def __init__(self) -> None:
        self.now = 1000.0

    def __call__(self) -> float:
        return self.now

    def advance(self, seconds: float) -> None:
        self.now += seconds


class ValidationTest(unittest.TestCase):
    def test_rejects_nan_and_infinite_ttl(self) -> None:
        # A NaN TTL would silently mean "never expire" and serve stale data.
        for ttl in [float("nan"), float("inf")]:
            with self.assertRaises(ValueError):
                InMemoryCache(ttl_seconds=ttl)

    def test_zero_and_negative_ttl_still_mean_no_expiry(self) -> None:
        # The documented way to turn expiry off keeps working.
        InMemoryCache(ttl_seconds=0)
        InMemoryCache(ttl_seconds=-1)


class BasicOperationsTest(unittest.TestCase):
    def test_put_then_get(self) -> None:
        cache = InMemoryCache()
        cache.put("k", "v")
        self.assertEqual(cache.get("k"), "v")

    def test_missing_key_returns_none(self) -> None:
        self.assertIsNone(InMemoryCache().get("missing"))

    def test_put_overwrites(self) -> None:
        cache = InMemoryCache()
        cache.put("k", "old")
        cache.put("k", "new")
        self.assertEqual(cache.get("k"), "new")
        self.assertEqual(cache.size(), 1)

    def test_remove(self) -> None:
        cache = InMemoryCache()
        cache.put("k", "v")
        cache.remove("k")
        self.assertIsNone(cache.get("k"))
        self.assertEqual(cache.size(), 0)

    def test_remove_missing_key_is_a_no_op(self) -> None:
        cache = InMemoryCache()
        cache.remove("missing")
        self.assertEqual(cache.size(), 0)


class TtlTest(unittest.TestCase):
    def setUp(self) -> None:
        self.clock = FakeClock()
        patcher = patch("time.monotonic", self.clock)
        patcher.start()
        self.addCleanup(patcher.stop)

    def test_entry_is_live_before_ttl(self) -> None:
        cache = InMemoryCache(ttl_seconds=10)
        cache.put("k", "v")
        self.clock.advance(9.9)
        self.assertEqual(cache.get("k"), "v")

    def test_entry_still_live_exactly_at_ttl(self) -> None:
        # The check is now > expires_at, so the boundary instant is still live.
        cache = InMemoryCache(ttl_seconds=10)
        cache.put("k", "v")
        self.clock.advance(10)
        self.assertEqual(cache.get("k"), "v")

    def test_entry_expires_after_ttl(self) -> None:
        cache = InMemoryCache(ttl_seconds=10)
        cache.put("k", "v")
        self.clock.advance(10.1)
        self.assertIsNone(cache.get("k"))

    def test_expired_get_removes_the_entry(self) -> None:
        cache = InMemoryCache(ttl_seconds=10)
        cache.put("k", "v")
        self.clock.advance(11)
        cache.get("k")
        # Moving the clock back cannot revive it: get() already deleted it.
        self.clock.advance(-11)
        self.assertIsNone(cache.get("k"))

    def test_put_again_resets_the_ttl(self) -> None:
        cache = InMemoryCache(ttl_seconds=10)
        cache.put("k", "v1")
        self.clock.advance(8)
        cache.put("k", "v2")
        self.clock.advance(8)
        self.assertEqual(cache.get("k"), "v2")

    def test_size_ignores_expired_entries(self) -> None:
        cache = InMemoryCache(ttl_seconds=10)
        cache.put("old", "v")
        self.clock.advance(6)
        cache.put("new", "v")
        self.clock.advance(6)
        # "old" is 12s old (expired), "new" is 6s old (live).
        self.assertEqual(cache.size(), 1)
        self.assertEqual(cache.get("new"), "v")

    def test_zero_ttl_never_expires(self) -> None:
        cache = InMemoryCache(ttl_seconds=0)
        cache.put("k", "v")
        self.clock.advance(1_000_000)
        self.assertEqual(cache.get("k"), "v")


if __name__ == "__main__":
    unittest.main()
