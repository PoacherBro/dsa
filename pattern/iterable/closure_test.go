package iterable_test

import (
	"fmt"
	"testing"

	"github.com/PoacherBro/dsa/pattern/iterable"
)

type arrBag struct {
	data []interface{}
	size int
}

func newArrBag(size int) *arrBag {
	b := &arrBag{
		data: make([]interface{}, 0, size),
		size: size,
	}
	for i := 0; i < size; i++ {
		b.data = append(b.data, i)
	}
	return b
}

func (a *arrBag) Iterator() (iterable.Iterator, bool) {
	index := 0
	var item interface{}
	return func() (interface{}, bool) {
		if index < a.size {
			item = a.data[index]
			index++
		} else {
			item = nil
		}
		return item, index < a.size
	}, index < a.size
}

func TestIterateBag_Closure_Array(t *testing.T) {
	a := newArrBag(10)
	var item interface{}
	it, hasNext := a.Iterator()
	for hasNext {
		item, hasNext = it()
		fmt.Printf("%v\n", item)
	}
}

type node struct {
	data interface{}
	next *node
}
type linkBag struct {
	first *node
	size  int
}

func newLinkBag(size int) *linkBag {
	b := new(linkBag)
	b.size = size
	var pre *node
	for i := 0; i < b.size; i++ {
		item := &node{
			data: i,
		}
		if i == 0 {
			b.first = item
			pre = item
		}
		pre.next = item
		pre = item
	}
	return b
}

func (b *linkBag) Iterator() (iterable.Iterator, bool) {
	item := b.first
	return func() (interface{}, bool) {
		current := item
		if current != nil {
			item = current.next
		}
		return current, item != nil
	}, item != nil && item.next != nil
}

func TestIterateBag_Closure_Link(t *testing.T) {
	a := newLinkBag(10)
	var item interface{}
	it, hasNext := a.Iterator()
	for hasNext {
		item, hasNext = it()
		fmt.Printf("%v\n", item)
	}
}

func BenchmarkIterateBag_Closure_Array(b *testing.B) {
	ab := newArrBag(1e6)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		it, hasNext := ab.Iterator()
		for hasNext {
			_, hasNext = it()
		}
	}
}

func BenchmarkIterateBag_Closure_Link(b *testing.B) {
	lb := newLinkBag(1e6)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		it, hasNext := lb.Iterator()
		for hasNext {
			_, hasNext = it()
		}
	}
}
