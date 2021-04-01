package utils

import io.circe
import play.api.libs.json._

package object circebindings {

  /** Transform a circe json value into a play json value. */
  private[circebindings] def circeToPlayTranslation(
      json: circe.Json
  ): play.api.libs.json.JsValue =
    json.asString
      .map(JsString)
      .orElse(json.asBoolean.map(JsBoolean))
      .orElse(json.asNumber.flatMap(_.toBigDecimal).map(JsNumber))
      .orElse(json.asArray.map(_.map(circeToPlayTranslation)).map(JsArray))
      .orElse(
        json.asObject
          .map(_.toMap.map { case (key, value) =>
            key -> circeToPlayTranslation(value)
          }.toList)
          .map(JsObject)
      )
      .getOrElse(JsNull)

}
