package com.fxhibon.json.derived

import cats.implicits._

import language.experimental.macros
import magnolia._
import play.api.libs.json.{JsError, JsPath, JsResult, JsSuccess, JsValue, Reads}

object ReadsDerivation {

  type Typeclass[T] = Reads[T]

  def combine[T](ctx: CaseClass[Reads, T]): Reads[T] = (value: JsValue) => {
    ctx.constructEither { param =>
      (JsPath \ param.label).read(param.typeclass).reads(value) match {
        case JsSuccess(value, _) => Right(value)
        case JsError(errors) => Left(errors)
      }
    } match {
      case Left(value) => JsError(value.flatten)
      case Right(value) => JsSuccess(value)
    }
  }

  def dispatch[T](ctx: SealedTrait[Reads, T]): Reads[T] = (value: JsValue) => {
    val typeNamePath = JsPath \ "type"
    typeNamePath
      .read[String]
      .flatMap { typeName =>
        ctx.subtypes.find(_.typeName.short == typeName) match {
          case Some(subtype) => Reads[T](_ => subtype.typeclass.reads(value))
          case None => Reads[T](_ => JsError(typeNamePath, "error.invalid.typename"))
        }
      }.reads(value)
  }

  implicit def gen[T]: Reads[T] = macro Magnolia.gen[T]

}
