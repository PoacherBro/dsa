package bag

import (
	"sync"

	"github.com/PoacherBro/dsa/pattern/iterable"
)

// Bag structure will store unsorted items
type Bag interface {
	iterable.CallbackIterable

	Add(interface{})
	Empty() bool
	Size() int
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

func (b *arrBag) Iterate(cb iterable.Callback) {
	b.locker.RLock()
	defer b.locker.RUnlock()
	for _, item := range b.data {
		if err := cb(item); err != nil {
			return
		}
	}
}

// =================== implement Array end ===============================

// =================== implement Link start ===============================
type linkBag struct {
	locker sync.RWMutex
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
