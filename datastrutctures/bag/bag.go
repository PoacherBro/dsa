package bag

import "sync"

// Bag structure will store unsorted items
type Bag interface {
	Add(interface{})
	Empty() bool
	Size() int
	Iterator() []interface{} // used for a standard iterator for golang
}

// =================== implement Array start ===============================
type arrBag struct {
	locker sync.RWMutex
	data   []interface{}
	size   int
}

// NewArrayBag create a `Bag` which is implemented by array underline
func NewArrayBag() Bag {
	return &arrBag{
		data: make([]interface{}, 0),
		size: 0,
	}
}

func (b *arrBag) Add(item interface{}) {
	b.locker.Lock()
	defer b.locker.Unlock()
	b.data = append(b.data, item)
	b.size++
}

func (b *arrBag) Empty() bool {
	return b.size == 0
}

func (b *arrBag) Size() int {
	b.locker.RLock()
	defer b.locker.RUnlock()
	return b.size
}

func (b *arrBag) Iterator() []interface{} {
	b.locker.RLock()
	defer b.locker.RUnlock()
	return b.data
}

// =================== implement Array end ===============================

// =================== implement Link start ===============================
type linkBag struct {
	locker sync.RUnlock
	first  *node
	size   int
}

type node struct {
	data  interface{}
	next  *node
	index int
}

// NewLinkBag create a `Bag` which is implemented by link underline
// func NewLinkBag() Bag {
// 	return &linkBag{}
// }

// func (b *linkBag) Add(item interface{}) {
// 	b.locker.Lock()
// 	defer b.locker.Unlock()

// }
