# 04 - AI Foundations

This folder is the bridge between classic CS refresh material and modern AI engineering.

This chapter makes the progression explicit:

- data structures explain how vectors, tokens, and metadata are represented
- algorithms explain retrieval, ranking, and search behavior
- systems thinking explains latency, cost, and reliability constraints
- AI foundations explain what the models and retrieval layers are actually doing

The goal is not to turn learners into research scientists. The goal is to give them enough conceptual grounding to understand what AI systems are doing, how to talk about them correctly, and how to make sound implementation choices.

## Progression To AI Engineering

This folder answers the question, "Now that I understand core CS mechanics, how do those mechanics show up inside real AI workflows?"

Examples:

- arrays and vector math lead into embeddings
- similarity scoring leads into retrieval
- systems tradeoffs lead into RAG design decisions
- measurement and feedback lead into evaluation and observability

## Core Topics This Folder Should Cover

- linear algebra refresh for vectors and matrices
- probability and statistics basics for model thinking
- optimization basics such as gradient descent
- embeddings and vector similarity
- transformer intuition
- retrieval-augmented generation (RAG)
- evaluation, observability, and failure analysis

## What Learners Should Get From This Section

By the end of this section, a learner should be able to:

- explain what an embedding is without hand-waving
- describe why vector search is useful
- explain the difference between model training and model inference
- explain RAG as a system design pattern, not as magic
- reason about accuracy, latency, cost, and evaluation tradeoffs

## Recommended Content Style

Material in this folder should prefer:

- intuitive explanation first
- equations only when they help
- diagrams, worked examples, and small demos
- links to Python notebooks when the tooling is naturally Python-first
- explicit connection to real AI products and platforms

## Gaps To Fill

Compared with the earlier folders, this section still needs:

- concrete examples
- deeper written walkthroughs
- runnable demos or notebooks
- references to evaluation and observability workflows

## Current Material

| File | What It Teaches | Why It Matters |
|------|------------------|----------------|
| `EmbeddingsAndVectorSearch.md` | embeddings, cosine similarity, retrieval flow, RAG connection, failure modes | gives learners the conceptual base they need before using vector databases or building retrieval pipelines |
| `TransformerIntuition.md` | attention as weighted lookup, Q/K/V framing, context window constraints, KV caching connection | demystifies what the model is doing well enough to make sound decisions about chunking, prompts, and cost |
| `RAGArchitecture.md` | full ingestion → retrieval → generation pipeline, failure modes at each stage, how chapter 03 systems concepts plug in | gives learners a complete system picture, not just the retrieval layer |
| `EvaluationMetrics.md` | Recall@k, Precision@k, MRR, groundedness, how to build an offline eval loop | teaches learners to measure retrieval and generation quality instead of guessing |

If code is added here, choose the language that best teaches the idea. Java is fine for simple conceptual demos. Python is often the better fit for matrix math, notebooks, or ML ecosystem examples.
