import java.util.Arrays;
import java.util.Random;

/** Tests for MergeSort. Run: javac *.java && java MergeSortTest */
public class MergeSortTest {

    /** MergeSort.sort works in place, so sort a copy and return it. */
    static int[] sorted(int... values) {
        int[] copy = Arrays.copyOf(values, values.length);
        MergeSort.sort(copy);
        return copy;
    }

    static void run() {
        // merge(arr, left, mid, right) merges arr[left..mid] and arr[mid+1..right], both already sorted.
        int[] halves = { 1, 4, 9, 2, 3, 10 };
        MergeSort.merge(halves, 0, 2, 5);
        Check.equal(new int[] { 1, 2, 3, 4, 9, 10 }, halves, "merge interleaves two sorted halves");
        int[] leftFirst = { 1, 2, 3, 4, 5 };
        MergeSort.merge(leftFirst, 0, 1, 4);
        Check.equal(new int[] { 1, 2, 3, 4, 5 }, leftFirst, "merge when the left half runs out first");

        Check.equal(new int[] { 3, 9, 10, 27, 38, 43, 82 }, sorted(38, 27, 43, 3, 9, 82, 10), "example from main");
        Check.equal(new int[0], sorted(), "empty array");
        Check.equal(new int[] { 1 }, sorted(1), "single element");
        Check.equal(new int[] { 1, 2, 3, 4 }, sorted(1, 2, 3, 4), "already sorted");
        Check.equal(new int[] { 1, 2, 3, 4 }, sorted(4, 3, 2, 1), "reversed");
        Check.equal(new int[] { -1, -1, 0, 3, 3 }, sorted(3, -1, 3, 0, -1), "duplicates and negatives");

        // Compare against the library sort on random input. A fixed seed keeps it repeatable.
        Random random = new Random(42);
        for (int trial = 0; trial < 50; trial++) {
            int[] values = new int[random.nextInt(41)];
            for (int i = 0; i < values.length; i++) {
                values[i] = random.nextInt(41) - 20;
            }
            int[] expected = Arrays.copyOf(values, values.length);
            Arrays.sort(expected);
            Check.equal(expected, sorted(values), "matches Arrays.sort on " + Arrays.toString(values));
        }
    }

    public static void main(String[] args) {
        run();
        Check.finish();
    }
}
