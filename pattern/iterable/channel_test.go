package iterable_test

import (
	"context"
	"fmt"
	"testing"
)

func (b *arrBag) Chan(ctx context.Context) <-chan interface{} {
	buf := make(chan interface{})
	go func() {
		defer close(buf)
		for _, item := range b.data {
			select {
			case <-ctx.Done():
				return
			default:
				buf <- item
			}
		}
	}()
	return buf
}

func (b *linkBag) Chan(ctx context.Context) <-chan interface{} {
	buf := make(chan interface{})
	node := b.first
	go func() {
		defer close(buf)
		for node != nil {
			select {
			case <-ctx.Done():
				return
			default:
				buf <- node.data
				node = node.next
			}
		}
	}()
	return buf
}

func TestIterateBag_Channel_Array(t *testing.T) {
	ab := newArrBag(10)
	for item := range ab.Chan(context.Background()) {
		fmt.Printf("%v\n", item)
	}
}

func TestIterateBag_Channel_Link(t *testing.T) {
	ab := newLinkBag(10)
	for item := range ab.Chan(context.Background()) {
		fmt.Printf("%v\n", item)
	}
}

func BenchmarkIterateBag_Channel_Array(b *testing.B) {
	ctx := context.Background()
	ab := newArrBag(1e6)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		for _ = range ab.Chan(ctx) {
		}
	}
}

func BenchmarkIterateBag_Channel_Link(b *testing.B) {
	ctx := context.Background()
	ab := newLinkBag(1e6)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		for _ = range ab.Chan(ctx) {
		}
	}
}
