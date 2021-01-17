package bag_test

import (
	"errors"
	"fmt"
	"testing"

	"github.com/PoacherBro/dsa/datastrutctures/bag"
)

var breakIteration = errors.New("break iteration")

// go test -v -timeout 3s -run ^TestArrayBag$
func TestArrayBag(t *testing.T) {
	b := bag.NewArrayBag(10)
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

	iterateCb := func(item interface{}) error {
		fmt.Println(item)
		return nil
	}
	b.Iterate(iterateCb)
}

// go test -v -timeout 3s -run ^TestLinkBag$
func TestLinkBag(t *testing.T) {
	b := bag.NewLinkBag()
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

	iterateCb := func(item interface{}) error {
		fmt.Println(item)
		return nil
	}
	b.Iterate(iterateCb)
}
