# 04 - AI Foundations

This chapter has no Python code yet. Its material is a set of written notes shared by both language tracks, so it lives in one place rather than being duplicated.

## Current Material

Read the notes in this order:

| Note | What It Teaches |
|------|------------------|
| [Embeddings and Vector Search](../../Java/04-ai-foundations/EmbeddingsAndVectorSearch.md) | embeddings, cosine similarity, retrieval flow, the RAG connection, and failure modes |
| [Transformer Intuition](../../Java/04-ai-foundations/TransformerIntuition.md) | attention as weighted lookup, context window constraints, and KV caching |
| [RAG Architecture](../../Java/04-ai-foundations/RAGArchitecture.md) | the ingestion, retrieval, and generation pipeline, and how it fails at each stage |
| [Evaluation Metrics](../../Java/04-ai-foundations/EvaluationMetrics.md) | Recall@k, Precision@k, MRR, groundedness, and offline evaluation loops |

## Finding The Python Examples

The notes name Java files. These are the Python equivalents:

| In The Notes | In This Track |
|------|------|
| `SimpleVectorSearchDemo.java` | [simple_vector_search_demo.py](../05-mini-projects/simple_vector_search_demo.py), without the benchmark |
| `InMemoryCache.java` | [in_memory_cache.py](../03-systems/in_memory_cache.py) |
| `RateLimiter.java` | [rate_limiter.py](../03-systems/rate_limiter.py) |
| `SimpleQueueDemo.java` | [simple_queue_demo.py](../03-systems/simple_queue_demo.py) |
| `LRUCache.java` | [lru_cache.py](../03-systems/lru_cache.py), built from a dict and a doubly linked list instead of `LinkedHashMap` |
| `KnowledgeGraphBFS.java` | [knowledge_graph_bfs.py](../05-mini-projects/knowledge_graph_bfs.py) |

The [Java chapter README](../../Java/04-ai-foundations/README.md) describes the chapter's goals and the topics it covers.

## Why This Matters

This chapter links the earlier ones to real AI systems. Lists hold vectors, a similarity score drives ranking, and systems constraints shape how retrieval runs in production.
