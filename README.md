# AI CS Refresher

Canonical curriculum repo for your AI engineering CS refresher and AI literacy cohort support.

## Repository Role

This repository is the **single source of truth**.

- All new content starts here.
- Dev/Mission receives a copied/adapted version from this repo.
- Do not make direct canonical-only updates in the downstream copy.

## Start Here

1. Read this README for scope and workflow.
2. Start in `01-data-structures`, then continue in order through `05-mini-projects`.
3. Use each folder `README.md` as the topic checklist.
4. Pull deep-dive material only when needed from the related repos below.

## Structure

```text
/
├── README.md
├── 01-data-structures/
├── 02-algorithms/
├── 03-systems/
├── 04-ai-foundations/
├── 05-mini-projects/
├── resources/
├── talks/
├── workshop-notes/
└── blog-assets/
```

## Folder Intent

- `01-data-structures`: Arrays, hash maps, trees, graphs, heaps, tries.
- `02-algorithms`: Sorting, searching, recursion, dynamic programming, graph algorithms.
- `03-systems`: Networking, OS/process basics, concurrency, databases, distributed systems.
- `04-ai-foundations`: Math for ML, model basics, embeddings, retrieval, evaluation, AI systems design.
- `05-mini-projects`: Small build projects that integrate concepts across folders.
- `resources`: Reference links, cheat sheets, books, papers.
- `talks`: Slide decks, speaking notes, live session artifacts.
- `workshop-notes`: Facilitator notes and workshop runbooks.
- `blog-assets`: Images/diagrams and writing support files.

## Content Conventions

- Prefer Markdown for notes and explanations.
- Keep one topic per file when possible.
- Use consistent file naming: `YYYY-MM-DD-topic-slug.md` for logs/journals, `topic-slug.md` for evergreen notes.
- Add lightweight exercises under each topic folder.

## Downstream Copy Workflow (Dev/Mission)

1. Build and review content here first.
2. Copy selected folders/files to Dev/Mission.
3. Apply teaching-specific adaptations only in Dev/Mission (prompts, cohort scaffolding, instructor notes).
4. If a downstream improvement should become canonical, port it back here via PR/commit before next copy cycle.

## Current Build Status

1. Core folder structure is in place.
2. Starter `README.md` files are in place for each learning area.
3. Next planned addition: `curriculum-map.md` with weekly sequencing.

## Related Deep-Dive Repos

Use these when a topic outgrows refresher scope:

- [`rag-evaluation-lab`](https://github.com/ramirez-ai-labs/rag-evaluation-lab) for deeper RAG evaluation workflows.
- [`ai-architecture-showcase`](https://github.com/ramirez-ai-labs/ai-architecture-showcase) for advanced architecture patterns.
- [`edge-ai-agent-lab`](https://github.com/ramirez-ai-labs/edge-ai-agent-lab) for edge-native agent systems.
- [`OpenAI-Tutorials`](https://github.com/ramirez-ai-labs/OpenAI-Tutorials) for API tutorial-style implementations.
