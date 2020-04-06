package iterable

type Objective interface {
	GenIterator() IteratorObj
}

type IteratorObj interface {
	HasNext() bool
	Next() interface{}
}
