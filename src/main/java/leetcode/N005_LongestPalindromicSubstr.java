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
    private static String longestPalindrome(String s) {
        assert s != null;

        int len = s.length();
        if (s.isEmpty() || len == 1) {
            return s;
        }

        // 存储最长回文子串
        String substr = "";

        // 可以直接从第二个字符开始
        for (int i = 1; i < len - 1; i++) {
            int m = i - 1, n = i + 1, tmpLenght;
            // 考虑回文长度为奇数情况，此时 i 为轴
            while (m >= 0 && n < len) {
                if (s.charAt(m) == s.charAt(n)) {
                    m --;
                    n ++;
                } else {
                    break;
                }
            }

            // 计算回文长度
            tmpLenght = n - m - 1;
            if (substr.length() < tmpLenght) {
                substr = s.substring(m + 1, n);
            }

            // 回文字符串为偶数情况
            m = i;
            n = i + 1;
            while (m >= 0 && n < len) {
                if (s.charAt(m) == s.charAt(n)) {
                    m --;
                    n ++;
                } else {
                    break;
                }
            }

            tmpLenght = n - m - 1;
            if (substr.length() < tmpLenght) {
                substr = s.substring(m + 1, n);
            }
        }

        return substr;
    }

    // 测试用例
    public static void main(String[] args) {
        System.out.println(longestPalindrome("ababac"));
        System.out.println(longestPalindrome("bbbbb"));
        System.out.println(longestPalindrome("babddb"));
    }
}
