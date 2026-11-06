# RAG Architecture

RAG (Retrieval-Augmented Generation) is a system design pattern, not a model feature.

The core idea: store external knowledge outside the model, retrieve the most relevant pieces at query time, and provide them as context for generation. This is the dominant architecture for grounding LLM responses in specific documents, internal knowledge bases, or time-sensitive data.

This note walks through the full system — ingestion, retrieval, generation — and identifies where each stage can fail and how the CS foundations in this repo show up inside it.

## System Overview

```
INGESTION PIPELINE                    QUERY PIPELINE
──────────────────                    ──────────────

  Raw Documents                         User Query
       │                                     │
       ▼                                     ▼
   Chunking                           Embed Query
       │                                     │
       ▼                                     ▼
  Embedding                          Vector Search ──► Top-K Chunks
  (per chunk)                               │
       │                                    ▼
       ▼                              (Optional) Re-rank
  Vector Store                              │
  + Metadata                               ▼
       │                             Assemble Prompt
       └──────────────────────────► (system + chunks + query)
                                           │
                                           ▼
                                      LLM Generate
                                           │
                                           ▼
                                       Response
```

Each arrow is a place where the system can fail. Understanding the failure modes is as important as understanding the happy path.

## Stage 1: Ingestion

### Chunking

Documents must be split into chunks before embedding. Chunk boundaries matter more than most engineers expect.

| Chunking strategy | When to use | Risk |
|-------------------|-------------|------|
| Fixed-size (e.g. 512 tokens) | Simple baseline | Splits mid-sentence, loses context at boundaries |
| Sentence-aware | Prose documents | Can produce very short or very long chunks |
| Semantic (embedding-based splits) | High-quality corpora | Slow and complex to implement |
| Hierarchical (parent + child chunks) | Long docs with internal structure | More index complexity |

**The rule of thumb:** chunks should be semantically coherent and short enough that the embedding captures a single idea, not a collection of unrelated paragraphs.

Overlap between adjacent chunks (e.g. 10–20% token overlap) reduces the risk of splitting a relevant passage across two chunks.

### Embedding

Each chunk is passed through an embedding model to produce a fixed-length vector.

Critical constraint: use the same embedding model for both ingestion and query-time embedding. A query embedded with model A and chunks embedded with model B will not produce meaningful similarity scores.

### Storage

Vectors plus metadata are stored in a vector database (Pinecone, Weaviate, pgvector, Chroma, Qdrant, etc.). The metadata — source document, chunk index, section title, timestamp, access controls — is what enables filtered retrieval.

The Java demo `SimpleVectorSearchDemo.java` implements the minimum version of this: in-memory vectors, cosine similarity scoring, top-k retrieval. The benchmark in that file shows why the linear-scan approach in the demo breaks at scale.

## Stage 2: Retrieval

### Vector Search

The user query is embedded with the same model used during ingestion. The query vector is scored against stored chunk vectors. Top-K chunks by similarity are returned.

Failure modes at this stage:

- **Wrong K:** too small and you miss relevant context; too large and you flood the prompt with noise
- **No metadata filtering:** similarity alone retrieves from the wrong tenant, language, or time range
- **Embedding model mismatch:** query and chunks embedded with different models produce garbage rankings
- **Index staleness:** new or updated documents are not in the index yet

### Re-ranking

Vector similarity is fast but imprecise. A cross-encoder re-ranker takes the query and each retrieved chunk together and produces a more accurate relevance score.

The tradeoff: cross-encoders are slower than approximate vector search, so they are typically applied to a shortlist (top-20 to top-50 from vector search), not the full corpus.

When to add re-ranking: when users report that obviously relevant documents are not appearing in answers, and the issue is retrieval quality rather than generation quality.

## Stage 3: Generation

### Prompt Assembly

Retrieved chunks are formatted and inserted into the prompt, typically between the system instructions and the user query.

Two constraints shape this:

1. **Context budget:** every chunk consumes tokens that must fit within the model's context window (see `TransformerIntuition.md`)
2. **Position sensitivity:** models attend more reliably to content at the beginning and end of the context; important content buried in the middle of a long context is sometimes missed ("lost in the middle")

### Generation

The LLM receives the assembled prompt and generates a response conditioned on the retrieved context.

The model does not "know" whether the retrieved chunks are correct. It generates text consistent with the context provided. If retrieval is wrong, generation will confidently produce wrong answers — this is the most common RAG failure mode in production.

## How Chapter 03 Systems Concepts Plug In

| Systems concept | Where it appears in RAG |
|----------------|------------------------|
| Cache (InMemoryCache.java, LRUCache.java) | Cache embedding results for repeated queries; cache KV tensors for shared system prompt prefixes |
| Rate limiter (RateLimiter.java) | Protect the embedding API and LLM endpoint from request spikes |
| Queue (SimpleQueueDemo.java) | Async ingestion pipeline: documents are queued, chunked, embedded, and indexed in the background |
| API design (RestApiDesignNotes.md) | RAG is typically exposed as a retrieval API; good API shape matters for streaming, error handling, and retry logic |

## Operational Concerns

RAG systems require ongoing maintenance, not just initial setup.

| Concern | Why it matters |
|---------|---------------|
| Index freshness | New documents must be ingested promptly; stale indexes produce outdated answers |
| Retrieval quality drift | As document content changes, previously high-performing chunks may become less relevant |
| Cost tracking | Embedding every document chunk and running vector search on every query adds up; monitor per-request cost |
| Evaluation | Without a measurement loop, you cannot tell whether RAG quality is improving or degrading over time |

## What Good RAG Looks Like At Production Scale

A mature RAG system adds:

- chunking pipeline with overlap and configurable strategy
- evaluation dataset (question, expected answer, expected source chunks)
- offline recall and precision measurement before shipping index changes
- metadata schema that supports access control, freshness filtering, and multi-tenant isolation
- monitoring for retrieval latency, embedding API errors, and answer groundedness

The evaluation layer is covered in `EvaluationMetrics.md`.

## Suggested Next Steps

1. Run `SimpleVectorSearchDemo.java` and observe the benchmark — that latency curve is what makes production vector databases necessary.
2. Read `EvaluationMetrics.md` to understand how to measure whether your RAG system is working.
3. Read `TransformerIntuition.md` to understand what happens inside the LLM during the generation step.
4. For a production implementation of the evaluation loop described here, see [rag-evaluation-lab](https://github.com/ramirez-ai-labs/rag-evaluation-lab).
