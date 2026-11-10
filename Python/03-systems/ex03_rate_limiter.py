"""Simple rate limiter - fixed window: allow at most N requests per window.

In AI systems: limit calls to LLM APIs, embedding APIs, or your own service
to avoid throttling and control cost. Production systems often use token bucket
or sliding window; this is the basic idea.

Known fixed-window weakness: a client can send up to 2N requests across a
window boundary (N at the end of one window, N at the start of the next).

Thread safety: a Lock makes the "check window, reset, increment" sequence
atomic as a whole. Separate atomic variables would not be enough; the Java
version's Javadoc explains why.
"""

import math
import threading
import time


class RateLimiter:
    def __init__(self, max_requests: int, window_seconds: float) -> None:
        """Allow at most max_requests per window_seconds."""
        # math.isfinite rejects NaN, which "window_seconds <= 0" would let through
        # (NaN fails every comparison, so the window would never reset), and
        # infinity, which would also mean a window that never resets.
        if max_requests <= 0 or not math.isfinite(window_seconds) or window_seconds <= 0:
            raise ValueError("max_requests and window_seconds must be positive finite numbers")
        self._max_requests = max_requests
        self._window = window_seconds
        self._window_start = time.monotonic()
        self._count = 0
        self._lock = threading.Lock()

    def allow(self) -> bool:
        """Return True if the request is allowed, False if rate limited."""
        # "with" takes the lock here and always releases it when the block ends,
        # even on an early return or an error. Only one thread can be inside.
        with self._lock:
            now = time.monotonic()
            if now - self._window_start >= self._window:
                # New window.
                self._window_start = now
                self._count = 0
            if self._count < self._max_requests:
                self._count += 1
                return True
            return False


def main() -> None:
    # Allow 2 requests per 1 second window.
    limiter = RateLimiter(max_requests=2, window_seconds=1.0)
    print(f"Request 1: {limiter.allow()}")  # True
    print(f"Request 2: {limiter.allow()}")  # True
    print(f"Request 3: {limiter.allow()}")  # False (rate limited)

    time.sleep(1.1)

    print(f"Request 4 after new window: {limiter.allow()}")  # True


if __name__ == "__main__":
    main()
