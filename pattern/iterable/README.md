# Golang Iterator 设计

在学习《算法》这本书时，第一章有一个 `Bag` 的数据结构，它定义为不能删除元素的集合。之前看到一篇文章，说数据结构底层结构其实只有数组与链表，所以在实现 `Bag` 时想使用数组和链表分别实现。  

但是在设计 `Bag` API 时发现，该如何提供一个通用的遍历方式给第三方调用？因为不像 Java，Golang 并未提供一个迭代接口，只是原生语法上支持对一些容器的遍历，如下所示：  

```go
// 如果 container 是数组或者 Slice，那么 k => index，v => 数组元素；
// 如果 container 是 Map，那么 k => key，v => value；
for k, v := range container {
}

// 对于 channel 也是使用 range 方式，但是返回值不同
for item := range channel {
}
```

所以对于自定义结构，需要设计一个通用的 API 进行遍历，即 Iterator 模式。  
对于 Golang 来实现一些数据结构，Iterator 模式是比较重要的。  

这里先设定 `Bag` 两种不同底层存储结构的定义：  
```go
// 使用数组做底层存储
type arrBag struct {
    data []interface{}
    size int
}

// 使用链表做底层存储
type node struct {
    data interface{}
    next *node
}
type linkBag struct {
    node *node
    size int
}
```

在参考了几篇博主的文章后，基于自身理解想到以下几种实现方式。  


## 模式1：回调机制  

定义一个 `Iterable` 接口，如下：  

```go
// if need to break, return error
type IterableCallback func(interface{}) error

type Iterable interface {
    Iterator(IterableCallback)
}
```

然后在对象中实现对应的 Iterator() 方法，改方法接受 callback 函数作为参数，默认会把 `Bag` 的每个数据迭代然后作为 callbak 参数传入，如果 callback 返回 `error`，那么就会中断迭代。  

```go
// Array Bag
func (b *arrBag) Iterator(cb IterableCallback) {
    var err error
    for i := 0; i < len(b.data); i++ { // b.data => []interface{}
        if err = cb(b.data[i]); err != nil {
            break
        }
    }
}

// Link Bag
func (b *linkBag) Iterator(cb IterableCallback) {
    var err error
    for item := b.first; item != nil; item = item.next {
        if err = cb(item); err != nil {
            break
        }
    }
}
```

那么对于调用方来说，把对迭代操作通过 callback 函数传递过来即可。  


```go
b := NewBag()

// case 1: find a item
var found interface{}
b.Iterator(func(item interface{}) error {
    if item == matched {
        found = item
        return errors.New("iterator break")
    }
    return nil
})

// case 2: sum
var sum int
b.Iterator(func(i interface{}) error {
    item := i.(int)
    sum += item
    return nil
})
```

这种实现方式简单明了，很好的利用 Golang 的函数编程特性，但有一个不好的点是需预先定义好函数类型以及参数类型，不太好扩展。不过整体看这个无伤大雅，毕竟大部分情况是可以满足的。  

## 模式2：闭包

类似 Python 或 JavaScript 的 generator 方式，使用闭包来控制迭代进度。  

```go
// For Array underlying implementation
func (b *arrBag) Iterator() (func() (interface{}, bool), bool) {
    index := 0
    var item interface{}
    return func() (interface{}, bool) {
        if index < a.size {
            item = a.data[index]
            index++
        } else {
            item = nil
        }
        return item, index < a.size
    }, index < a.size
}

// For Link underlying implementation
func (b *linkBag) Iterator() (func() (interface{}, bool), bool) {
    item := b.node
    return func() (interface{}, bool) {
        current := item
        if current != nil {
            item = current.next
        }
        return current, item != nil
    }, item != nil && item.next != nil
}
```

使用示例:  

```go
it, hasNext := a.Iterator()
for hasNext {
    item, hasNext = it()
    fmt.Printf("%v\n", item)
}
```

## 模式3：使用 `chan`  

从开头我们知道，除了 slice 和 map 的 `for-range` 形式外，Golang 也提供对 channel 的 `for-range` 语句操作。根据此特性，也可以通过 channel 来实现迭代。  

首先定义一个通用的 interface：  

```go
type ChanIterable interface {
    Chan(context.Context) chan interface{}
}
```

这里的 `context` 主要是为了控制终止迭代，也可以使用特定的 `chan`，譬如 `stop := make(chan struct{})`，把 `stop` 传入。虽然 Golang 推荐使用 `context` 作为 API 设计的第一个参数，但是这里感觉其实使用 `chan` 更好，因为调用者会有意识的做 `close(chan)` 动作，而对于 `context`，估计会漏掉 cancel context 操作。

方法定义：  

```go
// For Array underlying implementation
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

// For Link underlying implementation
func (b *linkBag) Chan(ctx context.Context) <-chan interface{} {
    buf := make(chan interface{})
    node := b.node
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
```

那么使用上，可以这样：  

```go
ctx, cancel := context.WithCancel(context.Background())
// defer cancel() // 如果需要遍历所有，可以把 cancel() 放到 defer 处
for item := range ab.Chan(ctx) {
    if item == matched {
        cancel() // 保证中断遍历，资源回收
        return
    }
}
```

这里的实现是无缓冲的 `chan`，为了提高性能，也可以使用有缓冲。但是对于有缓冲的 `chan`，我们在控制内部 `close` 后需要消费掉缓冲区内的数据，否则依然有内存浪费。所以对于有缓冲，虽然性能提升（做了 benchmark，可以看到性能提升的不高），但需要多余的循环，同时不好控制何时结束，所以这里不建议使用有缓冲 channel。  


## 模式4：有状态的遍历  

有状态其实是参考 Java 这种面向对象模式设计，创建一个单独指向源数据区的指针，并且记录遍历的进度。  

```go
type IteratorObj interface {
    HasNext() bool
    Next() interface{}
}
```

然后，定义一个 `Iterable` 接口，对于对象就需要实现此接口，它会生成一个新的对象。  
使用上，每次需要遍历时，都需要创建一个新的 `IteratorObj`。  

```go
type Iterable interface {
    GenIterator() IteratorObj
}
```

代码示例如下：  

```go
// 底层采用数组存储数据
type objArrayIterator struct {
    index int
    data  []interface{}
}

func (b *arrBag) GenIterator() iterable.IteratorObj {
    return &objArrayIterator{
        index: 0,
        data:  b.data,
    }
}

func (it *objArrayIterator) HasNext() bool {
    return it.index < len(it.data)
}

func (it *objArrayIterator) Next() interface{} {
    var result interface{}
    if it.index < len(it.data) {
        result = it.data[it.index]
        it.index++
    }
    return result
}

// 底层采用链表存储数据
type objLinkIterator struct {
    iNode *node
}

func (b *linkBag) GenIterator() iterable.IteratorObj {
    return &objLinkIterator{
        iNode: b.first,
    }
}

func (it *objLinkIterator) HasNext() bool {
    return it.iNode != nil
}

func (it *objLinkIterator) Next() interface{} {
    result := it.iNode
    if it.iNode != nil {
        it.iNode = it.iNode.next
    }
    return result
}
```

使用示例如下：  

```go
ab := newArrBag(10)
it := ab.GenIterator()
for it.HasNext() {
    fmt.Printf("%v\t", it.Next())
}
```

这种方式实现相对前三种有点啰嗦，不过从 Java 这类编程语言过来的同学可能会感觉比较熟悉。


## 总结

从最初的目的：实现统一的 iterator API 来看，以上四种实现方式从 API 设计上都无可挑剔。  

在参考的几篇文章里，有些实现方法都有一个通用问题，就是该如何保证中断遍历时能很好的回收资源，譬如使用 `chan` 模式的时候，内部实现改如何在最后 `close` 掉。  
但在上面实现的几种模式里，都保证最后能资源回收，不会出现此类问题。  

最后通过 Golang 的 benchmark 来对比四种实现方式哪种更优。  

```shell
#  go test -benchmem -bench ^Benchmark -run ^$
BenchmarkIterateBag_Callback_Array-8             429       2500652 ns/op           0 B/op           0 allocs/op
BenchmarkIterateBag_Callback_Link-8              375       3479282 ns/op           0 B/op           0 allocs/op
BenchmarkIterateBag_Channel_Array-8                6     220628550 ns/op         176 B/op           1 allocs/op
BenchmarkIterateBag_Channel_Link-8                 5     207023700 ns/op         200 B/op           2 allocs/op
BenchmarkIterateBag_Closure_Array-8              243       4789662 ns/op          56 B/op           3 allocs/op
BenchmarkIterateBag_Closure_Link-8               351       3945431 ns/op          24 B/op           2 allocs/op
BenchmarkIterateBag_Objective_Array-8            278       4335142 ns/op          32 B/op           1 allocs/op
BenchmarkIterateBag_Objective_Link-8             240       4364060 ns/op           8 B/op           1 allocs/op
```

从性能上看，使用 callback 方式更优，且没有额外的内存分配，并且 interface 设计更为简单、优雅。  
对于使用 chan 性能更差，觉得主要原因是虽然只有两个 goroutine 存在，但是对于 chan 的分配以及 goroutine 调度，对性能还是有点影响。  


## 参考  

1. [Iterators in Go](https://ewencp.org/blog/golang-iterators/)   
2. [What is most idiomatic way to create an iterator in Go?](https://stackoverflow.com/questions/14000534/what-is-most-idiomatic-way-to-create-an-iterator-in-go)，答案中有一个[代码示例](https://play.golang.org/p/oEg5wrC4J6)看起来很详细  
3. [Golang背包和迭代器的实现](https://blog.csdn.net/weixin_43102379/article/details/100078957)  