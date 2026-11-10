"""Tests for lru_cache. Run from this folder: python -m unittest -v"""

import unittest

from lru_cache import LRUCache


def filled_cache(capacity: int, keys: list[str]) -> LRUCache:
    cache = LRUCache(capacity)
    for key in keys:
        cache.put(key, key.upper())
    return cache


class ValidationTest(unittest.TestCase):
    def test_rejects_non_positive_capacity(self) -> None:
        for capacity in [0, -1]:
            with self.assertRaises(ValueError):
                LRUCache(capacity)


class BasicOperationsTest(unittest.TestCase):
    def test_get_missing_returns_none(self) -> None:
        self.assertIsNone(LRUCache(2).get("missing"))

    def test_put_then_get(self) -> None:
        cache = LRUCache(2)
        cache.put("a", "alpha")
        self.assertEqual(cache.get("a"), "alpha")
        self.assertEqual(cache.size(), 1)

    def test_put_existing_key_updates_value(self) -> None:
        cache = LRUCache(2)
        cache.put("a", "old")
        cache.put("a", "new")
        self.assertEqual(cache.get("a"), "new")
        self.assertEqual(cache.size(), 1)

    def test_empty_cache_has_no_keys(self) -> None:
        self.assertEqual(LRUCache(3).keys_most_recent_first(), [])


class EvictionTest(unittest.TestCase):
    def test_evicts_least_recently_inserted_when_never_read(self) -> None:
        cache = filled_cache(3, ["a", "b", "c", "d"])
        self.assertIsNone(cache.get("a"))
        self.assertEqual(cache.keys_most_recent_first(), ["d", "c", "b"])

    def test_get_protects_a_key_from_eviction(self) -> None:
        cache = filled_cache(3, ["a", "b", "c"])
        cache.get("a")
        cache.put("d", "D")
        # "b" is now the least recently used, not "a".
        self.assertIsNone(cache.get("b"))
        self.assertEqual(cache.get("a"), "A")

    def test_update_counts_as_a_use_and_does_not_evict(self) -> None:
        cache = filled_cache(3, ["a", "b", "c"])
        cache.put("a", "A2")
        self.assertEqual(cache.size(), 3)
        cache.put("d", "D")
        self.assertIsNone(cache.get("b"))
        self.assertEqual(cache.get("a"), "A2")

    def test_missing_get_does_not_change_order(self) -> None:
        cache = filled_cache(2, ["a", "b"])
        cache.get("zzz")
        self.assertEqual(cache.keys_most_recent_first(), ["b", "a"])

    def test_size_never_exceeds_capacity(self) -> None:
        cache = LRUCache(3)
        for number in range(100):
            cache.put(str(number), "v")
            self.assertLessEqual(cache.size(), 3)
        self.assertEqual(cache.keys_most_recent_first(), ["99", "98", "97"])

    def test_capacity_one(self) -> None:
        cache = filled_cache(1, ["a", "b"])
        self.assertIsNone(cache.get("a"))
        self.assertEqual(cache.get("b"), "B")

    def test_evicted_key_can_be_added_again(self) -> None:
        cache = filled_cache(2, ["a", "b", "c"])
        cache.put("a", "back")
        self.assertEqual(cache.get("a"), "back")
        self.assertEqual(cache.keys_most_recent_first(), ["a", "c"])


class LinkIntegrityTest(unittest.TestCase):
    def test_walking_backwards_matches_walking_forwards(self) -> None:
        # A broken prev pointer would pass the forward-only tests above.
        # This test reaches into the private sentinels, so if you rebuild the
        # class from scratch, keep the names _head, _tail, prev, and key.
        cache = filled_cache(4, ["a", "b", "c", "d", "e"])
        cache.get("c")
        cache.put("b", "B2")

        backwards: list[str] = []
        current = cache._tail.prev
        while current is not cache._head:
            backwards.append(current.key)
            current = current.prev

        forwards = cache.keys_most_recent_first()
        forwards.reverse()
        self.assertEqual(backwards, forwards)


if __name__ == "__main__":
    unittest.main()
