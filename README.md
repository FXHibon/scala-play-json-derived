## play-json derivation

Provide derivation of [Reads](https://github.com/playframework/play-json/blob/master/play-json/shared/src/main/scala/play/api/libs/json/Reads.scala#L36) (and soon [Writes](https://github.com/playframework/play-json/blob/master/play-json/shared/src/main/scala/play/api/libs/json/Writes.scala#L23)) of [play-json](https://github.com/playframework/play-json) types, using [magnolia](https://github.com/propensive/magnolia)

This is a work in progress

### How to

- for a complete examples, see the [tests](./src/test/scala/com/fxhibon/json/derived/ReadsDerivationTest.scala)

In a few words, given:

````scala
sealed trait Tree[T]

case class Branch[T](left: Tree[T], right: Tree[T]) extends Tree[T]

case class Leaf[T](value: T) extends Tree[T]
````

one can derive for zero cost an instance of **Reads[Tree[Int]]**, assuming there is a **Reads[Int]** in the scope

```scala
import com.fxhibon.json.derived.ReadsDerivation._

val treeReads: Reads[Tree] = gen[Tree]
```

I made sure that errors generated by failing Reads or Writes are the same that play-json macro's would have generated them.

### Check list
- [x] derive Reads
- [ ] derive Writes
- [ ] derive Format
- [ ] support customization of type name Reads / Writes
- [ ] support customization of payload Reads / Writes
