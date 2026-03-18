# 04 - AI Foundations

This folder is the bridge between classic CS refresh material and modern AI engineering.

The goal is not to turn learners into research scientists. The goal is to give them enough conceptual grounding to understand what AI systems are doing, how to talk about them correctly, and how to make sound implementation choices.

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

## Suggested File Additions

Examples of good additions for this folder:

- `GradientDescentBasics.md`
- `EmbeddingsAndVectorSearch.md`
- `RAGConcepts.md`
- `TransformerIntuition.md`
- `evaluation-checklist.md`

If code is added here, choose the language that best teaches the idea. Java is fine for simple conceptual demos. Python is often the better fit for matrix math, notebooks, or ML ecosystem examples.
