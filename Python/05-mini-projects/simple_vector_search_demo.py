"""Small end-to-end vector search demo.

This is intentionally tiny:
- embeddings are hand-written tuples
- every document is scanned linearly
- cosine similarity is used for ranking

In a real AI system you would usually generate embeddings with a model,
store them externally, add metadata filters, and use an ANN index.
"""

import math


class DocumentVector:
    def __init__(self, id: str, text: str, embedding: tuple[float, ...]) -> None:
        self.id = id
        self.text = text
        self.embedding = embedding


class SearchResult:
    def __init__(self, id: str, text: str, score: float) -> None:
        self.id = id
        self.text = text
        self.score = score


def validate_same_dimensions(left: tuple[float, ...], right: tuple[float, ...]) -> None:
    if len(left) != len(right):
        raise ValueError("embedding dimensions must match")


def dot_product(left: tuple[float, ...], right: tuple[float, ...]) -> float:
    """Multiply matching positions together and add up the results."""
    total = 0.0
    for index in range(len(left)):
        total += left[index] * right[index]
    return total


def vector_length(vector: tuple[float, ...]) -> float:
    """Euclidean length (norm): the square root of the sum of squared values."""
    total = 0.0
    for value in vector:
        total += value * value
    return math.sqrt(total)


def cosine_similarity(left: tuple[float, ...], right: tuple[float, ...]) -> float:
    validate_same_dimensions(left, right)

    left_length = vector_length(left)
    right_length = vector_length(right)

    if left_length == 0.0 or right_length == 0.0:
        raise ValueError("cosine similarity is undefined for a zero vector")

    return dot_product(left, right) / (left_length * right_length)


def search(
    query_embedding: tuple[float, ...],
    documents: list[DocumentVector],
    top_k: int,
) -> list[SearchResult]:
    """Exact top-k search by cosine similarity, highest score first.

    Keeps a list of the best results so far, sorted by score. Each document
    costs at most k steps, so the whole search is O(n * k).

    Python shortcut: heapq.nlargest(top_k, results, key=lambda r: r.score),
    which is O(n log k) and better for large k.
    """
    if top_k <= 0:
        raise ValueError("top_k must be greater than 0")

    best_results: list[SearchResult] = []
    for doc in documents:
        score = cosine_similarity(query_embedding, doc.embedding)

        # Walk past every result that scores at least as high as this one.
        # Using >= means tied documents keep their original order.
        position = 0
        while position < len(best_results) and best_results[position].score >= score:
            position += 1
        best_results.insert(position, SearchResult(doc.id, doc.text, score))

        # Drop the lowest score once we are holding more than top_k results.
        if len(best_results) > top_k:
            best_results.pop()
    return best_results


def main() -> None:
    documents = [
        DocumentVector(
            "doc-1",
            "Redis caching reduces repeated inference latency.",
            (0.92, 0.10, 0.18),
        ),
        DocumentVector(
            "doc-2",
            "Vector retrieval helps a RAG system find relevant chunks.",
            (0.88, 0.15, 0.30),
        ),
        DocumentVector(
            "doc-3",
            "Rate limiting protects expensive AI endpoints from overload.",
            (0.25, 0.92, 0.28),
        ),
        DocumentVector(
            "doc-4",
            "Audit logs and policy controls matter in secure AI platforms.",
            (0.14, 0.20, 0.95),
        ),
    ]

    # Query vector is intentionally closest to retrieval/RAG content.
    query_embedding = (0.90, 0.12, 0.35)
    top_results = search(query_embedding, documents, top_k=3)

    print("Top search results for query embedding:")
    for result in top_results:
        print(f"{result.id} | score={result.score:.4f} | {result.text}")

    print()
    print("What this demo teaches:")
    print("- embeddings are arrays of numbers")
    print("- cosine similarity produces a ranking score")
    print("- top-k retrieval returns the nearest items")
    print("- this demo uses exact scan, not an ANN index")


if __name__ == "__main__":
    main()
