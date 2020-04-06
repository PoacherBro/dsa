package iterable

import "context"

// ChanIterable iterate all items when context.Done()
type ChanIterable interface {
	Chan(context.Context) chan interface{}
}
