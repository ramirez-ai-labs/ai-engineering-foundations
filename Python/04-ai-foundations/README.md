# 04 - AI Foundations

This chapter has no Python code yet. Its material is a set of written notes shared by both language tracks, so it lives in one place rather than being duplicated.

## Current Material

Read the notes in this order:

| Note | What It Teaches |
|------|------------------|
| [Embeddings and Vector Search](../../Java/04-ai-foundations/01-EmbeddingsAndVectorSearch.md) | embeddings, cosine similarity, retrieval flow, the RAG connection, and failure modes |
| [Transformer Intuition](../../Java/04-ai-foundations/02-TransformerIntuition.md) | attention as weighted lookup, context window constraints, and KV caching |
| [RAG Architecture](../../Java/04-ai-foundations/03-RAGArchitecture.md) | the ingestion, retrieval, and generation pipeline, and how it fails at each stage |
| [Evaluation Metrics](../../Java/04-ai-foundations/04-EvaluationMetrics.md) | Recall@k, Precision@k, MRR, groundedness, and offline evaluation loops |

## Finding The Python Examples

The notes name Java files. These are the Python equivalents:

| In The Notes | In This Track |
|------|------|
| `Ex01SimpleQueueDemo.java` | [ex01_simple_queue_demo.py](../03-systems/ex01_simple_queue_demo.py) |
| `Ex02InMemoryCache.java` | [ex02_in_memory_cache.py](../03-systems/ex02_in_memory_cache.py) |
| `Ex03RateLimiter.java` | [ex03_rate_limiter.py](../03-systems/ex03_rate_limiter.py) |
| `Ex05LRUCache.java` | [ex05_lru_cache.py](../03-systems/ex05_lru_cache.py) |
| `Ex01SimpleVectorSearchDemo.java` | [ex01_simple_vector_search_demo.py](../05-mini-projects/ex01_simple_vector_search_demo.py), without the benchmark |
| `Ex02KnowledgeGraphBFS.java` | [ex02_knowledge_graph_bfs.py](../05-mini-projects/ex02_knowledge_graph_bfs.py) |

The [Java chapter README](../../Java/04-ai-foundations/README.md) describes the chapter's goals and the topics it covers.

## Why This Matters

This chapter links the earlier ones to real AI systems. Lists hold vectors, a similarity score drives ranking, and systems constraints shape how retrieval runs in production.
