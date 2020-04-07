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
	b := bag.NewArrayBag()
	b.Add(1)
	b.Add(2)
	size := b.Size()
	empty := b.Empty()
	if size != 2 {
		t.Error("bag size incorrect")
	}
	if empty {
		t.Error("bag should not be empty")
	}

	iterateCb := func(item interface{}) error {
		fmt.Println(item)
		return nil
	}
	b.Iterate(iterateCb)
}
