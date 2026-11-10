import java.util.ArrayList;
import java.util.List;

/** Tests for SimpleVectorSearchDemo. Run: javac *.java && java SimpleVectorSearchDemoTest */
public class SimpleVectorSearchDemoTest {

    static SimpleVectorSearchDemo.DocumentVector doc(String id, double... embedding) {
        return new SimpleVectorSearchDemo.DocumentVector(id, id, embedding);
    }

    static List<String> ids(List<SimpleVectorSearchDemo.SearchResult> results) {
        List<String> ids = new ArrayList<>();
        for (SimpleVectorSearchDemo.SearchResult result : results) {
            ids.add(result.id);
        }
        return ids;
    }

    static void run() {
        // Cosine similarity
        Check.equal(1.0, SimpleVectorSearchDemo.cosineSimilarity(new double[] { 1, 2 }, new double[] { 1, 2 }), 1e-9,
                "identical vectors score 1");
        Check.equal(0.0, SimpleVectorSearchDemo.cosineSimilarity(new double[] { 1, 0 }, new double[] { 0, 1 }), 1e-9,
                "orthogonal vectors score 0");
        Check.throwsIllegalArgument(
                () -> SimpleVectorSearchDemo.cosineSimilarity(new double[] { 1, 0 }, new double[] { 1, 0, 0 }),
                "dimension mismatch throws");
        Check.throwsIllegalArgument(
                () -> SimpleVectorSearchDemo.cosineSimilarity(new double[] { 0, 0 }, new double[] { 1, 0 }),
                "zero vector throws");

        // Search
        List<SimpleVectorSearchDemo.DocumentVector> docs = List.of(doc("a", 1, 0), doc("b", 1, 1), doc("c", 0, 1));
        Check.equal(List.of("a", "b", "c"), ids(SimpleVectorSearchDemo.search(new double[] { 1, 0.1 }, docs, 3)),
                "results ranked by score");
        Check.equal(List.of("c", "b", "a"), ids(SimpleVectorSearchDemo.search(new double[] { 0, 1 }, docs, 3)),
                "ranks correctly when the best match comes last");
        Check.equal(List.of("a", "b"), ids(SimpleVectorSearchDemo.search(new double[] { 1, 0.1 }, docs, 2)),
                "top-k keeps only the best results");
        List<SimpleVectorSearchDemo.DocumentVector> tied = List.of(doc("first", 1, 0), doc("second", 1, 0));
        Check.equal(List.of("first", "second"), ids(SimpleVectorSearchDemo.search(new double[] { 1, 0 }, tied, 2)),
                "tied scores keep document order (List.sort is stable)");
        Check.equal(3, SimpleVectorSearchDemo.search(new double[] { 1, 0 }, docs, 10).size(),
                "top-k larger than corpus returns all");
        Check.equal(0, SimpleVectorSearchDemo.search(new double[] { 1, 0 }, List.of(), 3).size(),
                "empty corpus returns no results");
        Check.throwsIllegalArgument(() -> SimpleVectorSearchDemo.search(new double[] { 1, 0 }, docs, 0),
                "non-positive top-k throws");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
