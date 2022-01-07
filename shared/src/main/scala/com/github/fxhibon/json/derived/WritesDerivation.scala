package com.github.fxhibon.json.derived

import com.github.fxhibon.json.derived.config.{PayloadPath, TypeNameWrites}
import magnolia1._
import play.api.libs.json._

object WritesDerivation {

  type Typeclass[T] = Writes[T]

  def join[T](ctx: CaseClass[Writes, T]): Writes[T] =
    (t: T) => {
      ctx.parameters
        .map { param =>
          (JsPath \ param.label)
            .write(param.typeclass)
            .writes(param.dereference(t))
        }
        .foldLeft(Json.obj())(_ ++ _)
    }

  def split[T](
      ctx: SealedTrait[Writes, T]
  )(implicit
      typeNameWrites: TypeNameWrites = TypeNameWrites.defaultTypeNameWrites,
      payloadPath: PayloadPath = PayloadPath.defaultPayloadPath
  ): Writes[T] =
    (t: T) => {
      ctx.split(t) { subtype =>
        typeNameWrites.writes.writes(subtype.typeName.short) ++ payloadPath.path
          .write(subtype.typeclass)
          .writes(subtype.cast(t))
          .as[JsObject]
      }
    }

  implicit def deriveWrites[T]: Writes[T] = macro Magnolia.gen[T]
}
