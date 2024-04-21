package bag_test

import (
	"testing"

	"github.com/PoacherBro/dsa/datastrutctures/bag"
)

// go test -v -timeout 3s -run ^TestArrayBag$
func TestArrayBag(t *testing.T) {
	b := bag.NewArrayBag[int](10)
	b.Add(1)
	b.Add(2)
	size := b.Size()
	empty := b.IsEmpty()
	if size != 2 {
		t.Fatal("bag size incorrect")
	}
	if empty {
		t.Fatal("bag should not be empty")
	}

	// iterateCb := func(item interface{}) bool {
	// 	fmt.Println(item)
	// 	return false
	// }
	// b.Iterate(iterateCb)
}

// go test -v -timeout 3s -run ^TestLinkBag$
func TestLinkBag(t *testing.T) {
	b := bag.NewLinkBag[int]()
	l := 10
	for i := 0; i < l; i++ {
		b.Add(i)
	}

	size := b.Size()
	empty := b.IsEmpty()
	if size != l {
		t.Fatal("bag size incorrect")
	}
	if empty {
		t.Fatal("bag should not be empty")
	}

	// iterateCb := func(item interface{}) bool {
	// 	fmt.Println(item)
	// 	return false
	// }
	// b.Iterate(iterateCb)
}

func BenchmarkArrayBag(b *testing.B) {
	b.Run("test add item into Array Bag", func(b *testing.B) {
		ab := bag.NewArrayBag[int](1000)
		for i := 0; i < b.N; i++ {
			ab.Add(i)
		}
	})
}

// BenchmarkArrayBag/test_add_item_into_Array_Bag-8         	43594042	        26.64 ns/op	      71 B/op	       0 allocs/op
