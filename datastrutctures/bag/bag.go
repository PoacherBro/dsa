package bag

import (
	"sync"

	"github.com/PoacherBro/dsa/pattern/iterable"
)

// Bag structure will store unsorted items
type Bag[T comparable] interface {
	Add(T)
	IsEmpty() bool
	Size() int
	IsExists(T) bool
}

var (
	_ Bag[string] = &arrBag[string]{}
	_ Bag[string] = &linkBag[string]{}
)

// =================== implement Array start ===============================

type arrBag[T comparable] struct {
	locker sync.RWMutex
	data   []T
	size   int
}

// NewArrayBag create a `Bag` which is implemented by array underline
func NewArrayBag[T comparable](cap int) Bag[T] {
	if cap <= 0 {
		cap = 16
	}
	return &arrBag[T]{
		data: make([]T, 0, cap),
	}
}

func (b *arrBag[T]) Add(item T) {
	b.locker.Lock()
	defer b.locker.Unlock()
	b.data = append(b.data, item)
	b.size++
}

func (b *arrBag[T]) IsEmpty() bool {
	return b.size == 0
}

func (b *arrBag[T]) Size() int {
	b.locker.RLock()
	defer b.locker.RUnlock()
	return b.size
}

func (b *arrBag[T]) IsExists(item T) bool {
	return false
}

func (b *arrBag[T]) Iterate(cb iterable.CallbackFn) {
	b.locker.RLock()
	defer b.locker.RUnlock()
	for _, item := range b.data {
		if isBreak := cb(item); isBreak {
			return
		}
	}
}

// =================== implement Array end ===============================

// =================== implement Link start ===============================
type linkBag[T comparable] struct {
	locker sync.RWMutex
	first  *node[T]
	size   int
}

type node[T comparable] struct {
	data  T
	next  *node[T]
	index int
}

// NewLinkBag create a `Bag` which is implemented by link underline
func NewLinkBag[T comparable]() Bag[T] {
	return &linkBag[T]{}
}

func (b *linkBag[T]) Add(item T) {
	b.locker.Lock()
	defer b.locker.Unlock()
	if b.first == nil {
		b.first = &node[T]{
			data:  item,
			index: 0,
		}
	} else {
		n := &node[T]{
			data:  item,
			index: b.first.index + 1,
			next:  b.first,
		}
		b.first = n
	}
	b.size++
}

func (b *linkBag[T]) Size() int {
	b.locker.RLock()
	defer b.locker.RUnlock()
	return b.size
}

func (b *linkBag[T]) IsEmpty() bool {
	b.locker.RLock()
	defer b.locker.RUnlock()
	return b.first == nil
}

func (b *linkBag[T]) IsExists(item T) bool {
	return false
}

func (b *linkBag[T]) Iterate(cb iterable.CallbackFn) {
	n := b.first
	for n != nil {
		if isBreak := cb(n); isBreak {
			return
		}
		n = n.next
	}
}
