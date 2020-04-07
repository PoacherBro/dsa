package stack_test

import (
	"testing"

	"github.com/PoacherBro/dsa/datastrutctures/stack"
)

// go test -v -timeout 3s -run ^TestArrayStack$
func TestArrayStack(t *testing.T) {
	q := stack.NewArrayStack()
	testStack(q, t)
}

// go test -v -timeout 3s -run ^TestLinkStack$
func TestLinkStack(t *testing.T) {
	q := stack.NewLinkStack()
	testStack(q, t)
}

func testStack(q stack.Stack, t *testing.T) {
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
	if _, err := q.Pop(); err != stack.ErrEmpty {
		t.Fatal("queue pop empty queue without err")
	}
}
