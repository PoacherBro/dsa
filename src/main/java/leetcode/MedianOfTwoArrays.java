package leetcode;

import java.io.PrintStream;

/**
 * @author Leo on 2017/2/4
 *
 * Question:
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
 * Example 1:
 * nums1 = [1, 3]
 * nums2 = [2]
 *
 * The median is 2.0
 *
 * Example 2:
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 *
 * The median is (2 + 3)/2 = 2.5
 * 中间值即为：两个数组顺序合并之后，如果是奇数长，则是数组中间位置的值；
 * 如果是偶数长，则是折半后，中间两个数的平均数
 */
public class MedianOfTwoArrays {
    /**
     * 解法1：直接把两个数组结合，因为是有序数组，同时遍历两个数组，然后比较+结合
     * 时间复杂度为O(n)，n是两个数组之和，空间复杂度是O(n)
     *
     * @param nums1 有序数组
     * @param nums2 有序数组
     * @return 两个数组的中间值
     */
    private static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        double result;
        int[] arr = new int[nums1.length + nums2.length];

        //使用归并排序思想，把两个有序数组直接合并
        int i = 0, j = 0;
        while (i < nums1.length && j < nums2.length) {
            int num1 = nums1[i];
            int num2 = nums2[j];

            if (num1 < num2) {
                arr[i + j] = num1;
                i++;
            } else {
                arr[i + j] = num2;
                j++;
            }
        }

        if (i != nums1.length) {
            System.arraycopy(nums1, i, arr, i + j, nums1.length - i);
        } else if (j != nums2.length) {
            System.arraycopy(nums2, j, arr, i + j, nums2.length - j);
        }

        int length = arr.length;
        int pos = length / 2;
        if (length % 2 == 0) {
            result = (arr[pos - 1] + arr[pos]) / 2.0f;
        } else {
            result = arr[pos];
        }

        return result;
    }

    /**
     * 解法2：还是用顺序查找，但是我们不需要使用O(n)的空间。
     * 利用merge sort类似的原理，我们记录第K个大小的数，这样搜索到k时，直接返回。
     * 时间复杂度时O(k)，空间复杂度时O(1)
     *
     * @param nums1 有序数组
     * @param nums2 有序数组
     * @return 中间值
     */
    private static double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int k = (m + n + 1) / 2;

        if ((m + n) % 2 == 0) {
            return (getKthNum(nums1, m, nums2, n, k + 1) + getKthNum(nums1, m, nums2, n, k)) / 2.0f;
        } else {
            return getKthNum(nums1, m, nums2, n, k);
        }

    }

    private static int getKthNum(int[] nums1, int m, int[] nums2, int n, int k) {
        // 确保 m <= n
        if (m > n) {
            return getKthNum(nums2, n, nums1, m, k);
        }

        // 处理特殊情况
        if (m == 0) {
            return nums2[k - 1];
        }
        if (k == 1) {
            return Math.min(nums1[0], nums2[0]);
        }
        if ((m + n) == k) {
            return Math.max(nums1[0], nums2[0]);
        }

        int i = 0, j = 0;
        int a1 = nums1[0], a2;
        int result = 0;

        while ((i + j) < k) {
            if (i < m) a1 = nums1[i];
            a2 = nums2[j];

            if (a1 <= a2) {
                // 确保数组不越界
                if (i < m) {
                    result = a1;
                    i++;
                } else {
                    result = a2;
                    j++;
                }
            } else {
                result = a2;
                j++;
            }
        }

        return result;
    }

    /**
     * 解法3：二分法查找，找到第K个大小的值
     * 同上面的解法，这个问题主要的思路就是找到第K个大小的值，这里的K是中间位置。
     *
     * 其实可以不需要按照上面顺序查找，根据二分法思想，可以用两个index，p和q指向两个数组。
     * 如果p + q = k，那么就找到了第k个大小的值。
     * 同时，退出循环的条件就是 nums1[p - 1] <= nums2[q] && nums1[p] >= nums2[q - 1]
     *
     * 利用二分法查找思想，我们确定p的搜索界限是[0, nums1.length]。同时由于q=k-p，那么q也动态的移动。
     *
     * https://discuss.leetcode.com/topic/4996/share-my-o-log-min-m-n-solution-with-explanation
     *
     * 复杂度是O(log(m+n))
     *
     * @param nums1 有序数组
     * @param nums2 有序数组
     * @return 中间值
     */
    private static double findMedianSortedArrays3(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;

        int k = (m + n + 1) / 2;

        if ((m + n) % 2 == 1) {
            return getKthNum2(nums1, m, nums2, n, k);
        } else {
            return (getKthNum2(nums1, m, nums2, n, k) + getKthNum2(nums1, m, nums2, n, k + 1)) / 2.0f;
        }
    }

    private static int getKthNum2(int[] nums1, int m, int[] nums2, int n, int k) {
        // 保证 m <= n
        if (m > n) {
            return getKthNum2(nums2, n, nums1, m, k);
        }
        // 处理特殊情况
        if (m == 0) {
            return nums2[k - 1];
        }
        if (k == 1) {
            return Math.min(nums1[0], nums2[0]);
        }
        if ((m + n) == k) {
            return Math.max(nums1[0], nums2[0]);
        }

        int iMin = 0, iMax = m;
        while (iMin <= iMax) {
            int i = (iMax + iMin) / 2; // 二分法查找
            int j = k - i;

            if (i < m && j > 0 && nums1[i] < nums2[j - 1]) {
                iMin = i + 1;
            } else if (i > 0 && j < n && nums1[i - 1] > nums2[j]) {
                iMax = i - 1;
            } else {
                if (i == 0) return nums2[j - 1];
                else if (j == 0) return nums1[i - 1];
                else return Math.max(nums1[i - 1], nums2[j - 1]);
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        PrintStream stdOut = System.out;
        int[] nums1 = {5};
        int[] nums2 = {3};
        stdOut.println(findMedianSortedArrays(nums1, nums2));
        stdOut.println(findMedianSortedArrays2(nums1, nums2));
        stdOut.println(findMedianSortedArrays3(nums1, nums2));
    }
}
