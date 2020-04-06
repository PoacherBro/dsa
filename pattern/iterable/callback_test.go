package iterable_test

import (
	"fmt"
	"testing"
)

func (b *arrBag) Iterate(cb func(interface{}) error) {
	var err error
	for _, item := range b.data {
		if err = cb(item); err != nil {
			break
		}
	}
}

func (b *linkBag) Iterate(cb func(interface{}) error) {
	var err error
	node := b.first
	for node != nil {
		if err = cb(node.data); err != nil {
			break
		}
		node = node.next
	}
}

func TestIterateBag_Callback_Array(t *testing.T) {
	ab := newArrBag(10)
	cb := func(item interface{}) error {
		fmt.Printf("%v\n", item)
		return nil
	}
	ab.Iterate(cb)
}

func TestIterateBag_Callback_Link(t *testing.T) {
	ab := newLinkBag(10)
	cb := func(item interface{}) error {
		fmt.Printf("%v\n", item)
		return nil
	}
	ab.Iterate(cb)
}

func BenchmarkIterateBag_Callback_Array(b *testing.B) {
	ab := newArrBag(1e6)
	cb := func(interface{}) error {
		return nil
	}
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		ab.Iterate(cb)
	}
}

func BenchmarkIterateBag_Callback_Link(b *testing.B) {
	ab := newLinkBag(1e6)
	cb := func(interface{}) error {
		return nil
	}
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		ab.Iterate(cb)
	}
}
