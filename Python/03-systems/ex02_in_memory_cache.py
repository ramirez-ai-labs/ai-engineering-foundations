"""Simple in-memory cache - key/value store with optional TTL (time-to-live).

In AI systems: cache embeddings, model outputs, or API responses to avoid
repeated computation or network calls. In production you'd use Redis/Memcached;
this illustrates the core idea.

This demo uses lazy expiration:
- expired entries are removed when you call get()
- size() also cleans up expired entries before reporting

It uses time.monotonic() rather than wall-clock time so TTLs are not affected
by system clock adjustments.

Limitations: it is not thread-safe, and it has no maximum size, so a real cache
would also need an eviction policy such as LRU to keep memory bounded.
"""

import math
import time


class InMemoryCache:
    def __init__(self, ttl_seconds: float = 0.0) -> None:
        """ttl_seconds <= 0 means entries never expire."""
        # Without this check, NaN would fail "self._ttl > 0" in put() and
        # silently mean "never expire", serving stale data forever. Use 0 to
        # turn expiry off on purpose.
        if not math.isfinite(ttl_seconds):
            raise ValueError("ttl_seconds must be a finite number (use 0 for no expiry)")
        self._ttl = ttl_seconds
        # Two dicts share the same keys: one for values, one for expiry times.
        self._values: dict[str, str] = {}
        self._expires_at: dict[str, float] = {}

    def put(self, key: str, value: str) -> None:
        self._values[key] = value
        if self._ttl > 0:
            self._expires_at[key] = time.monotonic() + self._ttl

    def get(self, key: str) -> str | None:
        if key not in self._values:
            return None
        if self._is_expired(key, time.monotonic()):
            self.remove(key)
            return None
        return self._values[key]

    def remove(self, key: str) -> None:
        if key in self._values:
            del self._values[key]
        if key in self._expires_at:
            del self._expires_at[key]

    def size(self) -> int:
        self._remove_expired_entries()
        return len(self._values)

    def _is_expired(self, key: str, now: float) -> bool:
        # Keys with no expiry time never expire.
        if key not in self._expires_at:
            return False
        return now > self._expires_at[key]

    def _remove_expired_entries(self) -> None:
        now = time.monotonic()
        # Build the list first: deleting while iterating a dict raises RuntimeError.
        expired_keys: list[str] = []
        for key in self._values:
            if self._is_expired(key, now):
                expired_keys.append(key)

        for key in expired_keys:
            self.remove(key)


def main() -> None:
    # Cache with 2-second TTL.
    cache = InMemoryCache(ttl_seconds=2.0)
    cache.put("user:1", "Alice")
    cache.put("user:2", "Bob")
    print(f"get(user:1): {cache.get('user:1')}")
    print(f"get(missing): {cache.get('missing')}")
    print(f"size: {cache.size()}")

    # Remove user:1 while it is still live, so this shows remove() working.
    cache.remove("user:1")
    print(f"after remove, get(user:1): {cache.get('user:1')}")
    print(f"size after remove: {cache.size()}")

    # Sleep long enough for the remaining entry to expire.
    time.sleep(2.1)

    print(f"after TTL, get(user:2): {cache.get('user:2')}")
    print(f"size after TTL cleanup: {cache.size()}")


if __name__ == "__main__":
    main()
