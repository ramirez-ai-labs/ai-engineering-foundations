# 03 – Systems

Connect CS theory to real systems you build with AI: caching, rate limiting, queues, and API design.

## Files

| File | Concepts |
|------|----------|
| `InMemoryCache.java` | Simple key-value cache with TTL / eviction |
| `RateLimiter.java` | Token-bucket or fixed-window rate limiting |
| `SimpleQueueDemo.java` | Queue as abstraction; producer/consumer pattern |
| `RestApiDesignNotes.md` | API design principles and notes |

## How to run

```bash
javac *.java && java InMemoryCache
# or: java RateLimiter, java SimpleQueueDemo
```

Read `RestApiDesignNotes.md` for design concepts.
