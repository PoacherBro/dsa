package iterable

import "context"

// ChanIterable iterate all items when context.Done()
type ChanIterable[T any] interface {
	Chan(context.Context) chan T
}
