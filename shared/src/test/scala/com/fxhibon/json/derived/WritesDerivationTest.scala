package com.fxhibon.json.derived

import WritesDerivation._
import play.api.libs.json.{JsPath, Json, OWrites, Writes}

class WritesDerivationTest extends munit.FunSuite {

  test("derive case class Writes") {
    val customizedDerivedWrites: Writes[Leaf[Int]] = gen[Leaf[Int]]
    val playDerivedWrites: Writes[Leaf[Int]] = Json.writes[Leaf[Int]]

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
    val customizedDerivedWrites: Writes[DoubleTree[Int]] = gen[DoubleTree[Int]]

    val result = customizedDerivedWrites.writes(
      Branch(
        left = Leaf(left = 123, right = 321),
        right = Leaf(left = 123, right = 321)
      )
    )

    assertEquals(
      result,
      Json.parse("""{"type":"Branch","left":{"type":"Leaf","left":123,"right":321},"right":{"type":"Leaf","left":123,"right":321}}""")
    )
  }

  test("derive sealed trait Writes with custom type name") {
    implicit val typeNameWrites: TypeNameWrites = new TypeNameWrites {
      override val writes: OWrites[String] = (JsPath \ "custom_type_name").write[String]
    }
    val customizedDerivedWrites: Writes[DoubleTree[Int]] = gen[DoubleTree[Int]]

    val json = customizedDerivedWrites.writes(Leaf(left = 123, right = 321))
    assertEquals(json, Json.parse("""{"custom_type_name":"Leaf","left":123,"right":321}"""))
  }

}
