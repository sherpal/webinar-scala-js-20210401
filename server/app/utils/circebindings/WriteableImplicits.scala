package utils.circebindings

import io.circe.Encoder
import io.circe.syntax._
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.mvc.Codec

/** See [[ReadWriteImplicits]] */
trait WriteableImplicits {

  implicit def jsonWritable[A](implicit
      writes: Encoder[A],
      codec: Codec
  ): Writeable[A] = {
    implicit val contentType: ContentTypeOf[A] =
      ContentTypeOf[A](Some(ContentTypes.JSON))

    val transform =
      Writeable.writeableOf_JsValue.transform compose circeToPlayTranslation compose ((_: A).asJson)
    Writeable(transform)
  }
}
