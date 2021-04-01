package webinar

import io.circe.{Encoder, Decoder}

import org.scalajs.dom.XMLHttpRequest
import scala.concurrent.Future
import scala.concurrent.Promise
import io.circe.parser.decode
import io.circe.syntax._
import org.scalajs.dom
import scala.util.{Success, Failure}

object MakeCall {

  def get[Response](path: String, parameters: Map[String, String])(implicit
      dec: Decoder[Response]
  ): Future[Response] =
    rawCall("GET", path, parameters, "")

  def post[Body, Response](
      path: String,
      parameters: Map[String, String],
      body: Body
  )(implicit enc: Encoder[Body], dec: Decoder[Response]): Future[Response] =
    rawCall("POST", path, parameters, body)

  private val csrfTokenName: String = "Csrf-Token"
  private def maybeCsrfToken = dom.document.cookie
    .split(";")
    .map(_.trim)
    .find(_.startsWith(s"$csrfTokenName="))
    .map(_.drop(csrfTokenName.length + 1))

  private def rawCall[Body, Response](
      method: String,
      path: String,
      parameters: Map[String, String],
      body: Body
  )(implicit enc: Encoder[Body], dec: Decoder[Response]): Future[Response] = {
    val request = new XMLHttpRequest

    val promise = Promise[Response]()

    request.onreadystatechange = (_: dom.Event) => {
      if (request.readyState == 4 && (request.status / 200 <= 1)) {
        val body = request.response.asInstanceOf[String]

        promise.complete(decode[Response](body).toTry)
      } else if (request.readyState == 4) {
        promise.complete(
          Failure(
            new RuntimeException(
              Option(request.response.asInstanceOf[String])
                .getOrElse("That blew up")
            )
          )
        )
      }
    }

    val queryString =
      parameters.map { case (key, value) => s"$key=$value" }.mkString("&")

    request.open(
      method,
      dom.document.location.origin.toString ++ "/" ++ path.dropWhile(
        _ == '/'
      ) ++ (if (queryString.nonEmpty) "?" else "") ++ queryString,
      async = true
    )

    maybeCsrfToken.filter(_ => method != "GET").foreach { token =>
      request.setRequestHeader(csrfTokenName, token)
    }
    request.setRequestHeader("Content-Type", "application/json")

    if (method != "GET") request.send(body.asJson.noSpaces)
    else request.send()

    promise.future
  }

}
