package iterable

type CallbackFn func(item any) bool

// CallbackIterable iterate all items based on passed callback, will break when return error from callback
type CallbackIterable interface {
	Iterate(CallbackFn) // if `CallBackFn` return error, then break the iterator
}
