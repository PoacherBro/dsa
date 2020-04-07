package iterable

type Callback func(interface{}) error

// CallbackIterable iterate all items based on passed callback, will break when return error from callback
type CallbackIterable interface {
	Iterate(Callback) // if return error, then break the iterator
}
