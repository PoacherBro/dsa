package queue

import (
	"errors"
	"sync"
)

// ErrEmpty when pop for a empty queue
var ErrEmpty = errors.New("empty queue")

// Queue is FIFO structure
type Queue interface {
	Push(interface{})
	Pop() (interface{}, error)
	Size() int
	IsEmpty() bool
}

type arrayQueue struct {
	locker sync.RWMutex
	data   []interface{}
}

// NewArrayQueue create a queue by array
func NewArrayQueue() Queue {
	return &arrayQueue{}
}

func (q *arrayQueue) Push(item interface{}) {
	q.locker.Lock()
	defer q.locker.Unlock()
	q.data = append(q.data, item)
}

func (q *arrayQueue) Pop() (interface{}, error) {
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

func (q *arrayQueue) IsEmpty() bool {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return len(q.data) == 0
}

func (q *arrayQueue) Size() int {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return len(q.data)
}

// =====================================

type node struct {
	data interface{}
	next *node
}

type linkQueue struct {
	locker sync.RWMutex
	first  *node
	size   int
}

// NewLinkQueue implement queue by link
func NewLinkQueue() Queue {
	return &linkQueue{}
}

func (q *linkQueue) Push(item interface{}) {
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

func (q *linkQueue) Pop() (interface{}, error) {
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

func (q *linkQueue) IsEmpty() bool {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return q.size == 0
}

func (q *linkQueue) Size() int {
	q.locker.RLock()
	defer q.locker.RUnlock()
	return q.size
}
