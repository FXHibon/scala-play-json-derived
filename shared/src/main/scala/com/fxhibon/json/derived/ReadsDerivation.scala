package com.fxhibon.json.derived

import com.fxhibon.json.derived.config.{PayloadPath, TypeNameReads}
import magnolia._
import play.api.libs.json._

import scala.language.experimental.macros

object ReadsDerivation {

  type Typeclass[T] = Reads[T]

  def combine[T](ctx: CaseClass[Reads, T]): Reads[T] =
    (value: JsValue) => {
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

  def dispatch[T](
      ctx: SealedTrait[Reads, T]
  )(implicit
      typeNameReads: TypeNameReads = TypeNameReads.defaultTypeNameReads,
      payloadPath: PayloadPath = PayloadPath.defaultPayloadPath
  ): Reads[T] =
    (value: JsValue) => {
      typeNameReads.reads
        .flatMap { typeName =>
          ctx.subtypes.find(_.typeName.short == typeName) match {
            case Some(subtype) =>
              Reads[T](_ => payloadPath.path.read(subtype.typeclass).reads(value))
            case None =>
              Reads[T](_ => JsError("error.invalid.typename"))
          }
        }
        .reads(value)
    }

  implicit def deriveReads[T]: Reads[T] = macro Magnolia.gen[T]

}
