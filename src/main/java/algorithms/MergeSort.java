package algorithms;

/**
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

        int start1 = low, end1 = mid;
        int start2 = mid + 1, end2 = high;

        mergeSortRecursive(arr, result, start1, end1);
        mergeSortRecursive(arr, result, start2, end2);

        int k = low;
        while (start1 <= end1 && start2 <= end2) {
            result[k++] = arr[start1] < arr[start2] ? arr[start1++] : arr[start2++];
        }
        while (start1 <= end1) {
            result[k++] = arr[start1++];
        }
        while (start2 <= end2) {
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
        //TODO
    }


    public static void main(String[] args) {
        int[] arr = {5, 8, 3, 4, 9, 7};
        int[] result = new int[arr.length];
        mergeSortRecursive(arr, result, 0, arr.length - 1);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
