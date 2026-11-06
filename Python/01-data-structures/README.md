# 01 - Data Structures

How information is stored, and which operations each shape makes fast or slow. Later AI work builds on these directly: lists become embedding vectors and batches, dicts become vocabularies and caches, and graphs become workflows and knowledge graphs.

## Files In This Folder

Work through the files in this order, from the most concrete structure to the most connected.

| File | What It Teaches | Why It Matters |
|------|------------------|----------------|
| `array_examples.py` | indexing, iteration, copying, 2D lists | the basis of vectors, batches, and token sequences |
| `hash_map_examples.py` | key-value storage, lookup, iteration, frequency counting | used in vocabularies, feature maps, caches, and counters |
| `linked_list_demo.py` | nodes, references, traversal, head insertion | teaches reference-based thinking and the tradeoffs against arrays |
| `tree_basics.py` | binary tree structure, traversals, BST search | builds recursive reasoning |
| `graph_dfs_and_bfs.py` | adjacency lists, DFS, BFS, visited tracking | models relationships, pipelines, and search spaces |

## How To Run

From this folder:

```bash
python array_examples.py
python hash_map_examples.py
python linked_list_demo.py
python tree_basics.py
python graph_dfs_and_bfs.py
```

## What To Pay Attention To

- **Lists:** index lookup is O(1). A Python `list` grows automatically, unlike a Java array.
- **Copying:** `b = a` does not copy a list. Both names refer to the same list, so `array_examples.py` builds a new one.
- **Dicts:** average lookup is O(1). `d[key]` raises `KeyError` for a missing key, while `d.get(key)` returns `None`.
- **Linked lists:** inserting at the head is cheap, but reaching a position means walking the list.
- **Trees:** BST search is only O(log n) when the tree stays reasonably balanced.
- **Graphs:** without visited tracking, a cycle makes traversal repeat forever.

## Beginner Mistakes To Avoid

- confusing a list index with the value stored there
- using `[[0] * 3] * 3` and getting three references to the same row
- assuming all trees are balanced
- forgetting to track visited nodes in a graph

## Practice Prompts

- `array_examples.py`: compute the sum and average of the list
- `hash_map_examples.py`: find the most frequent token with a loop
- `linked_list_demo.py`: add an `insert_at_tail` function
- `tree_basics.py`: add a postorder traversal
- `graph_dfs_and_bfs.py`: run both traversals from a different starting node

## Exit Criteria

Move on when you can describe the key time complexities, trace the code by hand, and change a file without breaking it.

The [Java chapter README](../../Java/01-data-structures/README.md) has a longer walkthrough of how these structures connect to AI engineering.
