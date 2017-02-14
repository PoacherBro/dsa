package algorithms;

/**
 * @author leo on 2/14/17.
 */
public class QuickSort {
    /**
     * 快速排序是平时使用最多的一种排序方法，从它的名字可以知道，它排序的速度很快。平均时间复查度为O(n*logn)，最坏的情况是O(n^2)。
     *
     * 先来理解快速排序的思想。其实快速排序是从冒泡排序演变过来的，冒泡排序时两两临近的数字相比较，
     * 而快速排序是把最小数和最大数相比较，从而节省时间。
     *
     * 快速排序会首相找到一个轴 pivot，一般以数组第一个元素作为轴。然后把小于pivot的放到左边，大于的放在右边。
     * 然后把左右两边按照相同的方法，继续选择各自的pivot排序，直到不能分裂成子数组为止。
     */

    // 递归实现
    private static void quickSort(int[] arr) {
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
        // i == j，因为pivot时在左边，这时候就看当前元素是不是小于或者等于pivot
        // 如果是，则直接交换，当前index就是分界点
        // 如果不是，那么就不需要交换，但是分界点则是j的前一个
        if (arr[j] <= pivot) {
            swap(arr, i, startIndex);
        } else {
            j--;
        }
        return j;
    }

    private static void sort(int[] arr, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }
        int pivotIndex = partition(arr, startIndex, endIndex);
        sort(arr, startIndex, pivotIndex - 1);
        sort(arr, pivotIndex + 1, endIndex);
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //Test
    public static void main(String[] args) {
        int[] arr = {5, 8, 3, 2, 9, 4};
        quickSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
