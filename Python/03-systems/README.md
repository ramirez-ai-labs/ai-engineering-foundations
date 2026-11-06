# 03 - Systems

Small versions of the components that keep AI services affordable and reliable. Caches reduce repeated inference and embedding cost, rate limiters protect expensive model endpoints, and queues separate slow jobs from user-facing requests.

These are teaching examples, not production infrastructure.

## Files In This Folder

| File | What It Teaches | Important Limitation |
|------|------------------|----------------------|
| `in_memory_cache.py` | local cache, TTL, lazy expiration | single process, not thread-safe, no maximum size, string keys and values only |
| `rate_limiter.py` | fixed-window rate limiting, locking | one process only, and allows bursts at window boundaries |
| `simple_queue_demo.py` | FIFO processing with `deque` | not a durable broker or a concurrent queue |

For API design, read the language-neutral [REST API design notes](../../Java/03-systems/RestApiDesignNotes.md). For cache eviction, read [LRUCache.java](../../Java/03-systems/LRUCache.java) in the Java track, which has no Python version yet.

## How To Run

From this folder:

```bash
python in_memory_cache.py
python rate_limiter.py
python simple_queue_demo.py
```

The cache and rate limiter demos pause for about two seconds and one second, so they can show time-based behavior.

## What To Pay Attention To

- **Cache:** TTL means a value expires after a set time. Expired entries are only cleaned up during `get()` or `size()`.
- **Monotonic time:** `time.monotonic()` keeps TTLs and windows correct even if the system clock changes.
- **Rate limiter:** a fixed window can allow up to 2N requests across a boundary. Production systems often use token bucket or sliding window instead.
- **Locking:** checking and updating the count must happen as one step, or two threads can both pass the limit.
- **Queue:** work comes out in arrival order. Use `queue.Queue` across threads and a broker such as SQS or Kafka across services.

## Beginner Mistakes To Avoid

- assuming a local cache behaves like a distributed one
- letting a cache grow without an eviction policy
- confusing a queue data structure with a durable message broker

## Practice Prompts

- `in_memory_cache.py`: add a `contains` method that respects expiry
- `rate_limiter.py`: add a test that runs `allow()` from several threads
- `simple_queue_demo.py`: build a small producer/consumer with `queue.Queue`

## Exit Criteria

Move on when you can explain what each example simplifies and where each concept fits in a real AI platform.

The [Java chapter README](../../Java/03-systems/README.md) has a longer walkthrough of how these concepts connect to AI engineering.
