package queue_test

import (
	"testing"

	"github.com/PoacherBro/dsa/datastrutctures/queue"
)

// go test -v -timeout 3s -run ^TestArrayQueue$
func TestArrayQueue(t *testing.T) {
	q := queue.NewArrayQueue()
	testQueue(q, t)
}

// go test -v -timeout 3s -run ^TestLinkQueue$
func TestLinkQueue(t *testing.T) {
	q := queue.NewLinkQueue()
	testQueue(q, t)
}

func testQueue(q queue.Queue, t *testing.T) {
	l := 10

	for i := 0; i < l; i++ {
		q.Push(i)
	}

	size := q.Size()
	empty := q.IsEmpty()
	if size != l {
		t.Fatalf("queue size get(%d) != expected(%d)", size, l)
	}
	if empty {
		t.Fatal("queue should not be empty")
	}

	for i := l; i > 0; i-- {
		item, err := q.Pop()
		if err != nil {
			t.Fatal(err)
		}

		it, ok := item.(int)
		if !ok {
			t.Fatal("queue pop item type is not `int`")
		}
		if it != i-1 {
			t.Fatalf("queue pop item get(%d) != expected(%d)", it, i-1)
		}
	}
	if _, err := q.Pop(); err != queue.ErrEmpty {
		t.Fatal("queue pop empty queue without err")
	}
}
