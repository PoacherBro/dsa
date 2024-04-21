package bag

import (
	"sync"
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
	b.locker.RLock()
	defer b.locker.RUnlock()
	for _, d := range b.data {
		if d == item {
			return true
		}
	}
	return false
}

// =================== implement Array end ===============================

// =================== implement Link start ===============================
type linkBag[T comparable] struct {
	locker sync.RWMutex
	head   *node[T]
	tail   *node[T]
	size   int
}

type node[T comparable] struct {
	data T
	pre  *node[T]
	next *node[T]
}

// NewLinkBag create a `Bag` which is implemented by link underline
func NewLinkBag[T comparable]() Bag[T] {
	return &linkBag[T]{}
}

func (b *linkBag[T]) Add(item T) {
	b.locker.Lock()
	defer b.locker.Unlock()
	n := &node[T]{
		data: item,
		pre:  nil,
		next: nil,
	}

	if b.head == nil {
		b.head = n
		b.tail = n
	} else {
		n.pre = b.tail
		b.tail.next = n
		b.tail = n
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
	return b.head == nil
}

func (b *linkBag[T]) IsExists(item T) bool {
	b.locker.RLock()
	defer b.locker.RUnlock()

	if b.size == 0 {
		return false
	}
	h, t := b.head, b.tail
	for i := 0; i <= b.size/2; i++ {
		if h.data == item || t.data == item {
			return true
		}
		h = h.next
		t = t.pre
	}
	return false
}
