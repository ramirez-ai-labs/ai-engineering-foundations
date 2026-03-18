import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Small end-to-end vector search demo.
 *
 * This is intentionally tiny:
 * - embeddings are hand-written arrays
 * - every document is scanned linearly
 * - cosine similarity is used for ranking
 *
 * In a real AI system you would usually generate embeddings with a model,
 * store them externally, add metadata filters, and use an ANN index.
 */
public class SimpleVectorSearchDemo {

    static class DocumentVector {
        final String id;
        final String text;
        final double[] embedding;

        DocumentVector(String id, String text, double[] embedding) {
            this.id = id;
            this.text = text;
            this.embedding = embedding;
        }
    }

    static class SearchResult {
        final String id;
        final String text;
        final double score;

        SearchResult(String id, String text, double score) {
            this.id = id;
            this.text = text;
            this.score = score;
        }
    }

    static List<SearchResult> search(double[] queryEmbedding, List<DocumentVector> documents, int topK) {
        if (topK <= 0) {
            throw new IllegalArgumentException("topK must be greater than 0");
        }

        List<SearchResult> results = new ArrayList<>();
        for (DocumentVector document : documents) {
            validateSameDimensions(queryEmbedding, document.embedding);
            double score = cosineSimilarity(queryEmbedding, document.embedding);
            results.add(new SearchResult(document.id, document.text, score));
        }

        results.sort(Comparator.comparingDouble(result -> result.score).reversed());
        return results.subList(0, Math.min(topK, results.size()));
    }

    static double cosineSimilarity(double[] left, double[] right) {
        validateSameDimensions(left, right);

        double dot = 0.0;
        double leftNorm = 0.0;
        double rightNorm = 0.0;

        for (int i = 0; i < left.length; i++) {
            dot += left[i] * right[i];
            leftNorm += left[i] * left[i];
            rightNorm += right[i] * right[i];
        }

        if (leftNorm == 0.0 || rightNorm == 0.0) {
            throw new IllegalArgumentException("cosine similarity is undefined for a zero vector");
        }

        return dot / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));
    }

    static void validateSameDimensions(double[] left, double[] right) {
        if (left.length != right.length) {
            throw new IllegalArgumentException("embedding dimensions must match");
        }
    }

    public static void main(String[] args) {
        List<DocumentVector> documents = List.of(
                new DocumentVector(
                        "doc-1",
                        "Redis caching reduces repeated inference latency.",
                        new double[] { 0.92, 0.10, 0.18 }),
                new DocumentVector(
                        "doc-2",
                        "Vector retrieval helps a RAG system find relevant chunks.",
                        new double[] { 0.88, 0.15, 0.30 }),
                new DocumentVector(
                        "doc-3",
                        "Rate limiting protects expensive AI endpoints from overload.",
                        new double[] { 0.25, 0.92, 0.28 }),
                new DocumentVector(
                        "doc-4",
                        "Audit logs and policy controls matter in secure AI platforms.",
                        new double[] { 0.14, 0.20, 0.95 }));

        // Query vector is intentionally closest to retrieval/RAG content.
        double[] queryEmbedding = { 0.90, 0.12, 0.35 };
        List<SearchResult> topResults = search(queryEmbedding, documents, 3);

        System.out.println("Top search results for query embedding:");
        for (SearchResult result : topResults) {
            System.out.printf("%s | score=%.4f | %s%n", result.id, result.score, result.text);
        }

        System.out.println();
        System.out.println("What this demo teaches:");
        System.out.println("- embeddings are arrays of numbers");
        System.out.println("- cosine similarity produces a ranking score");
        System.out.println("- top-k retrieval returns the nearest items");
        System.out.println("- this demo uses exact scan, not an ANN index");
    }
}
