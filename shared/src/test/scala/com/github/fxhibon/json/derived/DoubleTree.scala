package com.github.fxhibon.json.derived

sealed trait DoubleTree[T]

case class Branch[T](left: DoubleTree[T], right: DoubleTree[T]) extends DoubleTree[T]

case class Leaf[T](left: T, right: T) extends DoubleTree[T]
