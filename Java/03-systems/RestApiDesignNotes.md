# REST API Design Notes

Short reference for designing APIs that AI services (and clients) will call.

## Principles

1. **Resources over actions** — URLs name resources (`/users`, `/models`), not verbs. Use HTTP methods for actions: GET (read), POST (create), PUT/PATCH (update), DELETE.
2. **Stateless** — Each request carries enough context; no server-side session required. Auth via headers (e.g. `Authorization: Bearer <token>`).
3. **Consistent response shape** — Use a standard envelope for success and errors so clients can parse uniformly.

## Example response shapes

**Success (e.g. POST /embeddings, which creates embeddings from the input text in the request body):**

```json
{
  "data": [ { "embedding": [0.1, -0.2, ...], "index": 0 } ],
  "model": "text-embedding-3-small",
  "usage": { "prompt_tokens": 5, "total_tokens": 5 }
}
```

**Error:**

```json
{
  "error": {
    "code": "rate_limit_exceeded",
    "message": "Too many requests. Retry after 60s."
  }
}
```

## Status codes

| Code | Meaning |
|------|---------|
| 200 | OK (GET, PUT, PATCH) |
| 201 | Created (POST) |
| 204 | No content (DELETE success) |
| 400 | Bad request (validation, malformed body) |
| 401 | Unauthorized (missing or invalid auth) |
| 403 | Forbidden (auth OK but not allowed) |
| 404 | Not found |
| 429 | Too many requests (rate limit) |
| 500 | Internal server error |

## Rate limiting

- Return `429` when the client exceeds the limit.
- Include headers: `Retry-After`, `X-RateLimit-Limit`, `X-RateLimit-Remaining` so clients can back off and retry.

## For AI / LLM APIs

- **Streaming** — Use chunked transfer or SSE for long outputs (e.g. chat completions).
- **Timeouts** — LLM calls can be slow; set generous timeouts and document them.
- **Idempotency** — For non-idempotent operations, support `Idempotency-Key` header so clients can retry safely.

Use these notes alongside the cache, rate limiter, and queue demos in either track's `03-systems/` folder to connect code to API behavior.
