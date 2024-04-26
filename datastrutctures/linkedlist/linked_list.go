package linkedlist

type LinkNode[T comparable] struct {
	next *LinkNode[T]
	data T
}

// LinkedList 单链表，先加入的在 head
type LinkedList[T comparable] struct {
	head *LinkNode[T]
	size int
}

func NewLinkedList[T comparable]() *LinkedList[T] {
	return &LinkedList[T]{}
}

// Add a item at front
func (ll *LinkedList[T]) Add(item T) {
	node := &LinkNode[T]{
		next: ll.head,
		data: item,
	}
	ll.head = node
	ll.size++
}

// Delete all nodes whose `data` is `item`.
// return deleted count.
func (ll *LinkedList[T]) Delete(item T) int {
	if ll.head == nil {
		return 0
	}

	deletedCount := 0
	pre, current := ll.head, ll.head
	for current != nil {
		// 如果head节点匹配，那么直接删除头节点，继续下移
		if ll.head.data == item {
			ll.head = ll.head.next
			pre, current = ll.head, ll.head
			deletedCount++
			ll.size--
			continue
		}

		// 如果匹配上，删除节点
		if current.data == item {
			pre.next = current.next
			current = current.next
			ll.size--
			deletedCount++
		} else { // 如果未匹配，则将 pre / current 向后移
			pre = current
			current = current.next
		}
	}

	return deletedCount
}

func (ll *LinkedList[T]) Find(item T) bool {
	for current := ll.head; current != nil; current = current.next {
		if current.data == item {
			return true
		}
	}
	return false
}
