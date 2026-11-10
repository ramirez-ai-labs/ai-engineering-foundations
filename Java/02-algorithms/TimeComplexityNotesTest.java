/** Tests for TimeComplexityNotes. Run: javac *.java && java TimeComplexityNotesTest */
public class TimeComplexityNotesTest {

    static void run() {
        Check.equal(7, TimeComplexityNotes.getFirst(new int[] { 7, 8 }), "getFirst");
        Check.equal(-1, TimeComplexityNotes.getFirst(new int[0]), "getFirst of empty array is -1");
        Check.equal(15, TimeComplexityNotes.sum(new int[] { 1, 2, 3, 4, 5 }), "sum");
        Check.equal(0, TimeComplexityNotes.sum(new int[0]), "sum of empty array");
        Check.equal(0, TimeComplexityNotes.sum(new int[] { -3, 3 }), "sum with negatives");
        for (int n = 0; n < 8; n++) {
            Check.equal(n * (n - 1) / 2, TimeComplexityNotes.countPairs(new int[n]), "countPairs is n choose 2 for n=" + n);
        }
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
