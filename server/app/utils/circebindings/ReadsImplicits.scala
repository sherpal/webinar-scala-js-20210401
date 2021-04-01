package utils.circebindings

import io.circe._
import io.circe.parser._
import play.api.libs.json._

/** See [[ReadWriteImplicits]] */
trait ReadsImplicits {

  implicit def reads[A](implicit decoder: Decoder[A]): Reads[A] =
    (json: JsValue) =>
      decode[A](json.toString) match {
        case Right(value) => JsSuccess(value)
        case Left(error)  => JsError(error.getMessage)
      }

  implicit def writes[A](implicit encoder: Encoder[A]): Writes[A] = (o: A) =>
    circeToPlayTranslation(encoder(o))

}
