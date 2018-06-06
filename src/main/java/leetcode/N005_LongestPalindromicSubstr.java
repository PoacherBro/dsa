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
     *
     *
     * @param s 输入字符串
     * @return 返回最长回文子串
     */
    private static String longestPalindrome(String s) {
        assert s != null;

        if (s.isEmpty() || s.length() == 1) {
            return s;
        }

        int strLen = s.length();
        String substr = "";

        return null;
    }

    // 测试用例
    public static void main(String[] args) {
        System.out.println(longestPalindrome("ababac"));
        System.out.println(longestPalindrome("bbbbb"));
        System.out.println(longestPalindrome("babddb"));
    }
}
