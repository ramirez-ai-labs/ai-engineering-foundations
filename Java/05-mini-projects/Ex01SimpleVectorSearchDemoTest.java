import java.util.ArrayList;
import java.util.List;

/** Tests for Ex01SimpleVectorSearchDemo. Run: javac *.java && java Ex01SimpleVectorSearchDemoTest */
public class Ex01SimpleVectorSearchDemoTest {

    static Ex01SimpleVectorSearchDemo.DocumentVector doc(String id, double... embedding) {
        return new Ex01SimpleVectorSearchDemo.DocumentVector(id, id, embedding);
    }

    static List<String> ids(List<Ex01SimpleVectorSearchDemo.SearchResult> results) {
        List<String> ids = new ArrayList<>();
        for (Ex01SimpleVectorSearchDemo.SearchResult result : results) {
            ids.add(result.id);
        }
        return ids;
    }

    static void run() {
        // Cosine similarity
        Check.equal(1.0, Ex01SimpleVectorSearchDemo.cosineSimilarity(new double[] { 1, 2 }, new double[] { 1, 2 }), 1e-9,
                "identical vectors score 1");
        Check.equal(0.0, Ex01SimpleVectorSearchDemo.cosineSimilarity(new double[] { 1, 0 }, new double[] { 0, 1 }), 1e-9,
                "orthogonal vectors score 0");
        Check.throwsIllegalArgument(
                () -> Ex01SimpleVectorSearchDemo.cosineSimilarity(new double[] { 1, 0 }, new double[] { 1, 0, 0 }),
                "dimension mismatch throws");
        Check.throwsIllegalArgument(
                () -> Ex01SimpleVectorSearchDemo.cosineSimilarity(new double[] { 0, 0 }, new double[] { 1, 0 }),
                "zero vector throws");

        // Search
        List<Ex01SimpleVectorSearchDemo.DocumentVector> docs = List.of(doc("a", 1, 0), doc("b", 1, 1), doc("c", 0, 1));
        Check.equal(List.of("a", "b", "c"), ids(Ex01SimpleVectorSearchDemo.search(new double[] { 1, 0.1 }, docs, 3)),
                "results ranked by score");
        Check.equal(List.of("c", "b", "a"), ids(Ex01SimpleVectorSearchDemo.search(new double[] { 0, 1 }, docs, 3)),
                "ranks correctly when the best match comes last");
        Check.equal(List.of("a", "b"), ids(Ex01SimpleVectorSearchDemo.search(new double[] { 1, 0.1 }, docs, 2)),
                "top-k keeps only the best results");
        List<Ex01SimpleVectorSearchDemo.DocumentVector> tied = List.of(doc("first", 1, 0), doc("second", 1, 0));
        Check.equal(List.of("first", "second"), ids(Ex01SimpleVectorSearchDemo.search(new double[] { 1, 0 }, tied, 2)),
                "tied scores keep document order (List.sort is stable)");
        Check.equal(3, Ex01SimpleVectorSearchDemo.search(new double[] { 1, 0 }, docs, 10).size(),
                "top-k larger than corpus returns all");
        Check.equal(0, Ex01SimpleVectorSearchDemo.search(new double[] { 1, 0 }, List.of(), 3).size(),
                "empty corpus returns no results");
        Check.throwsIllegalArgument(() -> Ex01SimpleVectorSearchDemo.search(new double[] { 1, 0 }, docs, 0),
                "non-positive top-k throws");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
