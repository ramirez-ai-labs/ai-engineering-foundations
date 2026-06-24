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
| `InMemoryCache.java` | local cache, TTL, lazy expiration | single-process demo, not distributed and not thread-safe |
| `LRUCache.java` | LRU eviction, access-order tracking; connection to KV caching in LLM inference | single-process, not thread-safe, no TTL |
| `RateLimiter.java` | fixed-window rate limiting | simplified implementation, not token bucket or sliding window |
| `SimpleQueueDemo.java` | FIFO processing and queue behavior | not a real broker or concurrent queue system |
| `RestApiDesignNotes.md` | API resource design, status codes, error shape, AI API concerns | note file only, not executable code |

## How To Run

From this folder:

```bash
javac *.java
java InMemoryCache
java RateLimiter
java SimpleQueueDemo
```

Then read `RestApiDesignNotes.md` to connect the demos to actual service design.

## What To Pay Attention To

### InMemoryCache

- a cache stores previously computed or fetched values
- TTL means the value expires after some amount of time
- this example uses lazy expiration, so cleanup happens during access or size checks
- real systems usually use Redis, Memcached, or another external store

### RateLimiter

- rate limiting protects cost, reliability, and upstream dependencies
- this example uses a fixed window, which is easy to understand but imperfect
- fixed windows can create burstiness near window boundaries
- production systems often use token bucket or sliding-window approaches

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
- describing a fixed-window limiter as a token bucket
- confusing a queue data structure with a durable message broker
- designing API endpoints around verbs instead of resources

## AI Engineering Connections

- cache model outputs or embedding results to reduce cost and latency
- rate-limit expensive inference or embedding endpoints
- queue long-running jobs such as indexing, chunking, evaluation, or batch inference
- expose AI services through clear, stable APIs

## Suggested Follow-Up Work

- extend `InMemoryCache.java` with a `containsKey` method
- add a short sleep in `RateLimiter.java` to demonstrate a new time window
- build a tiny producer/consumer example after `SimpleQueueDemo.java`
- translate the REST notes into a small mock API spec

## Exit Criteria

You are ready to move on when you can:

- explain what these examples simplify
- describe where each concept fits in a real backend or AI platform
- identify the difference between demo code and production architecture
