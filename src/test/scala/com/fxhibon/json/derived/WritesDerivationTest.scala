package com.fxhibon.json.derived

import WritesDerivation._
import play.api.libs.json.{Json, Writes}

class WritesDerivationTest extends munit.FunSuite {

  test("derive case class Writes") {
    val customizedDerivedWrites: Writes[Leaf[Int]] = gen
    val playDerivedWrites: Writes[Leaf[Int]] = Json.writes

    val leaf = Leaf(left = 123, right = 321)

    val resultWithCustomized = customizedDerivedWrites.writes(leaf)
    val resultWithPlay = playDerivedWrites.writes(leaf)

    assertNoDiff(resultWithCustomized.toString(), resultWithPlay.toString())
    assertNoDiff(
      resultWithCustomized.toString(),
      """{"left":123,"right":321}"""
    )
  }

  test("derive sealed trait Writes") {
    val customizedDerivedWrites: Writes[DoubleTree[Int]] = gen

    val result = customizedDerivedWrites.writes(
      Branch(
        left = Leaf(left = 123, right = 321),
        right = Leaf(left = 123, right = 321)
      )
    )

    assertNoDiff(
      result.toString(),
      """{"type":"Branch","left":{"type":"Leaf","left":123,"right":321},"right":{"type":"Leaf","left":123,"right":321}}"""
    )
  }

}
