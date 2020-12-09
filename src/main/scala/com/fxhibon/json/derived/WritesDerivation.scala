package com.fxhibon.json.derived

import magnolia._
import play.api.libs.json._

import scala.language.experimental.macros

object WritesDerivation {

  type Typeclass[T] = Writes[T]

  def combine[T](ctx: CaseClass[Writes, T]): Writes[T] =
    (t: T) => {
      ctx.parameters
        .map { param =>
          (JsPath \ param.label)
            .write(param.typeclass)
            .writes(param.dereference(t))
        }
        .foldLeft(Json.obj())(_ ++ _)
    }

  def dispatch[T](
      ctx: SealedTrait[Writes, T]
  )(implicit typeNameWrites: TypeNameWrites = TypeNameWrites.defaultTypeNameWrites): Writes[T] =
    (t: T) => {
      ctx.dispatch(t) { subtype =>
        typeNameWrites.writes.writes(subtype.typeName.short) ++ subtype.typeclass
          .writes(subtype.cast(t))
          .as[JsObject]
      }
    }

  implicit def gen[T]: Writes[T] = macro Magnolia.gen[T]
}
