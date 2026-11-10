import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * End-to-end vector search demo with a linear-scan benchmark.
 *
 * This demo has two parts:
 *
 * Part 1 — conceptual demo: hand-written embeddings, cosine similarity, top-k retrieval.
 * Part 2 — scale benchmark: synthetic vectors at 100 / 1,000 / 10,000 items to show
 *           where linear scan breaks and why ANN indexes (HNSW, IVF) exist.
 *
 * In a real AI system you would generate embeddings with a model, store them externally,
 * add metadata filters, and use an approximate nearest-neighbor (ANN) index so that
 * retrieval latency stays sub-millisecond even at millions of vectors.
 *
 * Why ANN instead of exact scan?
 *   Exact cosine similarity over N vectors is O(N * D) where D is the embedding dimension.
 *   At N=10k and D=1536 (text-embedding-3-small) that is ~15M multiply-add operations
 *   per query. At N=10M it becomes ~15B. ANN indexes trade a small accuracy loss for
 *   sub-linear query time by doing work at index time: a navigable graph (HNSW, roughly
 *   O(log N) per query in practice) or an inverted file of clusters (IVF, which scans
 *   only the few clusters nearest the query).
 *   The benchmark below makes this degradation visible even in a toy setting.
 */
public class Ex01SimpleVectorSearchDemo {

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

        results.sort((a, b) -> Double.compare(b.score, a.score));
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

    // --- Part 2: benchmark helpers ---

    static List<DocumentVector> generateSyntheticCorpus(int size, int dims, Random rng) {
        List<DocumentVector> corpus = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            double[] embedding = new double[dims];
            for (int d = 0; d < dims; d++) {
                embedding[d] = rng.nextDouble();
            }
            corpus.add(new DocumentVector("doc-" + i, "synthetic document " + i, embedding));
        }
        return corpus;
    }

    static long benchmarkSearch(List<DocumentVector> corpus, int dims, Random rng) {
        double[] query = new double[dims];
        for (int d = 0; d < dims; d++) {
            query[d] = rng.nextDouble();
        }
        long start = System.nanoTime();
        search(query, corpus, 5);
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        // --- Part 1: conceptual demo ---
        List<DocumentVector> documents = new ArrayList<>();
        documents.add(new DocumentVector(
                "doc-1",
                "Redis caching reduces repeated inference latency.",
                new double[] { 0.92, 0.10, 0.18 }));
        documents.add(new DocumentVector(
                "doc-2",
                "Vector retrieval helps a RAG system find relevant chunks.",
                new double[] { 0.88, 0.15, 0.30 }));
        documents.add(new DocumentVector(
                "doc-3",
                "Rate limiting protects expensive AI endpoints from overload.",
                new double[] { 0.25, 0.92, 0.28 }));
        documents.add(new DocumentVector(
                "doc-4",
                "Audit logs and policy controls matter in secure AI platforms.",
                new double[] { 0.14, 0.20, 0.95 }));

        // Query vector is intentionally closest to retrieval/RAG content.
        double[] queryEmbedding = { 0.90, 0.12, 0.35 };
        List<SearchResult> topResults = search(queryEmbedding, documents, 3);

        System.out.println("=== Part 1: Conceptual Demo ===");
        System.out.println("Top search results for query embedding:");
        for (SearchResult result : topResults) {
            System.out.printf("  %s | score=%.4f | %s%n", result.id, result.score, result.text);
        }

        System.out.println();
        System.out.println("What this demo teaches:");
        System.out.println("  - embeddings are arrays of numbers");
        System.out.println("  - cosine similarity produces a ranking score");
        System.out.println("  - top-k retrieval returns the nearest items");
        System.out.println("  - this demo uses exact scan, not an ANN index");

        // --- Part 2: scale benchmark ---
        System.out.println();
        System.out.println("=== Part 2: Linear Scan Benchmark ===");
        System.out.println("Corpus size | Dims | Search latency (ms)");
        System.out.println("------------|------|--------------------");

        Random rng = new Random(42);
        int dims = 128; // smaller than production (1536) but enough to show the trend
        int[] sizes = { 100, 1_000, 10_000 };

        for (int size : sizes) {
            List<DocumentVector> corpus = generateSyntheticCorpus(size, dims, rng);

            // Warm up the JIT with a few throws-away searches.
            for (int w = 0; w < 5; w++) {
                benchmarkSearch(corpus, dims, rng);
            }

            // Average over 10 timed runs to reduce noise.
            long totalNs = 0;
            int runs = 10;
            for (int r = 0; r < runs; r++) {
                totalNs += benchmarkSearch(corpus, dims, rng);
            }
            double avgMs = totalNs / (double) runs / 1_000_000.0;
            System.out.printf("%,11d | %4d | %.4f ms%n", size, dims, avgMs);
        }

        System.out.println();
        System.out.println("Interpretation:");
        System.out.println("  Latency grows roughly linearly with corpus size — O(N * D).");
        System.out.println("  At production scale (millions of vectors, D=1536), exact scan");
        System.out.println("  is too slow for real-time queries. ANN indexes make query time");
        System.out.println("  sub-linear by doing work at index build time: HNSW builds a");
        System.out.println("  navigable graph (roughly O(log N) per query in practice), and IVF");
        System.out.println("  clusters vectors so a query scans only the nearest few clusters.");
        System.out.println("  The tradeoff: ANN may miss a small fraction of true nearest");
        System.out.println("  neighbors (controlled by an 'ef' or 'nprobe' parameter).");
    }
}
