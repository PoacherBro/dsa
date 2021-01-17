package stack

import (
	"errors"
	"sync"
)

// ErrEmpty when pop for a empty queue
var ErrEmpty = errors.New("empty stack")

// Stack is FIFO structure
type Stack interface {
	Push(interface{})
	Pop() (interface{}, error)
	Size() int
	IsEmpty() bool
}

var (
	_ Stack = &arrayStack{}
	_ Stack = &linkStack{}
)

type arrayStack struct {
	locker sync.RWMutex
	data   []interface{}
}

// NewArrayStack create a queue by array
func NewArrayStack(cap int) Stack {
	if cap <= 0 {
		cap = 16
	}
	return &arrayStack{
		data: make([]interface{}, 0, cap),
	}
}

func (q *arrayStack) Push(item interface{}) {
	q.locker.Lock()
	defer q.locker.Unlock()
	q.data = append(q.data, item)
}

func (q *arrayStack) Pop() (interface{}, error) {
	q.locker.Lock()
	defer q.locker.Unlock()
	size := len(q.data)
	if size == 0 {
		return nil, ErrEmpty
	}
	item := q.data[size-1]
	q.data = q.data[:size-1]
	return item, nil
}

func (q *arrayStack) IsEmpty() bool {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return len(q.data) == 0
}

func (q *arrayStack) Size() int {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return len(q.data)
}

// =====================================

type node struct {
	data interface{}
	next *node
}

type linkStack struct {
	locker sync.RWMutex
	first  *node
	size   int
}

// NewLinkStack implement queue by link
func NewLinkStack() Stack {
	return &linkStack{}
}

func (q *linkStack) Push(item interface{}) {
	q.locker.Lock()
	defer q.locker.Unlock()
	n := &node{
		data: item,
	}
	if q.first == nil {
		q.first = n
	} else {
		n.next = q.first
		q.first = n
	}
	q.size++
}

func (q *linkStack) Pop() (interface{}, error) {
	q.locker.Lock()
	defer q.locker.Unlock()
	size := q.size
	if size == 0 {
		return nil, ErrEmpty
	}
	item := q.first.data
	q.first = q.first.next
	q.size--
	return item, nil
}

func (q *linkStack) IsEmpty() bool {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return q.size == 0
}

func (q *linkStack) Size() int {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return q.size
}
