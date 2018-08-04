package algorithms;

import java.util.Stack;

/**
 * @author leo on 2/14/17.
 */
public class QuickSort {
    /*
     * 快速排序是平时使用最多的一种排序方法，从它的名字可以知道，它排序的速度很快。平均时间复查度为O(n*logn)，最坏的情况是O(n^2)。
     *
     * 先来理解快速排序的思想。其实快速排序是从冒泡排序演变过来的，冒泡排序时两两临近的数字相比较，
     * 而快速排序是把最小数和最大数相比较，从而节省时间。
     *
     * 快速排序会首先找到一个轴 pivot，一般以数组第一个元素作为轴。然后把小于pivot的放到左边，大于的放在右边。
     * 然后把左右两边按照相同的方法，继续选择各自的pivot排序，直到不能分裂成子数组为止。
     */

    /********************************************** 递归实现 *************************************************/
    private static void quickSortRecursive(int[] arr) {
        if (arr.length <= 1) {
            return;
        }

        sort(arr, 0, arr.length - 1);
    }

    // 数组分区，左小右大
    private static int partition(int[] arr, int startIndex, int endIndex) {
        int pivot = arr[startIndex];
        int i = startIndex + 1, j = endIndex;
        while (i < j) {
            // 以第一个元素为pivot，那么就先从尾端开始遍历
            while (arr[j] > pivot && i < j) {
                j--;
            }
            while (arr[i] < pivot && i < j) {
                i++;
            }
            swap(arr, i, j);
        }
        // i == j，因为pivot是在左边，这时候就看当前元素是不是小于或者等于pivot
        // 如果是，则直接交换，当前index就是分界点
        // 如果不是，那么就不需要交换，但是分界点则是j的前一个
        if (arr[j] <= pivot) {
            swap(arr, i, startIndex);
        } else {
            j--;
        }
        return j;
    }

    // 另一种做排序的实现，和上一种做法一样，只是不需要其他的临时变量和swap方法处理
    private static int partition2(int[] arr, int startIndex, int endIndex) {
        int pivot = arr[startIndex];

        while (startIndex < endIndex) {
            while (startIndex < endIndex && arr[endIndex] > pivot) {
                endIndex--;
            }
            arr[startIndex] = arr[endIndex];
            while (startIndex < endIndex && arr[startIndex] < pivot) {
                startIndex++;
            }
            arr[endIndex] = arr[startIndex];
        }

        // 此时，startIndex == endIndex
        arr[startIndex] = pivot;
        return startIndex;
    }

    // 另一种排序的方法，这种思想更直观，就是利用一个变量storeIndex来保存pivot的最终位置。
    // 初始storeIndex位置，选择一个数字作为pivot，并把pivot放到末尾（或开头），然后遍历除了pivot的元素
    // 1. 如果这个元素比pivot小，则把当前元素和storeIndex交换，storeIndex移动，代表下一个可交换的位置
    //    其中，如果storeIndex初始是开始位置，那么storeIndex++；如果在末尾，则是storeIndex--
    // 2. 如果这个元素比pivot大或者相等，则不做任何事
    // 最后再把storeIndex和pivot之前的位置交换一下
    //
    // 具体可以看 http://bubkoo.com/2014/01/12/sort-algorithm/quick-sort的实例分析。
    private static int partition3(int[] arr, int startIndex, int endIndex) {
        // 随便选择一个pivot数
        int pivot = arr[endIndex];
        int storeIndex = startIndex;
        for (int i = startIndex + 1; i < endIndex; i++) {
            if (arr[i] < pivot) {
                swap(arr, storeIndex, i);
                storeIndex++; // 交换位置之后，storeIndex加1，代表下一个可以替换的位置
            }
        }
        swap(arr, endIndex, storeIndex); // 把pivot放到正确位置

        return storeIndex;
    }

    private static void sort(int[] arr, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }
        int pivotIndex = partition(arr, startIndex, endIndex);
        sort(arr, startIndex, pivotIndex - 1);
        sort(arr, pivotIndex + 1, endIndex);
    }

    //********************************************** End *************************************************

    //****************************************** 非递归实现 ************************************************
    private static void quickSort(int[] arr) {
        // 利用栈来保存每次分区的边界位置
        Stack<Integer> pivot = new Stack<>();

        pivot.push(0);
        pivot.push(arr.length - 1);

        while (!pivot.isEmpty()) {
            int end = pivot.pop();
            int start = pivot.pop();

            int mid = partition(arr, start, end);

            if (mid - 1 > start) {
                pivot.push(start);
                pivot.push(mid - 1);
            }
            if (mid + 1 < end) {
                pivot.push(mid + 1);
                pivot.push(end);
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //Test
    public static void main(String[] args) {
        int[] arr = {5, 8, 3, 2, 9, 4};
        quickSortRecursive(arr);
//        quickSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
