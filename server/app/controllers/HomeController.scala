package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import utils.circebindings.ReadWriteImplicits._
import io.circe.generic.auto._
import models.Person

@Singleton
class HomeController @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  def hello() = Action {
    Ok("hello")
  }

  def postPerson() = Action(parse.json[Person]) { request =>
    val person = request.body
    println(person)
    Ok(s"Hey ${person.name}")
  }

}
