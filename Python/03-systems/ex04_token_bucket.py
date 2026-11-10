"""Token bucket rate limiter - allows short bursts while enforcing an average rate.

The bucket holds up to `capacity` tokens and refills at `refill_per_second`.
Each request spends tokens; if there are not enough, the request is rejected.
- capacity sets the largest burst a client can send at once
- refill_per_second sets the sustained rate

Compared with the fixed window in ex03_rate_limiter.py, there is no window boundary,
so a client can never get 2N requests through by timing them around one. The
bucket simply never holds more than `capacity` tokens.

Refill is lazy: instead of a background timer adding tokens, each call works
out how many tokens accumulated since the last call. That keeps it O(1) per
request with no threads to manage.

In AI systems: LLM providers rate-limit by requests AND by tokens per minute.
The `cost` argument models the second case, where a large prompt spends more
of the budget than a small one.

Limitations: one process only. A shared limit across servers keeps the bucket
state in a store such as Redis and updates it atomically (e.g. a Lua script).
"""

import math
import threading
import time


def _is_positive_finite(value: float) -> bool:
    # "value <= 0" alone is not enough: NaN fails every comparison, so it would
    # slip through, and infinity passes "> 0" but makes the limit meaningless
    # (infinite capacity allows every request).
    return math.isfinite(value) and value > 0


class TokenBucket:
    def __init__(self, capacity: float, refill_per_second: float) -> None:
        if not _is_positive_finite(capacity) or not _is_positive_finite(refill_per_second):
            raise ValueError("capacity and refill_per_second must be positive finite numbers")
        self._capacity = capacity
        self._refill_per_second = refill_per_second
        # Start full, so a new client can burst immediately.
        self._tokens = capacity
        self._last_refill = time.monotonic()
        self._lock = threading.Lock()

    def allow(self, cost: float = 1.0) -> bool:
        """Spend `cost` tokens if available. Return True if the request is allowed."""
        if not _is_positive_finite(cost):
            raise ValueError("cost must be a positive finite number")
        if cost > self._capacity:
            # The bucket can never hold this many tokens, so the request could
            # never succeed. Fail loudly instead of rejecting it forever.
            raise ValueError("cost exceeds bucket capacity")

        # Refill, check, and spend must happen as one step, or two threads can
        # both see enough tokens and both spend them.
        with self._lock:
            self._refill(time.monotonic())
            if self._tokens >= cost:
                self._tokens -= cost
                return True
            return False

    def _refill(self, now: float) -> None:
        elapsed = now - self._last_refill
        refilled = self._tokens + elapsed * self._refill_per_second
        # Tokens beyond capacity are discarded: idle time cannot be saved up
        # into an unlimited burst.
        # Python shortcut: min(self._capacity, refilled)
        if refilled > self._capacity:
            refilled = self._capacity
        self._tokens = refilled
        self._last_refill = now


def main() -> None:
    # Bursts of up to 3 requests, refilling 2 tokens per second on average.
    bucket = TokenBucket(capacity=3, refill_per_second=2)
    for request in range(1, 5):
        print(f"Request {request}: {bucket.allow()}")  # True, True, True, False

    # Half a second at 2 tokens per second refills one token. Sleep a little
    # longer, because sleep and clock resolution are not exact.
    time.sleep(0.6)
    print(f"Request 5 after 0.6s: {bucket.allow()}")  # True
    print(f"Request 6: {bucket.allow()}")  # False

    # A request can cost more than one token, like a long prompt.
    time.sleep(1.5)
    print(f"Request costing 3 tokens after 1.5s: {bucket.allow(cost=3)}")  # True


if __name__ == "__main__":
    main()
