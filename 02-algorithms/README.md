# 02 - Algorithms

This folder is a practical algorithm refresher. It is intentionally small. The point is to understand the patterns you actually reuse, not to turn this repo into a giant interview problem bank.

Focus areas:

- searching
- sorting
- hash-map-based lookup patterns
- time complexity analysis
- recursion and divide-and-conquer thinking

## Files In This Folder

| File | What It Teaches | Why It Matters |
|------|------------------|----------------|
| `BinarySearch.java` | binary search on sorted input | teaches logarithmic search and boundary reasoning |
| `MergeSort.java` | divide-and-conquer sorting | teaches recursion, merging, and O(n log n) analysis |
| `TwoSum.java` | brute force vs hash map optimization | teaches how better data structures improve runtime |
| `TimeComplexityNotes.java` | examples of O(1), O(n), and O(n^2) | teaches how to reason about runtime from code shape |

## Suggested Order

1. `TimeComplexityNotes.java`
2. `BinarySearch.java`
3. `TwoSum.java`
4. `MergeSort.java`

This order helps a beginner learn the runtime vocabulary first, then apply it.

## How To Run

From this folder:

```bash
javac *.java
java TimeComplexityNotes
java BinarySearch
java TwoSum
java MergeSort
```

## What To Pay Attention To

### Binary Search

- the array must already be sorted
- the algorithm throws away half the search space each step
- the loop condition `left <= right` matters
- midpoint calculation should avoid overflow in general code

### Two Sum

- brute force is easy to understand and useful as a baseline
- the hash map version is faster because it trades memory for speed
- storing what you have already seen is a common algorithmic pattern
- returning `[-1, -1]` is a simple way to show "not found"

### Merge Sort

- recursion breaks the problem into smaller subproblems
- merge sort is stable and predictable
- the merge step is where most bugs happen
- the runtime is O(n log n), not O(n^2), because each level does linear work and there are log n levels

### Time Complexity

- count the dominant operations
- constants matter less than growth rate when input gets large
- nested loops often indicate O(n^2), but not always
- runtime and memory are both worth tracking

## Beginner Mistakes To Avoid

- using binary search on unsorted input
- memorizing Big-O labels without understanding the loop behavior
- thinking recursion is magic instead of smaller repeated work
- optimizing too early before understanding the simple solution

## AI Engineering Connections

- binary search: threshold tuning, range selection, and sorted lookup problems
- hash-map patterns: retrieval shortcuts, counters, and indexing logic
- merge-sort thinking: divide large data processing into smaller chunks
- Big-O reasoning: estimating what will or will not scale in pipelines and services

## Practice Prompts

- `BinarySearch.java`: return the insertion position if the target is missing
- `TwoSum.java`: return all unique pairs instead of one pair
- `MergeSort.java`: sort an already sorted array and confirm the output stays correct
- `TimeComplexityNotes.java`: add an O(log n) example method

## Exit Criteria

Move on when you can:

- explain why one algorithm is faster than another
- identify the preconditions for each algorithm
- walk through one full example by hand
- modify the code without introducing off-by-one errors
