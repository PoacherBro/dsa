package main

import (
	"fmt"
	"math"
)

// int32 -2147483648～2147483647
func reverse(x int) int {
	if x >= math.MaxInt32 || x <= math.MinInt32 {
		return 0
	}
	ret := 0
	for {
		if x == 0 {
			break
		}
		t := x % 10
		if ret > math.MaxInt32/10 || (ret == math.MaxInt32/10 && t > 7) {
			return 0
		}
		if ret < math.MinInt32/10 || (ret == math.MinInt32/10 && t < -8) {
			return 0
		}
		ret = ret*10 + t
		x /= 10
	}
	return ret
}

func main() {
	fmt.Printf("reverse(321)=%d\n", reverse(321))
	fmt.Printf("reverse(-3456)=%d\n", reverse(-3456))
	fmt.Printf("reverse(120)=%d\n", reverse(120))
}
