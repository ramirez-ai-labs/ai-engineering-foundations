/** Tests for Ex02BinarySearch. Run: javac *.java && java Ex02BinarySearchTest */
public class Ex02BinarySearchTest {

    static void run() {
        int[] sorted = { 1, 3, 5, 7, 9, 11 };
        for (int i = 0; i < sorted.length; i++) {
            AlgorithmsCheck.equal(i, Ex02BinarySearch.search(sorted, sorted[i]), "finds " + sorted[i] + " at its index");
        }
        AlgorithmsCheck.equal(-1, Ex02BinarySearch.search(sorted, 0), "missing below range");
        AlgorithmsCheck.equal(-1, Ex02BinarySearch.search(sorted, 12), "missing above range");
        AlgorithmsCheck.equal(-1, Ex02BinarySearch.search(sorted, 4), "missing between elements");
        AlgorithmsCheck.equal(-1, Ex02BinarySearch.search(new int[0], 1), "empty array");
        AlgorithmsCheck.equal(0, Ex02BinarySearch.search(new int[] { 5 }, 5), "single element found");
        AlgorithmsCheck.equal(-1, Ex02BinarySearch.search(new int[] { 5 }, 4), "single element missing");
        // Catches off-by-one errors in the left <= right condition.
        AlgorithmsCheck.equal(0, Ex02BinarySearch.search(new int[] { 1, 2 }, 1), "two elements, first");
        AlgorithmsCheck.equal(1, Ex02BinarySearch.search(new int[] { 1, 2 }, 2), "two elements, second");
        AlgorithmsCheck.equal(0, Ex02BinarySearch.search(new int[] { -8, -3, 0, 4 }, -8), "negatives, first");
        AlgorithmsCheck.equal(3, Ex02BinarySearch.search(new int[] { -8, -3, 0, 4 }, 4), "negatives, last");
        // Plain binary search does not promise the FIRST match among duplicates.
        int[] duplicates = { 1, 2, 2, 2, 3 };
        AlgorithmsCheck.equal(2, duplicates[Ex02BinarySearch.search(duplicates, 2)], "duplicates return some matching index");
    }

    public static void main(String[] args) {
        run();
        AlgorithmsCheck.finish();
    }
}
