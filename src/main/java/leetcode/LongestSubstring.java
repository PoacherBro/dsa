package leetcode;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Leo on 2017/1/30
 */
public class LongestSubstring {
    /**
     * 利用两个指针i和j，i代表子字符串的起始位置，j代表末尾位置。通过一个Set，存储所有扫描的过得字符。
     * 同时维护一个最大子字符串长度。
     *
     * 首先j向后移动
     * 1. 如果当前字符（j位置）没有在Set里，就把就j字符放入Set里，并且判断当前最大子字符串长度和Set的大小。
     * 2. 如果当前字符已经存在Set里，则把i向后移一位，并删掉i字符
     *
     * @param s 输入的字符串
     * @return 最大不重复的子字符串长度
     */
    private static int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) return 0;

        int length = 0;
        int i = 0, j = 0;
        Set<Character> store = new HashSet<>();

        while (j < s.length()) {
            if (!store.contains(s.charAt(j))) {
                store.add(s.charAt(j++));
                length = Math.max(length, store.size());
            } else {
                store.remove(s.charAt(i++));
            }
        }
        return length;
    }

    /**
     * 利用Map来存储扫描过得字符。
     *
     * 保留两个指针，i和j(j <= i)，从头扫描字符串，如果字符已经在Map里，则把j移到相同字符的右边。
     * 这样substring的长度就一直是 i - j + 1
     *
     * @param s 输入的字符串
     * @return 最大不重复的子字符串长度
     */
    private static int lengthOfLongestSubstring2(String s) {
        if (s.length() == 0) return 0;

        Map<Character, Integer> map = new HashMap<>(s.length());
        int max = 0;
        for (int i = 0, j = 0; i < s.length(); i++) {
            if (map.containsKey(s.charAt(i))) {
                j = Math.max(j, map.get(s.charAt(i)) + 1);
            }
            map.put(s.charAt(i), i);
            max = Math.max(max, i - j + 1);
        }
        return max;
    }

    public static void main(String[] args) {
        PrintStream stdOut = System.out;
        String s = "abcabcbb";
        stdOut.print(lengthOfLongestSubstring(s));
        stdOut.println();
        stdOut.print(lengthOfLongestSubstring2(s));
    }
}
