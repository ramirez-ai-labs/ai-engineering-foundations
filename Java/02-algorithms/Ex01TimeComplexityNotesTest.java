/** Tests for Ex01TimeComplexityNotes. Run: javac *.java && java Ex01TimeComplexityNotesTest */
public class Ex01TimeComplexityNotesTest {

    static void run() {
        AlgorithmsCheck.equal(7, Ex01TimeComplexityNotes.getFirst(new int[] { 7, 8 }), "getFirst");
        AlgorithmsCheck.equal(-1, Ex01TimeComplexityNotes.getFirst(new int[0]), "getFirst of empty array is -1");
        AlgorithmsCheck.equal(15, Ex01TimeComplexityNotes.sum(new int[] { 1, 2, 3, 4, 5 }), "sum");
        AlgorithmsCheck.equal(0, Ex01TimeComplexityNotes.sum(new int[0]), "sum of empty array");
        AlgorithmsCheck.equal(0, Ex01TimeComplexityNotes.sum(new int[] { -3, 3 }), "sum with negatives");
        for (int n = 0; n < 8; n++) {
            AlgorithmsCheck.equal(n * (n - 1) / 2, Ex01TimeComplexityNotes.countPairs(new int[n]), "countPairs is n choose 2 for n=" + n);
        }
    }

    public static void main(String[] args) {
        run();
        AlgorithmsCheck.finish();
    }
}
