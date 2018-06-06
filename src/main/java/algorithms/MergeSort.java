package algorithms;

/**
 * 归并排序是建立在归并操作上的排序，该算法采用分治法
 * 1. 把需要排序的数组依次划分为最小单元（1或者2个元素）
 * 2. 再把每个子单元依次合并
 *
 * 具体可参考：https://zh.wikipedia.org/wiki/%E5%BD%92%E5%B9%B6%E6%8E%92%E5%BA%8F
 *
 * @author Leo on 2017/2/14.
 */
public class MergeSort {
    /**
     * 递归实现
     *
     * @param arr 要排序的数组
     * @param result 临时存储数组
     * @param low 起始位置
     * @param high 结束位置
     */
    private static void mergeSortRecursive(int[] arr, int[] result, int low, int high) {
        if (low >= high) {
            return;
        }
        int length = high - low, mid = (length >> 1) + low;

        int start1 = low;
        int start2 = mid + 1;

        mergeSortRecursive(arr, result, start1, mid);
        mergeSortRecursive(arr, result, start2, high);

        int k = low;
        while (start1 <= mid && start2 <= high) {
            result[k++] = arr[start1] < arr[start2] ? arr[start1++] : arr[start2++];
        }
        while (start1 <= mid) {
            result[k++] = arr[start1++];
        }
        while (start2 <= high) {
            result[k++] = arr[start2++];
        }
        for (k = low; k <= high; k++) {
            arr[k] = result[k];
        }
    }

    /**
     * 非递归实现
     *
     * @param arr 要排序的数组
     */
    private static void mergeSort(int[] arr) {
        int length = arr.length;
        if (length <= 1) {
            return;
        }

        int[] result = new int[length];
        int block, start;

        // 最小的块为1，然后是block * 2的块合并
        for (block = 1; block < length * 2; block *= 2) {
            // 把数组按照块划分，然后合并
            for (start = 0; start < length; start += 2 * block) {
                int low = start;
                int mid = (start + block) < length ? (start + block) : length;
                int high = (start + 2 * block) < length ? (start + 2 * block) : length;

                // 两个块的起始位置以及结束位置
                int start1 = low;
                int start2 = mid;

                while (start1 < mid && start2 < high) {
                    result[low++] = arr[start1] < arr[start2] ? arr[start1++] : arr[start2++];
                }
                while (start1 < mid) {
                    result[low++] = arr[start1++];
                }
                while (start2 < high) {
                    result[low++] = arr[start2++];
                }
            }

            int[] temp = arr;
            arr = result;
            result = temp;
        }
    }


    public static void main(String[] args) {
        int[] arr = {5, 8, 3, 4, 4, 9, 7};
        int[] result = new int[arr.length];
        mergeSortRecursive(arr, result, 0, arr.length - 1);
        mergeSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
