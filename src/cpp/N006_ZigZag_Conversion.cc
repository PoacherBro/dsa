#include <iostream>
#include <string>
#include <vector>

using namespace std;

/**
 * 解法1：使用两个变量 - 当前字符以及当前字符的方向
 * 思路：从左到右遍历字符串，对于每个字符计算应该所在行数，利用一个数组来存储每行
 * 应该显示什么字符串。而且可以知道，每到第一行和最后一行时，应该要转向。
 *
 * 算法复杂度 O(n), n = len(s)
 * 空间复杂度 O(n)
 *
 */
string convert(string s, int numRows)
{
   if (numRows == 1) return s;
  
   vector<string> rows(min(numRows, int(s.size())));
   int curRow = 0;
   bool goingDown = false;

   for (char c : s) 
   {
       rows[curRow] += c;
       if (curRow == 0 || curRow == numRows - 1)
           goingDown = !goingDown;

       curRow += goingDown ? 1 : -1;
   }
   
   string ret;
   for (string row : rows) 
   {
       cout << row << endl;
       ret += row;
   }
  
   return ret;

}

/*
 * 解法2：利用规律来计算每个字符所在位置
 * 思路：找到所有字符坐标的规律，利用公式计算输出字符应该如何表示。从第一行开始，一直到最后一行。
 * 可以发现，按照Z型来看，同一行的字符在原字符串里坐标相差 2*numRows-2，总结来说规律就是：
 * 对于每行的第 k 个字符，在原字符串的坐标是：
 * 1) 对于第一行，坐标是 k*(2*numRows-2)
 * 2) 对于最后一行，坐标是 k*(2*numRows-2)+(numRows-1)
 * 3) 对于中间行 i，坐标是 k*(2*numRows-2)+i和(k+1)(2*numRows-2)-i
 *
 * 时间复杂度为 O(n), n = len(s)
 * 空间复杂度为 O(n)，对于C++来说，如果返回字符串不考虑额外空间，空间复杂度是O(1)
 * 
 */
string convert2(string s, int numRows)
{
    if (numRows == 1) return s;

    string ret;
    int n = s.size();
    int cycleLen = 2 * numRows - 2;

    for (int i = 0; i < numRows; i++) 
    {
        for (int j = 0; j + i < n; j += cycleLen)
        {
            ret += s[i + j];
            if (i != 0 && i != numRows - 1 && j + cycleLen - i < n)
                ret += s[j + cycleLen - i];
        }

    }
    return ret;
}

int main()
{
    std::cout << convert("PAYPALISHIRING", 3) << std::endl;
    std::cout << convert("PAYPALISHIRING", 4) << std::endl;

    std::cout << convert2("PAYPALISHIRING", 3) << std::endl;
    std::cout << convert2("PAYPALISHIRING", 4) << std::endl;
}
