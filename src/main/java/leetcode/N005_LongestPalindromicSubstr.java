package leetcode;

/**
 * @author Leo on 2018/06/05
 *
 * Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000.
 *
 * Example 1:
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 *
 * Example 2:
 * Input: "cbbd"
 * Output: "bb"
 *
 */
public class N005_LongestPalindromicSubstr {
    // 存储最长回文子串的index
    private int low;
    // 最长回文子串的长度
    private int maxLen;

    /**
     * 解法1：
     * 回文的特点是从中间往两边看，字符串是一样的，那么可以从第一个字符开始往尾部循环：
     * 每到一个字符，往两边扩展，查找最长相同的字符串。
     *
     * 需要注意的是回文字符串长度可能是偶数，也可能是奇数，那么中间值就有两种情况了。
     *
     * 时间复杂度是 (n - 1) * (n/2 + n/2) = n^2 - n, O(n^2)
     *
     * @param s 输入字符串
     * @return 返回最长回文子串
     */
    private String longestPalindrome(String s) {
        assert s != null;

        int len = s.length();
        if (s.isEmpty() || len == 1) {
            return s;
        }

        // 重构后，不能从第二个开始，需要从第一个开始查找
        for (int i = 0; i < len - 1; i++) {
            expandPalindromic(s, i, i); // 回文子串长度为奇数
            expandPalindromic(s, i, i + 1);  // 回文子串长度为偶数
        }

        return s.substring(low, low + maxLen);
    }

    /**
     * 抽象一个方法来查找回文左右两边的最长子串
     * @param s 原输入字符串
     * @param m 回文左边开始查找的index
     * @param n 回文右边开始查找的index
     */
    private void expandPalindromic(String s, int m, int n) {
        while (m >= 0 && n < s.length() && s.charAt(m) == s.charAt(n)) {
            m --;
            n ++;
        }

        int tmpLenght = n - m - 1;
        if (maxLen < tmpLenght) {
            low = m + 1;
            maxLen = tmpLenght;
        }
    }

    // 测试用例
    public static void main(String[] args) {
        N005_LongestPalindromicSubstr longestPalindromic = new N005_LongestPalindromicSubstr();
        System.out.println(longestPalindromic.longestPalindrome("bb"));
        System.out.println(longestPalindromic.longestPalindrome("ababac"));
        System.out.println(longestPalindromic.longestPalindrome("bbbbb"));
        System.out.println(longestPalindromic.longestPalindrome("babddb"));
    }
}
