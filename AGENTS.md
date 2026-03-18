# AGENTS.md

## Purpose

This repository is the canonical source of truth for the **AI Engineer CS Refresh** curriculum. It should be treated like a clean, reusable reference repo rather than a one-off demo app.

Primary goals:

- Keep content technically correct, practical, and easy to study.
- Preserve a stable structure across numbered topic folders.
- Prefer small, readable examples over framework-heavy implementations.
- Connect computer science fundamentals to modern AI engineering work.

## Repo Shape

The repository is organized as a textbook-style curriculum:

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

Interpretation:

- `01-data-structures/` and `02-algorithms/`: concise CS refresh material.
- `03-systems/`: production systems concepts tied to AI platforms and backend architecture.
- `04-ai-foundations/`: practical ML and LLM foundations explained clearly.
- `05-mini-projects/`: realistic integration projects that combine systems and AI concepts.
- `resources/`: checklists, study aids, diagrams, and supporting references.

## Collaborator Profile

Assume the repository owner is:

- an experienced software engineer
- an AI architect
- a data scientist
- a DevOps practitioner
- a security professional

Implications for how you should assist:

- Do not explain basic engineering concepts unless asked.
- Prefer precise, technically rigorous recommendations.
- Surface tradeoffs, failure modes, and operational concerns.
- Treat security, deployment, and maintainability as first-class requirements.
- Write for an expert audience while keeping material teachable for learners.

## Working Style

When contributing to this repo:

- Be concise, direct, and technically grounded.
- Prefer implementation over vague planning when the next step is clear.
- Keep examples small enough to teach from, but realistic enough to matter.
- Preserve existing folder numbering and naming conventions.
- Avoid turning the repo into a generic web app starter unless explicitly requested.

## Content Standards

### General

- Favor clarity over cleverness.
- Use descriptive names for files, classes, methods, and variables.
- Keep examples focused on one concept at a time.
- Add short explanatory comments only where they improve comprehension.
- Document public-facing functions, classes, and instructional files when useful.

### Language Preferences

- Default language for CS examples is **Java** unless a different language is clearly more appropriate.
- Use **Python** when the topic is naturally ML/AI-first, notebook-oriented, or ecosystem-constrained.
- Use **TypeScript** for frontend or typed scripting work when needed.
- If introducing a new language, explain why it is the right choice for that folder or example.

### Educational Bias

- Optimize for teaching core ideas, not showing off abstractions.
- Prefer worked examples, runnable snippets, and short demos.
- Tie theory to real AI engineering scenarios where possible.
- Keep explanations reusable for self-study and teaching.

## Engineering Standards

### Code Quality

- Keep code clean, minimal, and production-aware.
- Follow idiomatic style for the language being used.
- Include type hints in Python.
- Keep Java examples straightforward and easy to compile/run locally.
- Avoid unnecessary dependencies.

### Testing

- Add tests for non-trivial logic and mini-project code.
- Cover both expected behavior and failure cases where practical.
- Do not add heavyweight test scaffolding for tiny teaching examples unless it materially improves the repo.

### Security

- Never commit secrets, credentials, or tokens.
- Use environment variables for configuration.
- Validate inputs in apps, APIs, and scripts.
- Prefer secure defaults for networked examples.
- Flag security risks explicitly when proposing architecture or code changes.

### Operations

- Consider observability, deployment, reproducibility, and failure handling in project-style code.
- Prefer simple local run instructions.
- Use Docker, CI, or deployment config only when it adds real value to the repo.

## Contribution Rules

- Keep the root README and folder READMEs aligned with the actual repo contents.
- Do not add placeholders that imply files or features exist when they do not.
- When creating new files, place them in the numbered folder that best matches the topic.
- When adding mini-projects, keep scope tight and clearly document the concept being demonstrated.
- Favor canonical, reusable material over cohort-specific or one-off teaching content.

## Documentation Expectations

New or updated content should usually include:

- a short explanation of the concept or objective
- how to run or use the example
- why the example matters for AI engineering
- any important limitations, assumptions, or next steps

## What To Avoid

- Generic portfolio boilerplate that does not match this repo
- overengineered project structure for simple examples
- hidden assumptions about frontend/backend app architecture
- unnecessary frameworks or dependencies
- low-signal filler content
- beginner-level overexplanation aimed at the repo owner

## Agent Instruction Summary

If you are an AI agent working in this repository:

- Treat this repo as a high-quality reference curriculum.
- Collaborate with the owner as a senior peer.
- Make technically strong, security-aware, and operationally credible choices.
- Keep changes aligned with the repo's educational mission and current structure.
- Prefer concrete improvements that increase clarity, correctness, and reuse.
