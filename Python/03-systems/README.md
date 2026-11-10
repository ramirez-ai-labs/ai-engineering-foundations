# 03 - Systems

Small versions of the components that keep AI services affordable and reliable. Caches reduce repeated inference and embedding cost, rate limiters protect expensive model endpoints, and queues separate slow jobs from user-facing requests.

These are teaching examples, not production infrastructure.

## Files In This Folder

| File | What It Teaches | Important Limitation |
|------|------------------|----------------------|
| `in_memory_cache.py` | local cache, TTL, lazy expiration | single process, not thread-safe, no maximum size, string keys and values only |
| `lru_cache.py` | LRU eviction from scratch: dict plus doubly linked list with sentinel nodes | single process, not thread-safe, no TTL |
| `rate_limiter.py` | fixed-window rate limiting, locking | one process only, and allows bursts at window boundaries |
| `token_bucket.py` | token bucket rate limiting, lazy refill, weighted cost | one process only |
| `simple_queue_demo.py` | FIFO processing with `deque` | not a durable broker or a concurrent queue |

For API design, read the language-neutral [REST API design notes](../../Java/03-systems/RestApiDesignNotes.md).

`lru_cache.py` builds the linked list by hand. The Java track's [LRUCache.java](../../Java/03-systems/LRUCache.java) gets the same behavior from `LinkedHashMap`, which matches Python's `OrderedDict`. Interviewers usually ask for the hand-built version.

## How To Run

From this folder:

```bash
python in_memory_cache.py
python lru_cache.py
python rate_limiter.py
python token_bucket.py
python simple_queue_demo.py
python -m unittest -v
```

The cache, rate limiter, and token bucket demos pause for a second or two, so they can show time-based behavior. The tests do not pause: they replace `time.monotonic` with a fake clock and move time forward by hand. The rate limiter and token bucket tests also call `allow()` from several threads to check the locking.

## What To Pay Attention To

- **Cache:** TTL means a value expires after a set time. Expired entries are only cleaned up during `get()` or `size()`.
- **Monotonic time:** `time.monotonic()` keeps TTLs and windows correct even if the system clock changes.
- **LRU:** the dict finds a node in O(1) and the doubly linked list reorders it in O(1). Each node stores its own key so eviction can also delete it from the dict. Sentinel head and tail nodes remove every empty-list and end-of-list special case.
- **Rate limiter:** a fixed window can allow up to 2N requests across a boundary. A test in `test_rate_limiter.py` demonstrates it.
- **Token bucket:** capacity sets the burst size and the refill rate sets the sustained rate. Refill is computed from elapsed time on each call, so there is no background timer. The `cost` argument models token-per-minute limits on LLM APIs.
- **Locking:** checking and updating the count must happen as one step, or two threads can both pass the limit.
- **Queue:** work comes out in arrival order. Use `queue.Queue` across threads and a broker such as SQS or Kafka across services.

## Beginner Mistakes To Avoid

- assuming a local cache behaves like a distributed one
- letting a cache grow without an eviction policy
- confusing a queue data structure with a durable message broker

## Practice Prompts

- `in_memory_cache.py`: add a `contains` method that respects expiry
- `lru_cache.py`: rewrite it with `OrderedDict`, and confirm the tests still pass
- capstone: combine `lru_cache.py` and `in_memory_cache.py` into a cache with both a TTL and a maximum size, with tests for each eviction path
- `token_bucket.py`: add a `wait_time(cost)` method that returns how long until a request of that cost would be allowed
- `simple_queue_demo.py`: build a small producer/consumer with `queue.Queue`

## Exit Criteria

Move on when you can explain what each example simplifies and where each concept fits in a real AI platform.

The [Java chapter README](../../Java/03-systems/README.md) has a longer walkthrough of how these concepts connect to AI engineering.
