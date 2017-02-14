package leetcode;

/**
 * @author Leo on 2017/1/29
 */

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

public class AddTwoNumbers {
    private static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode a1 = l1;
        ListNode a2 = l2;
        ListNode result = new ListNode(0);
        ListNode d = result;

        // 进位数，两数相加大于10，则需要进一位
        int sum = 0;
        while (a1 != null || a2 != null) {
            if (a1 != null) {
                sum += a1.val;
                a1 = a1.next;
            }
            if (a2 != null) {
                sum += a2.val;
                a2 = a2.next;
            }
            d.next = new ListNode(sum % 10);
            sum = sum / 10;

            d = d.next;
        }
        if (sum > 0) {
            d.next = new ListNode(sum);
        }
        // 去除第一个初始化的节点
        return result.next;
    }

    public static void main(String[] args) {
        int[] arr1 = {2, 4, 5};
        int[] arr2 = {5, 6, 4};

        ListNode l1 = createListNode(arr1);
        ListNode l2 = createListNode(arr2);

        ListNode result = addTwoNumbers(l1, l2);
        while (result != null) {
            System.out.print(result.val);
            result = result.next;
        }
    }

    private static ListNode createListNode(int[] arr) {
        ListNode l = new ListNode(arr[0]);
        ListNode d = l;
        for (int i = 1; i < arr.length; i++) {
            d.next = new ListNode(arr[i]);
            d = d.next;
        }
        return l;
    }
}
