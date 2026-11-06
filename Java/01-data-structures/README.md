# 01 - Data Structures

This folder introduces the core data structures that show up repeatedly in software engineering and AI systems.

This chapter sits at the base of the repo's progression:

- refresh how data is stored and accessed
- build intuition for runtime and representation tradeoffs
- prepare learners to understand how AI systems hold tokens, vectors, metadata, and graph-shaped relationships

The goal is not to memorize definitions. The goal is to understand:

- how each structure stores data
- what operations are fast or slow
- what tradeoffs you make when choosing one structure over another
- where the structure appears in real AI engineering work

## Progression To AI Engineering

This folder answers two foundational questions:

1. how is information represented in memory?
2. what storage shape best fits the operation you need?

Those questions directly enable later AI work:

- arrays lead into embeddings, tensors, token buffers, and batches
- hash maps lead into vocabularies, indexes, caches, and metadata joins
- trees lead into recursive reasoning, parsing, and search structures
- graphs lead into workflows, dependency tracking, retrieval flows, and knowledge graphs

## Why Data Structures Matter Later In AI Engineering

Most AI systems are pipelines that move and transform data. Before a learner works on vector databases, RAG pipelines, model serving, or agent orchestration, they need to understand the shapes and containers that make those systems possible.

Examples:

- arrays become token sequences, embedding vectors, and mini-batches
- hash maps become vocabularies, cache indexes, feature maps, and metadata stores
- trees help with recursive reasoning, parsing, and hierarchical state
- graphs show up in workflows, dependency tracking, knowledge graphs, and traversal-heavy retrieval tasks

If a learner is weak on these basics, later AI topics often feel magical. If the learner is solid here, later systems feel like combinations of familiar parts.

## Files In This Folder

| File | What It Teaches | Why It Matters |
|------|------------------|----------------|
| `ArrayExamples.java` | indexing, iteration, copying, and 2D arrays | arrays are the basis of vectors, tensors, batches, and token sequences |
| `HashMapExamples.java` | key-value storage, lookup, iteration, frequency counting | hash maps are used in vocabularies, feature maps, caches, and counters |
| `LinkedListDemo.java` | nodes, references, traversal, head insertion | linked lists teach pointer-style thinking and tradeoffs vs arrays |
| `TreeBasics.java` | binary tree structure, traversals, BST lookup | trees build recursive reasoning used in many CS topics |
| `GraphDFSAndBFS.java` | adjacency lists, DFS, BFS, visited tracking | graphs model relationships, pipelines, dependencies, and search spaces |

## Suggested Order

Work through the files in this order:

1. `ArrayExamples.java`
2. `HashMapExamples.java`
3. `LinkedListDemo.java`
4. `TreeBasics.java`
5. `GraphDFSAndBFS.java`

This order starts with the most concrete structure and gradually moves toward more recursive and graph-based thinking.

## How To Run

From this folder:

```bash
javac *.java
java ArrayExamples
java HashMapExamples
java LinkedListDemo
java TreeBasics
java GraphDFSAndBFS
```

## What To Pay Attention To

### Arrays

- why index lookup is O(1)
- why array size is fixed in Java
- why copying matters when you do not want to mutate shared data
- how a 2D array can represent a batch of vectors

### Hash Maps

- why average-case lookup is O(1)
- why missing keys return `null`
- when `getOrDefault` is safer than `get`
- why frequency counting is a common pattern

### Linked Lists

- each node stores a value and a reference to the next node
- inserting at the head is cheap
- random access is slow because you must walk the list
- the structure is simple, but traversal logic is easy to get wrong without care

### Trees

- recursive structures are easier to understand when you draw them
- inorder traversal gives sorted output for a binary search tree
- BST search is only fast when the tree stays reasonably balanced
- "tree" and "BST" are not the same thing

### Graphs

- graphs generalize trees because nodes can have many connections
- DFS explores deeply first, BFS explores level by level
- visited tracking is required to avoid repeated work or infinite loops
- adjacency lists are usually the simplest representation for sparse graphs

## Beginner Mistakes To Avoid

- confusing array index with array value
- assuming a hash map keeps insertion order
- thinking linked lists support fast indexing like arrays
- assuming all trees are balanced
- forgetting to track visited nodes in graph traversal

## AI Engineering Connections

- arrays: embeddings, token IDs, feature vectors
- hash maps: vocabularies, request caches, lookup tables
- linked lists: useful mostly for conceptual grounding and interview-style reasoning
- trees: recursive thinking, parse structures, search logic
- graphs: retrieval flows, dependency graphs, knowledge graphs, workflow orchestration

## How These Structures Become Building Blocks

This folder is the first layer of several later AI engineering concepts:

- embeddings and vector search depend on strong intuition for arrays and matrix-like structures
- tokenization pipelines rely on mapping between strings, token IDs, and metadata, which is map-heavy work
- document and chunk indexes depend on key-based lookup and graph-like relationships between artifacts
- agent systems often maintain graph-shaped tool flows or tree-shaped decision paths
- retrieval systems often combine arrays for numeric data and maps for metadata joins

A useful mental model is:

- data structures answer "how is the information stored?"
- algorithms answer "how do we operate on it?"
- systems answer "how do we serve it reliably at scale?"

## Practice Prompts

After reading each file, try one or two small extensions:

- `ArrayExamples.java`: compute the sum and average of the array
- `HashMapExamples.java`: print the most frequent token
- `LinkedListDemo.java`: add an `insertAtTail` method
- `TreeBasics.java`: add a postorder traversal
- `GraphDFSAndBFS.java`: test traversal from a different starting node

## Exit Criteria

You are ready to move on when you can:

- explain the difference between arrays, maps, trees, and graphs
- describe the key time complexity for common operations
- trace the code manually without guessing
- modify a sample file without breaking it
