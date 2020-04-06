package iterable_test

import (
	"fmt"
	"testing"

	"github.com/PoacherBro/dsa/pattern/iterable"
)

type objArrayIterator struct {
	index int
	data  []interface{}
}

func (b *arrBag) GenIterator() iterable.IteratorObj {
	return &objArrayIterator{
		index: 0,
		data:  b.data,
	}
}

func (it *objArrayIterator) HasNext() bool {
	return it.index < len(it.data)
}

func (it *objArrayIterator) Next() interface{} {
	var result interface{}
	if it.index < len(it.data) {
		result = it.data[it.index]
		it.index++
	}
	return result
}

type objLinkIterator struct {
	iNode *node
}

func (b *linkBag) GenIterator() iterable.IteratorObj {
	return &objLinkIterator{
		iNode: b.first,
	}
}

func (it *objLinkIterator) HasNext() bool {
	return it.iNode != nil
}

func (it *objLinkIterator) Next() interface{} {
	result := it.iNode
	if it.iNode != nil {
		it.iNode = it.iNode.next
	}
	return result
}

func BenchmarkIterateBag_Objective_Array(b *testing.B) {
	ab := newArrBag(1e6)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		it := ab.GenIterator()
		for _ = it.Next(); it.HasNext(); _ = it.Next() {
		}
	}
}

func BenchmarkIterateBag_Objective_Link(b *testing.B) {
	ab := newLinkBag(1e6)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		it := ab.GenIterator()
		for _ = it.Next(); it.HasNext(); _ = it.Next() {
		}
	}
}

func TestIterateBag_Objective_Array(t *testing.T) {
	ab := newArrBag(10)
	it := ab.GenIterator()
	for it.HasNext() {
		fmt.Printf("%v\t", it.Next())
	}
	fmt.Println()
}

func TestIterateBag_Objective_Link(t *testing.T) {
	ab := newLinkBag(10)
	it := ab.GenIterator()
	for it.HasNext() {
		fmt.Printf("%v\t", it.Next())
	}
	fmt.Println()
}
