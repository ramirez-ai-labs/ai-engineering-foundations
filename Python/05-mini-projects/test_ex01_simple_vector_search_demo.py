"""Tests for ex01_simple_vector_search_demo. Run from this folder: python -m unittest -v"""

import unittest

from ex01_simple_vector_search_demo import DocumentVector, SearchResult, cosine_similarity, search


def result_ids(results: list[SearchResult]) -> list[str]:
    ids: list[str] = []
    for result in results:
        ids.append(result.id)
    return ids


class CosineSimilarityTest(unittest.TestCase):
    def test_identical_vectors_score_one(self) -> None:
        self.assertAlmostEqual(cosine_similarity((1.0, 2.0), (1.0, 2.0)), 1.0)

    def test_orthogonal_vectors_score_zero(self) -> None:
        self.assertAlmostEqual(cosine_similarity((1.0, 0.0), (0.0, 1.0)), 0.0)

    def test_dimension_mismatch_raises(self) -> None:
        with self.assertRaises(ValueError):
            cosine_similarity((1.0, 0.0), (1.0, 0.0, 0.0))

    def test_zero_vector_raises(self) -> None:
        with self.assertRaises(ValueError):
            cosine_similarity((0.0, 0.0), (1.0, 0.0))


class SearchTest(unittest.TestCase):
    docs = [
        DocumentVector("a", "x-axis", (1.0, 0.0)),
        DocumentVector("b", "diagonal", (1.0, 1.0)),
        DocumentVector("c", "y-axis", (0.0, 1.0)),
    ]

    def test_results_ranked_by_score(self) -> None:
        results = search((1.0, 0.1), self.docs, top_k=3)
        self.assertEqual(result_ids(results), ["a", "b", "c"])

    def test_ranks_correctly_when_best_match_comes_last(self) -> None:
        results = search((0.0, 1.0), self.docs, top_k=3)
        self.assertEqual(result_ids(results), ["c", "b", "a"])

    def test_top_k_keeps_only_the_best_results(self) -> None:
        results = search((1.0, 0.1), self.docs, top_k=2)
        self.assertEqual(result_ids(results), ["a", "b"])

    def test_tied_scores_keep_document_order(self) -> None:
        tied_docs = [
            DocumentVector("first", "same vector", (1.0, 0.0)),
            DocumentVector("second", "same vector", (1.0, 0.0)),
        ]
        results = search((1.0, 0.0), tied_docs, top_k=2)
        self.assertEqual(result_ids(results), ["first", "second"])

    def test_top_k_larger_than_corpus_returns_all(self) -> None:
        self.assertEqual(len(search((1.0, 0.0), self.docs, top_k=10)), 3)

    def test_empty_corpus_returns_no_results(self) -> None:
        self.assertEqual(search((1.0, 0.0), [], top_k=3), [])

    def test_non_positive_top_k_raises(self) -> None:
        with self.assertRaises(ValueError):
            search((1.0, 0.0), self.docs, top_k=0)


if __name__ == "__main__":
    unittest.main()
