/**
 * Binary search - O(log n) search in a sorted array.
 *
 * Prerequisite: array must be sorted. Used in many "find boundary" and
 * "search in sorted space" problems.
 */
public class Ex02BinarySearch {

    /** Returns index of target, or -1 if not found. */
    static int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // avoid overflow
            if (nums[mid] == target) return mid;
            if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] sorted = { 1, 3, 5, 7, 9, 11 };
        System.out.println("search(5): " + search(sorted, 5));   // 2
        System.out.println("search(9): " + search(sorted, 9));   // 4
        System.out.println("search(4): " + search(sorted, 4));   // -1
    }
}
