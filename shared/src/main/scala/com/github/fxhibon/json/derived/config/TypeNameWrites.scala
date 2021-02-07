package com.github.fxhibon.json.derived.config

import play.api.libs.json.{JsPath, OWrites}

/**
  * define how the type name of sealed trait should be written
  * it provides a default instance that writes the type in "type" field,
  * with the value being the sort name of the case class,
  * at same level than others payload fields,
  * without any transformation
  */
case class TypeNameWrites(writes: OWrites[String])

object TypeNameWrites {
  val defaultTypeNameWrites: TypeNameWrites = TypeNameWrites((JsPath \ "type").write[String])
}
