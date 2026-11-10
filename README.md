# AI Engineer CS Refresh

This repository is a structured refresher for:

- CS students who want stronger fundamentals before building AI systems
- new engineers trying to get started in AI engineering
- experienced engineers who want a compact, practical review of core topics

The repo is organized like a short textbook. Each numbered folder is a chapter in a progression:

1. refresh the core CS foundations
2. show how those foundations reappear in modern AI systems
3. help learners see why AI tools and architectures work the way they do

The emphasis is on small examples, clear explanations, and explicit mapping from foundational CS ideas to the AI systems, platforms, and tools learners will be using and building.

## Core Goal

The ultimate goal of this repo is twofold:

- help CS majors refresh the foundations they need to reason well about software systems
- show the progression from those foundations into real AI engineering workflows

This means the repo should not teach data structures, algorithms, systems, and AI topics as disconnected chapters. It should teach them as a sequence where each layer enables the next:

- arrays and hash maps enable embeddings, metadata lookup, and caching
- algorithms enable ranking, search, retrieval, and scaling decisions
- systems concepts enable reliable APIs, pipelines, queues, and model-serving infrastructure
- AI foundations explain the behavior and tradeoffs of embeddings, retrieval, generation, and evaluation
- mini-projects combine those layers into small, credible AI-engineering exercises

## Who This Repo Is For

This repo is designed for learners who need:

- a practical review of data structures and algorithms
- systems concepts that matter in real AI platforms
- a bridge from classical CS material to embeddings, vector search, and RAG
- examples that are short enough to study but still technically honest

If you want production-scale projects, use this repo as the foundation first. Then move into specialized labs or full applications.

## Learning Path

Pick a language track, `Java/` or `Python/`, then work through its folders in order:

1. `01-data-structures/`
2. `02-algorithms/`
3. `03-systems/`
4. `04-ai-foundations/`
5. `05-mini-projects/`

The intended progression is:

1. learn the core CS concept
2. understand its runtime and design tradeoffs
3. see how it appears inside AI systems
4. apply it in a small integrated example

Recommended approach:

1. Read the folder README first.
2. Run each example.
3. Read the code line by line and trace the output.
4. Re-implement the example without looking.
5. Explain how the concept shows up in AI engineering.

## Repository Structure

```text
/
├── README.md
├── Java/
│   ├── 01-data-structures/
│   ├── 02-algorithms/
│   ├── 03-systems/
│   ├── 04-ai-foundations/
│   └── 05-mini-projects/
├── Python/
│   ├── README.md
│   ├── 01-data-structures/
│   ├── 02-algorithms/
│   ├── 03-systems/
│   ├── 04-ai-foundations/
│   └── 05-mini-projects/
├── resources/
├── blog-assets/
├── talks/
└── workshop-notes/
```

Folder purpose:

- `01-data-structures/`: arrays, hash maps, linked lists, trees, and graphs
- `02-algorithms/`: searching, sorting, problem-solving patterns, and Big-O
- `03-systems/`: caches, queues, rate limiting, and API design basics
- `04-ai-foundations/`: the AI concepts learners need before building LLM systems
- `05-mini-projects/`: integrated practice projects that combine several ideas
- `resources/`: supporting notes, positioning, and study material

## Language Choice

The chapters are available in two languages, so choose the one you want to learn or refresh.

Java is the default language for new CS examples in this repository because:

- it is explicit and beginner-friendly for core data structures
- it forces learners to see types and structure clearly
- it maps well to backend and systems thinking

Python is the better fit for learners heading straight into the AI ecosystem. The Python track deliberately avoids language shortcuts such as comprehensions and `enumerate`, so the underlying logic stays visible. A `Python shortcut:` comment names the built-in version where it helps. See [Python/README.md](Python/README.md).

The written notes in `Java/04-ai-foundations/` and `Java/03-systems/RestApiDesignNotes.md` are mostly language-neutral, so the Python track links to them instead of duplicating them.

## How To Run The Code

### Java

Each numbered folder contains small Java files with a `main` method.

Example:

```bash
cd Java/01-data-structures
javac *.java
java ArrayExamples
```

You can do the same in `Java/02-algorithms/` and `Java/03-systems/`.

There is also a first runnable mini-project in `Java/05-mini-projects/`:

```bash
cd Java/05-mini-projects
javac SimpleVectorSearchDemo.java
java SimpleVectorSearchDemo
```

### Python

Python 3.10 or newer is required, with no packages to install.

```bash
cd Python/01-data-structures
python array_examples.py
```

Every Python file with functions or classes has a `test_<name>.py` beside it. Run a chapter's tests from its folder:

```bash
cd Python/05-mini-projects
python simple_vector_search_demo.py
python -m unittest -v
```

The tests also make step 4 of the recommended approach checkable: delete a function body, rewrite it, and run the tests. See [Python/README.md](Python/README.md#re-implementing-from-scratch).

## What Good Learning Looks Like Here

A learner should be able to answer these questions after each folder:

- What problem does this structure or algorithm solve?
- What is the time complexity?
- What are the tradeoffs?
- Where does this show up in a real AI system?
- What are the common beginner mistakes?

If the code runs but the learner cannot answer those questions, the material is not clear enough yet.

## Design Principles For This Repo

- Keep examples small and runnable.
- Prefer clarity over abstraction.
- Do not hide the core logic behind helper frameworks.
- Use comments to explain why the code works, not to repeat obvious syntax.
- Keep documentation honest about what the code does and does not do.
- Connect each topic back to AI engineering where appropriate.

## Current Scope

In both tracks, the strongest material is in:

- `01-data-structures/`
- `02-algorithms/`
- `03-systems/`
- the vector search and knowledge graph demos in `05-mini-projects/`

`Java/04-ai-foundations/` holds the chapter's notes on embeddings, transformers, RAG architecture, and evaluation metrics.

The two tracks are not identical:

- The Python track has a test suite for every chapter, plus material the Java track does not have yet: linked list reversal and slow/fast pointers, post-order and level-order tree traversal, recursive DFS, BFS shortest path, and a token bucket rate limiter.
- Python's `lru_cache.py` builds LRU from a dict and a doubly linked list. `LRUCache.java` uses `LinkedHashMap` instead.
- The Java vector search demo has a linear-scan benchmark that the Python version does not.

## Contribution Expectations

If you update this repo:

- keep the numbered curriculum structure intact
- prefer teaching-quality examples over shortcuts
- add enough explanation for a beginner to follow the code
- avoid overstating what an example implements
- keep the repo stable and reusable for self-study

## Next Step

If you are new to this repo, start with [Java/01-data-structures/README.md](Java/01-data-structures/README.md) or [Python/01-data-structures/README.md](Python/01-data-structures/README.md).
