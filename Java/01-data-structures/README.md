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
| `Ex01ArrayExamples.java` | indexing, iteration, copying, and 2D arrays | arrays are the basis of vectors, tensors, batches, and token sequences |
| `Ex02HashMapExamples.java` | key-value storage, lookup, iteration, frequency counting | hash maps are used in vocabularies, feature maps, caches, and counters |
| `Ex03LinkedListDemo.java` | nodes, references, traversal, head insertion, removal, reversal, slow/fast pointers | linked lists teach pointer-style thinking and tradeoffs vs arrays |
| `Ex04TreeBasics.java` | binary tree structure, in-/pre-/post-order and level-order traversal, height, BST insert and lookup | trees build recursive reasoning used in many CS topics |
| `Ex05GraphDFSAndBFS.java` | adjacency lists, iterative and recursive DFS, BFS, BFS shortest path, visited tracking | graphs model relationships, pipelines, dependencies, and search spaces |
| `*Test.java`, `DataStructuresTests.java`, `DataStructuresCheck.java` | tests for the linked list, tree, and graph files | lets you check a from-scratch rewrite (see [Testing](#testing)) |

## Suggested Order

Work through the files in this order:

1. `Ex01ArrayExamples.java`
2. `Ex02HashMapExamples.java`
3. `Ex03LinkedListDemo.java`
4. `Ex04TreeBasics.java`
5. `Ex05GraphDFSAndBFS.java`

This order starts with the most concrete structure and gradually moves toward more recursive and graph-based thinking.

## How To Run

From this folder:

```bash
javac *.java
java Ex01ArrayExamples
java Ex02HashMapExamples
java Ex03LinkedListDemo
java Ex04TreeBasics
java Ex05GraphDFSAndBFS
java DataStructuresTests
```

## Testing

`java DataStructuresTests` runs every test in this folder. Each `*Test.java` file also runs on its own, for example `java Ex03LinkedListDemoTest`. A failure prints `FAIL <name>: expected <...> but was <...>`, and the run exits with status 1.

The tests need no JUnit download. `DataStructuresCheck.java` is a small assertion helper. Each chapter folder has its own copy with a chapter-specific name (`AlgorithmsCheck`, `SystemsCheck`, `MiniProjectsCheck`), and its own runner (`AlgorithmsTests`, ...). Every folder still compiles with `javac *.java`, and the names never clash when an IDE opens `Java/` as one project. A real project would use JUnit and packages.

To re-implement a method from scratch:

1. Delete the body of a method, for example `reverse` in `Ex03LinkedListDemo.java`, and leave the signature and Javadoc.
2. Write it again from memory.
3. Run `javac *.java && java Ex03LinkedListDemoTest`.
4. Restore the original with `git checkout -- Ex03LinkedListDemo.java` when you are done.

You know a topic when your version passes on the first run and you can state its Big-O and where it appears in an AI system without notes.

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
- when reversing, save `curr.next` before overwriting it, or the rest of the list is lost
- slow and fast pointers find the middle in one pass, and the same trick detects cycles

### Trees

- recursive structures are easier to understand when you draw them
- inorder traversal gives sorted output for a binary search tree
- BST search is only fast when the tree stays reasonably balanced
- inserting sorted values builds a chain of height n, which is why `TreeMap` is a self-balancing red-black tree
- post-order visits children before their parent, which is how `height` works
- level-order traversal is graph BFS on a tree, with no visited set because trees have no cycles
- "tree" and "BST" are not the same thing

### Graphs

- graphs generalize trees because nodes can have many connections
- DFS explores deeply first, BFS explores level by level
- visited tracking is required to avoid repeated work or infinite loops
- adjacency lists are usually the simplest representation for sparse graphs
- the iterative DFS prints `[0, 2, 3, 1]` and the recursive one prints `[0, 1, 3, 2]` on the same graph; both are valid, because the stack pops the last neighbor pushed
- recursive DFS is shorter, but a deep graph throws `StackOverflowError`
- BFS gives the shortest path by edge count, but only in unweighted graphs

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

- `Ex01ArrayExamples.java`: compute the sum and average of the array
- `Ex02HashMapExamples.java`: print the most frequent token
- `Ex03LinkedListDemo.java`: add an `insertAtTail` method, then detect a cycle with slow and fast pointers
- `Ex04TreeBasics.java`: return level-order as a list of levels (`[[4], [2, 6], [1, 3, 5]]`), then write `isValidBST`
- `Ex05GraphDFSAndBFS.java`: change `dfs` so it prints the same order as `dfsRecursive`

## Exit Criteria

You are ready to move on when you can:

- explain the difference between arrays, maps, trees, and graphs
- describe the key time complexity for common operations
- trace the code manually without guessing
- modify a sample file without breaking it
