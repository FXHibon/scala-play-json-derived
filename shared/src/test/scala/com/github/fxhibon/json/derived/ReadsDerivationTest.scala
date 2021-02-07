package com.github.fxhibon.json.derived

import play.api.libs.json.{JsError, JsPath, JsValue, Json, Reads}
import ReadsDerivation._
import com.github.fxhibon.json.derived.config.{PayloadPath, TypeNameReads}

class ReadsDerivationTest extends munit.FunSuite {

  test("derive case class Reads") {

    val correctPayload = """{"left": 123, "right": 321}"""
    val incorrectPayload = """{"left": "qwe"}"""

    val customDerivedReads: Reads[Leaf[Int]] = deriveReads[Leaf[Int]]
    val playDerivedReads: Reads[Leaf[Int]] = Json.reads[Leaf[Int]]

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
      failedResultWithCustomReads.asEither.swap.getOrElse(fail("reads should have failed")).toSet,
      failedResultWithPlayReads.asEither.swap.getOrElse(fail("reads should have failed")).toSet
    )
  }

  test("derive sealed trait Reads") {

    val trees: List[JsValue] = List(
      Json.parse("""{"type": "Leaf", "left": 123, "right": 321}"""),
      Json.parse(
        """{"type": "Branch", "left": {"type": "Leaf", "left": 123, "right": 321}, "right": {"type": "Leaf", "left": 123, "right": 321}}"""
      )
    )

    val customDerivedReads: Reads[DoubleTree[Int]] = deriveReads[DoubleTree[Int]]

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
      """{"obj":[{"msg":["error.invalid.typename"],"args":[]}]}"""
    )
  }

  test("derive sealed trait with custom type name") {
    implicit val typeNameReads: TypeNameReads = TypeNameReads((JsPath \ "custom_typename").read[String])
    val customDerivedReads: Reads[DoubleTree[Int]] = deriveReads[DoubleTree[Int]]

    val result = customDerivedReads.reads(Json.parse("""{"custom_typename": "Leaf", "left": 123, "right": 321}"""))

    assertEquals(result.get, Leaf(left = 123, right = 321))
  }

  test("derive sealed trait with custom payload path") {
    implicit val payloadPath: PayloadPath = PayloadPath(JsPath \ "data")
    val customDerivedReads: Reads[DoubleTree[Int]] = deriveReads[DoubleTree[Int]]

    val result = customDerivedReads.reads(Json.parse("""{"type": "Leaf", "data": {"left": 123, "right": 321}}"""))
    val errorResult = customDerivedReads.reads(Json.parse("""{"type": "Leaf", "left": 123, "right": 321}"""))

    assertEquals(result.get, Leaf(left = 123, right = 321))
    assertNoDiff(
      JsError.toJson(errorResult.asInstanceOf[JsError]).toString,
      """{"obj.data":[{"msg":["error.path.missing"],"args":[]}]}"""
    )
  }
}
