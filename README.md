# AI Engineer CS Refresh (Canonical Repo)

This repo is the **canonical source of truth** for the AI Engineer CS Refresh curriculum.

- If you’re studying: start at `01-data-structures/` and go in order.
- If you’re teaching (or adapting): keep this repo clean and stable, then adapt it in a separate teaching repo.

## What’s inside

Think of this repo like a **textbook**. Each numbered folder is a chapter.

```text
/
├── README.md
├── 01-data-structures/
├── 02-algorithms/
├── 03-systems/
├── 04-ai-foundations/
├── 05-mini-projects/
└── resources/
```

- **`01-data-structures/`**: Arrays, stacks, queues, hash maps, trees, graphs
- **`02-algorithms/`**: Sorting, searching, recursion, dynamic programming, graph algorithms
- **`03-systems/`**: Networking, databases, OS basics, distributed systems, scaling
- **`04-ai-foundations/`**: Math basics, ML concepts, training/evaluation, embeddings, LLM basics
- **`05-mini-projects/`**: Small end-to-end projects that combine earlier skills
- **`resources/`**: Cheatsheets, templates, links, setup guides, study advice

## How to use this repo (beginners)

- **Go in order**: start with `01-...`, then `02-...`, etc.
- **Skim first, then do**: read the notes, then try the exercises/projects.
- **Keep your own notes**: add a personal notes file outside this repo, or in your own fork.

## Canonical repo rules (source of truth)

This repo should stay:

- **Stable**: avoid class-specific or cohort-specific changes here.
- **Reusable**: content should make sense to someone learning alone.
- **Consistent**: numbered folders, predictable naming, and clear instructions.

## Teaching/adapted repo guidance (downstream)

If you create a teaching version (example: for a cohort), put teaching-only material there, like:

```text
cohort-exercises/
instructor-notes/
student-prompts/
```

Keep this repo as the **reference** and adapt the teaching repo as needed (pace, deadlines, rubrics, cohort tooling).

## Suggested next steps

- Add a short `README.md` inside each chapter folder explaining what to learn and what to do.
- Add a simple checklist in `resources/` (study plan + prerequisites).