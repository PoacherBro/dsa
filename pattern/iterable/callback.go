package iterable

// CallbackIterable iterate all items based on passed callback, will break when return error from callback
type CallbackIterable interface {
	Iterate(func(interface{}) error) // if return error, then break the iterator
}
