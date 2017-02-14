package leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo on 2017/1/29.
 */
public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> temp = new HashMap<Integer, Integer>(nums.length);
        for (int i = 0, j = nums.length; i < j; i++) {
            if (temp.containsKey(target - nums[i])) {
                result[0] = temp.get(target - nums[i]);
                result[1] = i;
                return result;
            }
            temp.put(nums[i], i);
        }
        return result;
    }
}
