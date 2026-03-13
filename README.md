# AI Engineer CS Refresh (Canonical Repo)

This repo is the **canonical source of truth** for the AI Engineer CS Refresh curriculum.

- If you’re studying: start at `01-data-structures/` and go in order.
- If you’re teaching (or adapting): keep this repo clean and stable, then adapt it in a separate teaching repo.

## What’s inside (Java-first)

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

Everything is written with **Java** as the default language.

- **`01-data-structures/`**  
  - Goal: refresh core structures you actually use in AI work.  
  - Java examples: `ArrayExamples.java`, `HashMapExamples.java`, `LinkedListDemo.java`, `TreeBasics.java`, `GraphDFSAndBFS.java`.
- **`02-algorithms/`**  
  - Goal: light algorithm refresh, not competitive programming.  
  - Focus topics: recursion, sorting, searching, Big‑O.  
  - Java examples: `BinarySearch.java`, `MergeSort.java`, `TwoSum.java`, `TimeComplexityNotes.java`.
- **`03-systems/`**  
  - Goal: connect CS theory → real systems you build with AI.  
  - Topics: distributed systems, caching, load balancing, queues, microservices, API design.  
  - Java examples: `InMemoryCache.java`, `RateLimiter.java`, `SimpleQueueDemo.java`, `RestApiDesignNotes.md`.
- **`04-ai-foundations/`**  
  - Goal: explain the AI concepts students and engineers actually ask about.  
  - Topics: gradient descent, backpropagation, embeddings, vector search, transformers, RAG.  
  - Java examples: small demos using pseudocode or Java-style math utilities, plus links to Python notebooks where needed.
- **`05-mini-projects/`**  
  - Goal: tie everything together with small, realistic builds.  
  - Examples: “search service with caching,” “simple vector search service,” “rate-limited API wrapper.”
- **`resources/`**  
  - Cheatsheets, diagrams, reading lists, and study plans.  
  - Example files: `cs_refresh_checklist.md`, `systems_reading_list.md`, `ai_foundations_reading_list.md`.

## How to use this repo (beginners, in Java)

**Step 1 – Algorithms & Data Structures (light refresh)**  
- Go to `01-data-structures/` and `02-algorithms/`.  
- Work through a **small set** of Java files, not hundreds of problems.  
- Aim to understand and re‑implement, at minimum:
  - Arrays and hash maps
  - Linked lists, trees, graphs
  - Recursion
  - Sorting and searching
  - Time complexity (Big‑O)
- Suggested classic problems to mirror in Java:
  - Two Sum  
  - Binary Search  
  - Reverse Linked List  
  - DFS / BFS graph traversal  
  - Merge Sort  
- Use sites like LeetCode or HackerRank as **supplements**, not the main course.

**Step 2 – Systems & Architecture (your strength)**  
- Go to `03-systems/`.  
- Map concepts you already know into small Java examples:
  - Distributed systems and microservices
  - Caching and load balancing
  - Queues and messaging
  - API design and rate limiting  
- Recommended book: *Designing Data‑Intensive Applications* (Kleppmann). Use this folder to turn those ideas into tiny Java demos.

**Step 3 – Core Programming Refresher (in Java)**  
- Treat this repo as your “CS refresher repo” instead of random practice.  
- Organize Java files like:

```text
01-data-structures/
  ├─ LinkedListDemo.java
  ├─ BinaryTreeDemo.java
  └─ GraphDfsBfsDemo.java
02-algorithms/
  ├─ MergeSort.java
  ├─ BinarySearch.java
  └─ DynamicProgrammingExamples.java
03-systems/
  ├─ SimpleCache.java
  └─ RateLimiter.java
```

- You do **not** need C++ unless you deliberately want it; Java is the primary language here.

**Step 4 – AI Foundations (most important for this course)**  
- Go to `04-ai-foundations/`.  
- Focus on the concepts you’ll explain to non‑experts:
  - Gradient descent and backpropagation (intuitive explanation first, math second)
  - Embeddings and vector search
  - Transformers (high‑level view of attention and tokens)
  - RAG pipelines (how LLMs + retrieval work together)
- Recommended book: *Hands‑On Machine Learning with Scikit‑Learn, Keras, and TensorFlow* (Géron).  
- Use this repo to hold:
  - Java‑style pseudocode for the ideas  
  - Simple Java services that **use** AI systems (e.g., call an API, wrap a vector DB)  
  - Links out to Python notebooks when the ecosystem is Python‑first.

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

- Add a short `README.md` inside each chapter folder explaining what to learn and which Java files to run.
- Add a simple checklist in `resources/` (study plan + prerequisites).
- Create minimal Java stubs (empty classes + `main` methods) for each example listed above, so beginners can open, run, and then gradually fill them in.