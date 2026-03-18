# 05 - Mini Projects

This folder should contain small end-to-end projects that combine ideas from the earlier sections.

A good mini-project in this repo should:

- reinforce one or two core CS ideas
- connect clearly to AI engineering work
- stay small enough for a learner to understand in one sitting
- avoid heavy scaffolding unless the scaffolding itself is part of the lesson

## Project Ideas

### 1. Search Service With Caching

Concepts:

- hash maps
- caching
- API design
- latency reduction

What a learner should build:

- a small search endpoint or command-line service
- a cache for repeated queries
- simple request and response logging

### 2. Rate-Limited API Wrapper

Concepts:

- queues
- rate limiting
- retries
- external service protection

What a learner should build:

- a wrapper around a mock AI endpoint
- a limiter that blocks or delays extra requests
- clear output showing allowed vs rejected requests

### 3. Simple Vector Search Demo

Concepts:

- arrays
- cosine similarity or dot product
- ranking
- retrieval intuition

What a learner should build:

- a tiny in-memory vector index
- a query vector
- ranked nearest results

## Project Quality Bar

Mini-projects in this repo should include:

- a problem statement
- setup and run instructions
- a short architecture explanation
- beginner-readable code
- comments on what is intentionally simplified

## What To Avoid

- oversized frameworks for a tiny concept
- fake complexity that hides the main lesson
- production claims for demo-only code
- projects with no explanation of why they matter
