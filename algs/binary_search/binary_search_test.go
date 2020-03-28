package binary_search_test

import (
	"testing"

	"github.com/PoacherBro/dsa/algs/binary_search"
)

func TestBinarySearch(t *testing.T) {
	a := []int{1, 45, 90, 100, 291, 311}
	ret := binary_search.Search(45, a)
	if ret != 1 {
		t.Error("test failed, not found matched key")
	}
}
