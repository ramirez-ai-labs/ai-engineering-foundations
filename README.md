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

Work through the folders in order:

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
2. Compile and run each Java example.
3. Read the code line by line and trace the output.
4. Re-implement the example without looking.
5. Explain how the concept shows up in AI engineering.

## Repository Structure

```text
/
├── README.md
├── 01-data-structures/
├── 02-algorithms/
├── 03-systems/
├── 04-ai-foundations/
├── 05-mini-projects/
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

Java is the default language for the CS examples in this repository because:

- it is explicit and beginner-friendly for core data structures
- it forces learners to see types and structure clearly
- it maps well to backend and systems thinking

Python may be used later for AI-first topics when the ecosystem makes it the better teaching tool.

## How To Run The Code

Each numbered folder contains small Java files with a `main` method.

Example:

```bash
cd 01-data-structures
javac *.java
java ArrayExamples
```

You can do the same in `02-algorithms/` and `03-systems/`.

There is also a first runnable mini-project in `05-mini-projects/`:

```bash
cd 05-mini-projects
javac SimpleVectorSearchDemo.java
java SimpleVectorSearchDemo
```

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

Today, the strongest material is in:

- `01-data-structures/`
- `02-algorithms/`
- `03-systems/`
- `04-ai-foundations/EmbeddingsAndVectorSearch.md`
- `05-mini-projects/SimpleVectorSearchDemo.java`

The `04-ai-foundations/` and `05-mini-projects/` folders are now seeded with an initial embeddings/vector-search note and a first runnable retrieval demo, but they still need additional material to match the breadth of the earlier folders.

## Contribution Expectations

If you update this repo:

- keep the numbered curriculum structure intact
- prefer teaching-quality examples over shortcuts
- add enough explanation for a beginner to follow the code
- avoid overstating what an example implements
- keep the repo stable and reusable for self-study

## Next Step

If you are new to this repo, start with `01-data-structures/README.md`.
