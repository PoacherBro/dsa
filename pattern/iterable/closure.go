package iterable

// Iterator will return `item` and indicate whether has next item
type Iterator func() (interface{}, bool)

// ClosureIterable defined the iterator function which will return the item during iteration untile has no next
type ClosureIterable interface {
	Iterator() (Iterator, bool)
}
