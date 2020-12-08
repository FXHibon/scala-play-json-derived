package com.fxhibon.json.derived

import play.api.libs.json.{JsError, JsValue, Json, Reads}
import ReadsDerivation._

class ReadsDerivationTest extends munit.FunSuite {

  test("derive case class Reads") {

    val correctPayload = """{"left": 123, "right": 321}"""
    val incorrectPayload = """{"left": "qwe"}"""

    val customDerivedReads: Reads[Leaf[Int]] = gen
    val playDerivedReads: Reads[Leaf[Int]] = Json.reads

    val successResultWithCustomReads =
      customDerivedReads.reads(Json.parse(correctPayload))
    val successResultWithPlayReads =
      playDerivedReads.reads(Json.parse(correctPayload))

    val failedResultWithCustomReads =
      customDerivedReads.reads(Json.parse(incorrectPayload))
    val failedResultWithPlayReads =
      playDerivedReads.reads(Json.parse(incorrectPayload))

    assertEquals(successResultWithCustomReads.get, Leaf(123, 321))
    assertEquals(successResultWithCustomReads, successResultWithPlayReads)

    assertEquals(
      failedResultWithCustomReads.asEither.left.get.toSet,
      failedResultWithPlayReads.asEither.left.get.toSet
    )
  }

  test("derive sealed trait Reads") {

    val trees: List[JsValue] = List(
      Json.parse("""{"type": "Leaf", "left": 123, "right": 321}"""),
      Json.parse(
        """{"type": "Branch", "left": {"type": "Leaf", "left": 123, "right": 321}, "right": {"type": "Leaf", "left": 123, "right": 321}}"""
      )
    )

    val customDerivedReads: Reads[DoubleTree[Int]] = gen

    val successResultWithCustomReads = trees.map(_.as(customDerivedReads))

    val failureWithCustomReadsNoTypeFound =
      customDerivedReads.reads(Json.parse("""{"left": 123, "right": 321}"""))
    val failureWithCustomReadsUnknownType = customDerivedReads.reads(
      Json.parse("""{"type": "FooType", "left": 123, "right": 321}""")
    )

    assertEquals(
      successResultWithCustomReads,
      List(
        Leaf(left = 123, right = 321),
        Branch(
          left = Leaf(left = 123, right = 321),
          right = Leaf(left = 123, right = 321)
        )
      )
    )

    assertNoDiff(
      JsError
        .toJson(failureWithCustomReadsNoTypeFound.asInstanceOf[JsError])
        .toString(),
      """{"obj.type":[{"msg":["error.path.missing"],"args":[]}]}"""
    )
    assertNoDiff(
      JsError
        .toJson(failureWithCustomReadsUnknownType.asInstanceOf[JsError])
        .toString(),
      """{"obj.type":[{"msg":["error.invalid.typename"],"args":[]}]}"""
    )
  }
}
