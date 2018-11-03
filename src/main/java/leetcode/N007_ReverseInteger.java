package leetcode;

public class N007_ReverseInteger {

    /**
     * 和反转string一样，我们可以使用pop/push的方式来反转数字。
     * 只是可以通过数学方式避免使用array/stack额外的存储空间。
     * @param x
     * @return
     */
    public int reverse(int x) {
        int ret = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;

            if (ret > Integer.MAX_VALUE/10 || (ret == Integer.MAX_VALUE / 10 && pop > 7))
                return 0;
            
            if (ret < Integer.MIN_VALUE/10 || (ret == Integer.MIN_VALUE / 10 && pop < -8))
                return 0;

            ret = ret * 10 + pop;
        }
        return ret;
    }

    public static void main(String[] args) {
        N007_ReverseInteger ri = new N007_ReverseInteger();
        System.out.println(ri.reverse(123));
        System.out.println(ri.reverse(-123));
        System.out.println(ri.reverse(120));
        System.out.println(ri.reverse(-120));
        System.out.println(ri.reverse(Integer.MAX_VALUE));
        System.out.println(ri.reverse(Integer.MIN_VALUE));
    }
}