package linkedlist_test

import (
	"testing"

	"github.com/PoacherBro/dsa/datastrutctures/linkedlist"
)

func TestLinkedList_Remove(t *testing.T) {
	ll := linkedlist.NewLinkedList[int]()
	ll.Add(2)
	ll.Add(2)
	ll.Add(3)
	ll.Add(5)
	ll.Add(2)
	ll.Add(4)
	ll.Add(2)

	count := ll.Delete(2)

	if count != 4 {
		t.Fatalf("should deleted total 4 items, but deleted %d", count)
	}

}
