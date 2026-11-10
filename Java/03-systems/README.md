# 03 - Systems

This folder connects classical CS ideas to the kinds of systems AI engineers actually build and operate.

This chapter moves the repo from algorithmic reasoning into service behavior:

- how requests are handled
- how expensive work is controlled
- how latency and throughput are managed
- how backend components are exposed safely and predictably

The examples here are intentionally small. They are not production-ready infrastructure. They are teaching examples that expose the main concepts directly:

- caching
- rate limiting
- queues
- API design

## Progression To AI Engineering

This folder answers the operational questions that come after data structures and algorithms:

1. how do we serve capabilities through stable interfaces?
2. how do we control load, latency, and repeated work?
3. how do we decouple expensive tasks from user-facing request paths?

Those questions directly enable AI platforms:

- caches reduce repeated inference and embedding cost
- rate limiters protect expensive model endpoints
- queues support ingestion, indexing, batch jobs, and evaluations
- API design shapes how applications consume AI services reliably

## Files In This Folder

| File | What It Teaches | Important Limitation |
|------|------------------|----------------------|
| `InMemoryCache.java` | local cache, TTL, lazy expiration, monotonic time | single-process demo, not distributed and not thread-safe, no maximum size |
| `LRUCache.java` | LRU eviction built from scratch: HashMap plus doubly linked list with sentinel nodes; connection to KV caching in LLM inference | single-process, not thread-safe, no TTL |
| `RateLimiter.java` | fixed-window rate limiting, locking | one process only, and allows bursts at window boundaries |
| `TokenBucket.java` | token bucket rate limiting, lazy refill, weighted cost | one process only |
| `SimpleQueueDemo.java` | FIFO processing and queue behavior | not a real broker or concurrent queue system |
| `RestApiDesignNotes.md` | API resource design, status codes, error shape, AI API concerns | note file only, not executable code |
| `*Test.java`, `AllTests.java`, `Check.java`, `FakeClock.java`, `ConcurrencyHelper.java` | tests and test helpers | test support only |

## How To Run

From this folder:

```bash
javac *.java
java InMemoryCache
java LRUCache
java RateLimiter
java TokenBucket
java SimpleQueueDemo
java AllTests
```

The cache, rate limiter, and token bucket demos pause for a second or two, so they can show time-based behavior.

The tests do not pause. The time-based classes take an optional clock (a `LongSupplier` standing in for `System.nanoTime()`), and the tests pass a `FakeClock` that moves forward only when told to. Production code uses the public constructors, which read the real clock. The rate limiter and token bucket tests also call `allow()` from 8 threads at once to check the locking. See [01-data-structures/README.md](../01-data-structures/README.md#testing) for how the tests work.

Then read `RestApiDesignNotes.md` to connect the demos to actual service design.

## What To Pay Attention To

### InMemoryCache

- a cache stores previously computed or fetched values
- TTL means the value expires after some amount of time
- this example uses lazy expiration, so cleanup happens during access or size checks
- TTLs use `System.nanoTime()`, which is monotonic; `System.currentTimeMillis()` is wall-clock time and jumps when the system clock is corrected
- compare `nanoTime` values by subtracting (`now - expiresAt > 0`), never with `<` directly
- real systems usually use Redis, Memcached, or another external store

### LRUCache

- the map finds a node in O(1) and the doubly linked list reorders it in O(1); neither structure can do both
- each node stores its own key, so eviction can also remove it from the map
- sentinel head and tail nodes remove every empty-list and end-of-list special case
- in production, `LinkedHashMap` with `accessOrder = true` and `removeEldestEntry` does the same job; the Javadoc shows how

### RateLimiter

- rate limiting protects cost, reliability, and upstream dependencies
- this example uses a fixed window, which is easy to understand but imperfect
- fixed windows can allow up to 2N requests across a window boundary; `RateLimiterTest` demonstrates it
- `allow()` is `synchronized` because "check window, reset, increment" must be one step; two separate `AtomicLong`s are not enough, as the Javadoc explains
- production systems often use token bucket or sliding-window approaches

### TokenBucket

- capacity sets the burst size and the refill rate sets the sustained rate
- refill is computed from elapsed time on each call, so there is no background timer
- the `cost` argument models token-per-minute limits on LLM APIs, where a long prompt spends more of the budget

### SimpleQueueDemo

- queues process work in arrival order
- they help decouple producers from consumers
- queue semantics are central to task execution, background jobs, and event processing
- this example is single-process and purely instructional

### REST API Design Notes

- good APIs are predictable and consistent
- clients need stable status codes and error shapes
- AI APIs often need extra care around rate limits, streaming, retries, and idempotency

## Beginner Mistakes To Avoid

- assuming a local in-memory cache behaves like a distributed cache
- letting a cache grow without an eviction policy
- measuring durations with wall-clock time instead of a monotonic clock
- assuming several atomic variables add up to an atomic operation
- describing a fixed-window limiter as a token bucket
- confusing a queue data structure with a durable message broker
- designing API endpoints around verbs instead of resources

## AI Engineering Connections

- cache model outputs or embedding results to reduce cost and latency
- rate-limit expensive inference or embedding endpoints
- queue long-running jobs such as indexing, chunking, evaluation, or batch inference
- expose AI services through clear, stable APIs

## Suggested Follow-Up Work

- extend `InMemoryCache.java` with a `containsKey` method that respects expiry
- rewrite `LRUCache.java` on top of `LinkedHashMap`, and confirm `LRUCacheTest` still passes
- capstone: combine `LRUCache.java` and `InMemoryCache.java` into a cache with both a TTL and a maximum size, with tests for each eviction path
- add a `waitTimeMillis(cost)` method to `TokenBucket.java` that returns how long until a request of that cost would be allowed
- build a tiny producer/consumer example after `SimpleQueueDemo.java` with `BlockingQueue`
- translate the REST notes into a small mock API spec

## Exit Criteria

You are ready to move on when you can:

- explain what these examples simplify
- describe where each concept fits in a real backend or AI platform
- identify the difference between demo code and production architecture
