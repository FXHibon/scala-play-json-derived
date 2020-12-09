package com.fxhibon.json.derived

import play.api.libs.json.{JsPath, Reads}

/**
  * define how the type name of sealed trait should be read
  * it provides a default instance that reads the type in "type" field
  * at same level than others payload fields, without any transformation
  */
trait TypeNameReads {
  val reads: Reads[String]
}

object TypeNameReads {
  val defaultTypeNameReads: TypeNameReads = new TypeNameReads {
    override val reads: Reads[String] = (JsPath \ "type").read[String]
  }
}
