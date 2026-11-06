# Evaluation Metrics For AI Systems

Building an AI system without an evaluation loop is like writing code without tests: you ship, you guess, and you learn about failures from users.

This note covers the metrics that matter most for retrieval and generation quality in LLM-based systems — defined plainly, with small examples. It then describes how to build a minimal offline evaluation loop before deploying or changing a RAG pipeline.

## Why Evaluation Is Non-Negotiable

LLMs produce confident-sounding text regardless of whether the content is correct. Without measurement, "it sounds good" is the only quality signal — and it is a poor one.

Evaluation answers questions like:

- Is retrieval returning the documents that actually contain the answer?
- Is the model using the retrieved context, or hallucinating beyond it?
- Did this change to the chunking strategy make retrieval better or worse?
- Which embedding model produces higher recall for this document corpus?

None of those questions have answers without metrics and a test dataset.

## Retrieval Metrics

These metrics measure the quality of the retrieval stage — independent of what the LLM does with the results.

### Recall@K

**Definition:** of all the chunks that are relevant to this query, what fraction appear in the top-K retrieved results?

```
Recall@K = (relevant chunks in top-K) / (total relevant chunks)
```

**Example:**

- Query: "What is the rate limit for the embeddings API?"
- Relevant chunks: [chunk-3, chunk-7, chunk-12]
- Top-5 retrieved: [chunk-3, chunk-1, chunk-7, chunk-22, chunk-9]
- Relevant chunks in top-5: chunk-3, chunk-7 → 2 out of 3

```
Recall@5 = 2 / 3 = 0.67
```

**Why it matters:** if the relevant chunk is not retrieved, the model cannot use it. Recall@K is the primary signal for whether retrieval is working.

**Typical targets:** Recall@5 above 0.75–0.80 is a reasonable bar before deploying; below 0.6 usually indicates a chunking or embedding problem.

### Precision@K

**Definition:** of the top-K retrieved chunks, what fraction are actually relevant?

```
Precision@K = (relevant chunks in top-K) / K
```

**Example (same as above):**

```
Precision@5 = 2 / 5 = 0.40
```

**Why it matters:** low precision means the model's context is cluttered with irrelevant chunks, which increases noise and can push relevant content outside the effective attention range.

**Recall vs Precision tension:** increasing K improves recall but typically reduces precision. The right K depends on your context budget and re-ranking capability.

### MRR (Mean Reciprocal Rank)

**Definition:** the average of the reciprocal rank of the first relevant chunk across queries.

```
MRR = (1 / |Q|) * Σ (1 / rank_of_first_relevant_chunk_for_query_i)
```

**Example:**

| Query | First relevant chunk at rank | Reciprocal rank |
|-------|------------------------------|-----------------|
| Q1    | 1                            | 1/1 = 1.00      |
| Q2    | 3                            | 1/3 = 0.33      |
| Q3    | 2                            | 1/2 = 0.50      |

```
MRR = (1.00 + 0.33 + 0.50) / 3 = 0.61
```

**Why it matters:** MRR rewards systems that put a relevant result at the top. A high Recall@10 with MRR of 0.3 means relevant documents are being retrieved but buried — useful for batch workloads, bad for user-facing search.

## Generation Metrics

These metrics measure the quality of what the LLM produces, given the retrieved context.

### Groundedness

**Definition:** does the model's response use the retrieved context, or does it generate claims that go beyond (or contradict) what was provided?

Groundedness is typically measured by an LLM-as-judge: a separate model is asked whether each claim in the response can be supported by at least one retrieved chunk.

```
Groundedness = (claims supported by retrieved context) / (total claims in response)
```

A groundedness score near 1.0 means the model is citing what it was given. A low score means hallucination.

**Why it matters:** high retrieval quality with low groundedness means the model is ignoring the context. This usually points to a prompt engineering problem (unclear instructions), a context position problem (relevant chunks buried), or a model that was fine-tuned to answer from memory.

### Answer Correctness

**Definition:** does the response answer the question correctly, relative to a reference answer?

This can be measured by exact string match (for structured outputs), F1 over tokens (for extractive QA), or LLM-as-judge comparison (for open-ended answers).

Answer correctness integrates both retrieval and generation: a correct answer requires the right chunk to be retrieved and the model to use it correctly.

## Building A Minimal Offline Evaluation Loop

An evaluation loop needs three things:

1. **A test dataset:** question, expected answer, expected source chunk IDs
2. **A retrieval function:** given a question, return the top-K chunks
3. **A scoring function:** compare retrieved chunks and/or generated answers against ground truth

### Step 1: Create A Synthetic Dataset

For a new document corpus with no existing Q&A pairs:

- Sample 20–50 representative chunks
- For each chunk, use an LLM to generate 2–3 questions that the chunk answers
- Record the question, the expected chunk ID, and the expected answer text

This is fast to generate and gives you enough signal to detect regressions.

### Step 2: Run Retrieval And Score

For each question in the dataset:

1. Embed the question
2. Retrieve top-K chunks
3. Check whether the expected chunk appears in the top-K
4. Record the rank of the first relevant chunk

Compute Recall@K and MRR across all questions.

### Step 3: Gate Changes On Metrics

Before deploying a change to chunking strategy, embedding model, or index parameters:

- Run the evaluation loop against the existing system (baseline)
- Apply the change
- Run the evaluation loop again
- Deploy only if Recall@K and MRR do not regress

This is the same logic as a test suite: metrics are your regression tests for retrieval quality.

## Metrics In Context: What To Watch

| Signal | What It Tells You |
|--------|------------------|
| Recall@K drops | Recent change broke retrieval — check chunking, embedding model, or index |
| Precision@K drops with Recall@K stable | More noise in results — consider tighter metadata filters or lower K |
| MRR drops | Relevant results are ranked lower — check query expansion or re-ranker |
| Groundedness drops | Model is ignoring retrieved context — check prompt, context position, or model version |
| Answer correctness drops with Recall@K stable | Retrieval is working but generation is failing — prompt or model issue |

## Production Implementation

The patterns described here are implemented end-to-end in [rag-evaluation-lab](https://github.com/ramirez-ai-labs/rag-evaluation-lab):

- synthetic dataset generation
- offline Recall@k and MRR computation
- LLM-as-judge groundedness scoring
- CI integration for regression detection

That repo is the production reference for everything described here.

## Suggested Next Steps

1. Read `RAGArchitecture.md` to understand the system these metrics are measuring.
2. Look at the `rag-evaluation-lab` repo to see how the eval loop is implemented in Python.
3. Pick one metric — Recall@5 — and instrument it against a small document set you own.
4. Once you have a baseline, change one parameter (chunk size, K, embedding model) and observe how the metric moves.
