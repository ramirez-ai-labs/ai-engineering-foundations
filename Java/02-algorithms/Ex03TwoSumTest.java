/**
 * Tests for Ex03TwoSum. Run: javac *.java && java Ex03TwoSumTest
 *
 * Both versions must give the same answers, so every case runs against both.
 */
public class Ex03TwoSumTest {

    static void check(int[] nums, int target, int[] expected, String name) {
        AlgorithmsCheck.equal(expected, Ex03TwoSum.twoSumBrute(nums, target), "brute: " + name);
        AlgorithmsCheck.equal(expected, Ex03TwoSum.twoSum(nums, target), "map: " + name);
    }

    static void run() {
        check(new int[] { 2, 7, 11, 15 }, 9, new int[] { 0, 1 }, "classic example");
        check(new int[] { 1, 2, 3, 4 }, 7, new int[] { 2, 3 }, "pair at the end");
        // 3 + 3 = 6, but there is only one 3, so the answer must be 2 + 4.
        check(new int[] { 3, 2, 4 }, 6, new int[] { 1, 2 }, "does not use the same element twice");
        check(new int[] { 3, 3 }, 6, new int[] { 0, 1 }, "duplicate values");
        check(new int[] { -4, 1, 0, 4 }, 0, new int[] { 0, 3 }, "negative numbers and zero");
        check(new int[] { 1, 2, 3 }, 100, new int[] { -1, -1 }, "no solution");
        check(new int[0], 5, new int[] { -1, -1 }, "empty array");
        check(new int[] { 5 }, 5, new int[] { -1, -1 }, "single element");

        int[] nums = { 10, -2, 7, 5, 8, 1 };
        int[] pair = Ex03TwoSum.twoSum(nums, 13);
        AlgorithmsCheck.isTrue(pair[0] < pair[1], "indices are in increasing order");
        AlgorithmsCheck.equal(13, nums[pair[0]] + nums[pair[1]], "indices point at values that sum to target");
    }

    public static void main(String[] args) {
        run();
        AlgorithmsCheck.finish();
    }
}
