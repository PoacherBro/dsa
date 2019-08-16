package main

import (
	"fmt"
	"math"
)

func myAtoi(str string) int {
	l := len(str)
	if l == 0 {
		return 0
	}

	numCount := 0
	flag := 0 // flag '-' then 1
	flagCount := 0
	result := 0
	rs := []rune(str)
	for i := 0; i < l; i++ {
		c := rs[i]
		if c >= '0' && c <= '9' && flagCount < 2 {
			numCount++
			result = result*10 + int(c-'0')
			if (result - 1) > math.MaxInt32 {
				break
			}
		} else if (c == '+' || c == '-') && numCount == 0 {
			if c == '-' {
				flag = 1
			}
			flagCount++
		} else if c == ' ' && numCount == 0 && flagCount == 0 {
			continue
		} else {
			break
		}
	}

	if flag == 1 {
		if -result > math.MinInt32 {
			return -result
		} else {
			return math.MinInt32
		}
	} else {
		if result > math.MaxInt32 {
			return math.MaxInt32
		} else {
			return result
		}
	}
}

func main() {
	print("-12131")
	print("dasdfad asdfa 12131")
	print("   - 12 2223")
	print("4193 with words")
	print("words and 987")
	print("-91283472332")
	print("2323你好")
}

func print(data string) {
	fmt.Println(fmt.Sprintf("myAtoi(%s)", data), myAtoi(data))
}
