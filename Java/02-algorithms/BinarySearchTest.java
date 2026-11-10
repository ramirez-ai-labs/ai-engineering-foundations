/** Tests for BinarySearch. Run: javac *.java && java BinarySearchTest */
public class BinarySearchTest {

    static void run() {
        int[] sorted = { 1, 3, 5, 7, 9, 11 };
        for (int i = 0; i < sorted.length; i++) {
            Check.equal(i, BinarySearch.search(sorted, sorted[i]), "finds " + sorted[i] + " at its index");
        }
        Check.equal(-1, BinarySearch.search(sorted, 0), "missing below range");
        Check.equal(-1, BinarySearch.search(sorted, 12), "missing above range");
        Check.equal(-1, BinarySearch.search(sorted, 4), "missing between elements");
        Check.equal(-1, BinarySearch.search(new int[0], 1), "empty array");
        Check.equal(0, BinarySearch.search(new int[] { 5 }, 5), "single element found");
        Check.equal(-1, BinarySearch.search(new int[] { 5 }, 4), "single element missing");
        // Catches off-by-one errors in the left <= right condition.
        Check.equal(0, BinarySearch.search(new int[] { 1, 2 }, 1), "two elements, first");
        Check.equal(1, BinarySearch.search(new int[] { 1, 2 }, 2), "two elements, second");
        Check.equal(0, BinarySearch.search(new int[] { -8, -3, 0, 4 }, -8), "negatives, first");
        Check.equal(3, BinarySearch.search(new int[] { -8, -3, 0, 4 }, 4), "negatives, last");
        // Plain binary search does not promise the FIRST match among duplicates.
        int[] duplicates = { 1, 2, 2, 2, 3 };
        Check.equal(2, duplicates[BinarySearch.search(duplicates, 2)], "duplicates return some matching index");
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
