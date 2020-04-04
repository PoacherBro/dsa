package binarysearch_test

import (
	"testing"

	"github.com/PoacherBro/dsa/algs/binarysearch"
)

func TestBinarySearch(t *testing.T) {
	sorted := []int{1, 45, 90, 100, 291, 311} // should be sorted
	ret := binarysearch.Search(45, sorted)
	if ret != 1 {
		t.Error("test failed, not found matched key")
	}

	notSorted := []int{23, 49, 90, 100, 95}
	ret = binarysearch.Search(95, notSorted)
	if ret != -1 {
		t.Errorf("unexpected that found `key` in not-sorted array (index=%d)", ret)
	}
}
