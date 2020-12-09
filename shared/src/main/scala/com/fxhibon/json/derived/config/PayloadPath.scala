package com.fxhibon.json.derived.config

import play.api.libs.json.JsPath

case class PayloadPath(path: JsPath)

object PayloadPath {
  val defaultPayloadPath: PayloadPath = PayloadPath(JsPath())
}
