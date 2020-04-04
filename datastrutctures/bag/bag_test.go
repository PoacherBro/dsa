package bag_test

import (
	"fmt"
	"testing"

	"github.com/PoacherBro/dsa/datastrutctures/bag"
)

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

	for item := range b.Iterator() {
		fmt.Println(item)
	}
}
