import java.util.HashMap;
import java.util.Map;

/**
 * Two Sum - find two indices such that arr[i] + arr[j] = target.
 *
 * Brute force: O(n^2). With hash map (store complement): O(n).
 * Classic interview problem and pattern: "use a map to remember what you've seen."
 */
public class TwoSum {

    /** O(n^2) - two nested loops */
    static int[] twoSumBrute(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) return new int[] { i, j };
            }
        }
        return new int[] { -1, -1 };
    }

    /** O(n) - one pass with hash map: value -> index; look for (target - value) */
    static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (seen.containsKey(need)) return new int[] { seen.get(need), i };
            seen.put(nums[i], i);
        }
        return new int[] { -1, -1 };
    }

    public static void main(String[] args) {
        int[] nums = { 2, 7, 11, 15 };
        int target = 9;
        System.out.println("Brute: " + java.util.Arrays.toString(twoSumBrute(nums, target)));
        System.out.println("Map:   " + java.util.Arrays.toString(twoSum(nums, target)));
    }
}
