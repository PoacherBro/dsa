package leetcode;

import java.io.PrintStream;
import java.util.Arrays;

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
     * 利用merge sort类似的原理（合并分割的数组），我们记录第K个大小的数，这样搜索到k时，直接返回。
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
        if (m == 0 && n == 0) {
            throw new IllegalArgumentException("Two arrays both are empty");
        }

        // 确保 m <= n
        if (m > n) {
            return getKthNum(nums2, n, nums1, m, k);
        }

        // 处理特殊情况
        if (m == 0) {
            return nums2[k - 1];
        }
        if (k == 1) {
            return m > 0 ? Math.min(nums1[0], nums2[0]) : nums2[0];
        }
        if ((m + n) == k) {
            return m > 0 ? Math.max(nums1[m - 1], nums2[n - 1]) : nums2[n - 1];
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
     * 对于两个数据长度为m和n的两个数组A和B，我们其实把两个数组分割成这样：
     *            left_part       |      right_part
     * A[0], A[1], ......, A[i-1] | A[i], A[i+1], ......, A[m-1]
     * B[0], B[1], ......, B[j-1] | B[j], B[j+1], ......, B[n-1]
     * 要满足中间值，则把左右两边作为一个数组，只要同时满足两个条件=>
     * 1. len(left_part) = len(right_part)
     * 2. max(left_part) <= min(right_part)
     * 那么，可以说中间值是 median = (max(left_part) + min(right_part))/2
     *
     * 其实可以不需要按照上面顺序查找，根据二分法思想，可以用两个index，i和j指向两个数组。
     * 如果i + j = k，那么就找到了第k个大小的值。
     * 同时，退出循环的条件就是 nums1[i - 1] <= nums2[j] && nums1[i] >= nums2[j - 1]
     *
     * https://discuss.leetcode.com/topic/4996/share-my-o-log-min-m-n-solution-with-explanation
     *
     * 复杂度是O(log(m+n))
     *
     * @param nums1 有序数组
     * @param nums2 有序数组
     * @return 中间值
     */
    private static double findMedianSortedArrays3(int[] nums1, int nums2[]) {
        int m = nums1.length;
        int n = nums2.length;

        if (m == 0 && n == 0)
            throw new IllegalArgumentException("Two arrays both are empty");

        // 保证m<=n
        if (m > n) {
            return findMedianSortedArrays3(nums2, nums1);
        }

        int i = 0, j = 0, iMin = 0, iMax = m, k = (m + n + 1) / 2;
        double maxLeft = 0.0, minRight;
        while (iMin <= iMax) {
            i = (iMin + iMax) / 2;
            j = k - i;

            // 根据m<=n && i<m && j=k-i，可以推断一定 j > 0
            if (i < m && nums2[j - 1] > nums1[i]) {
                // i 对应的值小了
                iMin = i + 1;
            } else if (i > 0 && nums1[i - 1] > nums2[j]) { // 同理，i>0则一定j<n
                // i 对应的值大了
                iMax = i - 1;
            } else {
                // i 值刚好
                if (i == 0) maxLeft = (double)nums2[k - 1];
                else if (j == 0) maxLeft = (double)nums1[k - 1];
                else maxLeft = (double)Math.max(nums1[i - 1], nums2[j - 1]);

                break;
            }
        }

        // 如果是基数，那么maxLeft就是平均值
        if ((m + n) % 2 == 1) {
            return maxLeft;
        }

        if (i == m) {
            minRight = (double)nums2[j];
        } else if (j == n) {
            minRight = (double)nums1[i];
        } else {
            minRight = (double)Math.min(nums1[i], nums2[j]);
        }
        return (maxLeft + minRight) / 2.0f;

    }


    /**
     * 引申问题，对于两个已排序的数组，如何找到第K个大小的数？（从小到大）
     * 和解法3的思想是一样的，不过这个时候我们假设就是要找出第K个大小的值，它不是中间值，该如何做？
     *
     * 解法1： 和之前解法2一样，用两个指针指向两个数组，此时算法复杂度是O(k)；
     * 解法2： 思想和之前解法3差不多，采用分治法，从两个数组各自的中间开始查找，此时算法复杂度是O(log(m) + log(n))；
     * 解法3： 在解法2类似，不过不是从两个数组的中间开始，而是从 K/2 开始，此时算法复杂度为O(log(k)) = O(log(m+n))。
     *
     * 以下对应为解法2，用递归实现。
     * 从每个数组的中间开始找起,
     * 1. 如果mid1+mid2 < K，则说明还需要往后面找，这时：
     *    1.1 如果nums1[mid1-1]>nums2[mid2-1]，说明nums2在mid2之前数据都不满足要求，可以舍弃，
     *        从mid2后面开始找，同时更正k的值为k-mid2;
     *    1.2 否则，则是从nums1的mid1后面开始找，更正k=k-mid1。
     * 2. 如果mid1+mid2 >= k，则说明中间值之前的数据能满足要求，这时：
     *    2.1 如果nums1[mid1-1]>nums2[mid2-1]，说明这个值可能在nums1里，且范围是[0 - (mid1-1)]；
     *    2.2 否则，则可能在nums2里，且范围是[0 - (mid2-1)]。
     *    注意这时不需要修改K值。
     * 3. 当一直切分到两个数组其中一个长度为0时，则另外一个数组的第K（修正过的）个值为对应要查找的值。
     *
     * 参考：https://www.geeksforgeeks.org/k-th-element-two-sorted-arrays/
     * @param nums1 从小到大排列整数数组
     * @param nums2 从小到大排列整数数组
     * @param k 第几个大的数，从1开始算起
     * @return 对应K的那个数
     */
    private static int findKthNum(int[] nums1, int[] nums2, int k) {
        int m = nums1.length;
        int n = nums2.length;

        // 处理异常数据
        if (m == 0 && n == 0) {
            throw new IllegalArgumentException("Two arrays cannot both be empty");
        }
        if (k > (m + n)) {
            throw new IllegalArgumentException("The K exceeded the sum of two arrays' length");
        }

        if (m == 0) {
            return nums2[k - 1];
        }
        if (n == 0) {
            return nums1[k - 1];
        }

        // 此处不能用(m+1)/2，因为如果m>0，则此表达式值一直不能为0，也就不能退出递归
        // 所以，对应K，应该是K-1，从0开始索引
        int mid1 = m / 2;
        int mid2 = n / 2;
        if (mid1 + mid2 < k - 1) {
            if (nums1[mid1] > nums2[mid2]) {
                return findKthNum(nums1, Arrays.copyOfRange(nums2, mid2 + 1, n), k - mid2 - 1);
            } else {
		        return findKthNum(Arrays.copyOfRange(nums1, mid1 + 1, m), nums2, k - mid1 - 1);
	        }
        } else {
	        if (nums1[mid1] > nums2[mid2]) {
		        return findKthNum(Arrays.copyOfRange(nums1, 0, mid1), nums2, k);
	        } else {
	            return findKthNum(nums1, Arrays.copyOfRange(nums2, 0, mid2), k);
            }
	    }
    }

    /**
     * 还是查找第K个大小数字问题，对应解法3，复杂度为O(log(K))，即O(log(m+n))，利用K值来二分查找。
     * 当m,n>2时，效率要比解法2好 => log(m)+log(n) = log(mn) > log(m+n), m,n>2
     *
     * 思路：（和求median问题的解法3类似）
     *
     * 参考：https://www.geeksforgeeks.org/k-th-element-two-sorted-arrays/
     * @param nums1 从小到大排列整数数组
     * @param nums2 从小到大排列整数数组
     * @param k 第几个大的数，从1开始算起
     * @return 对应K的那个数
     */
    private static int findKthNum2(int[] nums1, int[] nums2, int k) {
        int m = nums1.length;
        int n = nums2.length;

        // 处理异常数据
        if (m == 0 && n == 0) {
            throw new IllegalArgumentException("Two arrays cannot both be empty");
        }
        if (k > (m + n)) {
            throw new IllegalArgumentException("The K exceeded the sum of two arrays' length");
        }

        // 确保m<=n
        if (m > n) {
            return findKthNum2(nums2, nums1, k);
        }

        if (m == 0) {
            return nums2[k - 1];
        }
        if (k == 1) {
            return Math.min(nums1[0], nums2[0]);
        }

        int i = Math.min(m, k / 2), j = Math.min(n, k / 2);

        //递归方式
        if (nums1[i - 1] > nums2[j - 1]) {
            // 此时只需要找第 k-j 个元素就行，因为已经排除比较小的 j 个元素了
            return findKthNum2(nums1, Arrays.copyOfRange(nums2, j, n), k - j);
        } else {
            // 同理
            return findKthNum2(Arrays.copyOfRange(nums1, i, m), nums2, k - i);
        }
    }

    public static void main(String[] args) {
        PrintStream stdOut = System.out;
        int[] nums1 = {1};
        int[] nums2 = {2};
        stdOut.println(findMedianSortedArrays(nums1, nums2));
        stdOut.println(findMedianSortedArrays2(nums1, nums2));
        stdOut.println(findMedianSortedArrays3(nums1, nums2));
        stdOut.println(findKthNum(nums1, nums2, 2));
        stdOut.println(findKthNum2(nums1, nums2, 2));
    }
}
