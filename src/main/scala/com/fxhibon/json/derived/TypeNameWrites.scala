package com.fxhibon.json.derived

import play.api.libs.json.{JsPath, OWrites}

/**
  * define how the type name of sealed trait should be written
  * it provides a default instance that writes the type in "type" field,
  * with the value being the sort name of the case class,
  * at same level than others payload fields,
  * without any transformation
  */
trait TypeNameWrites {
  val writes: OWrites[String]
}

object TypeNameWrites {
  val defaultTypeNameWrites: TypeNameWrites = new TypeNameWrites {
    override val writes: OWrites[String] = (JsPath \ "type").write[String]
  }
}
