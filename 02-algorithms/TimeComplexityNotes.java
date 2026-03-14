/**
 * Big-O refresher — what we mean by time (and space) complexity.
 *
 * O(1)   — constant: array index, hash lookup
 * O(log n) — logarithmic: binary search
 * O(n)   — linear: single loop over input
 * O(n log n) — linearithmic: merge sort, heap sort
 * O(n²)  — quadratic: two nested loops over input
 * O(2^n) — exponential: naive recursion (e.g. fib without memo)
 *
 * We usually care about worst case; sometimes average (e.g. hash table).
 */
public class TimeComplexityNotes {

    // O(1) — constant time
    static int getFirst(int[] arr) {
        return arr.length > 0 ? arr[0] : -1;
    }

    // O(n) — one pass
    static int sum(int[] arr) {
        int s = 0;
        for (int x : arr) s += x;
        return s;
    }

    // O(n²) — two nested passes
    static int countPairs(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++)
            for (int j = i + 1; j < arr.length; j++)
                count++;
        return count;
    }

    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5 };
        System.out.println("getFirst: " + getFirst(arr));
        System.out.println("sum: " + sum(arr));
        System.out.println("countPairs (n choose 2): " + countPairs(arr));
    }
}
