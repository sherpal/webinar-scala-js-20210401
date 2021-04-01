package webinar

import com.raquo.laminar.api.L._
import typings.canvasConfetti.{mod => confetti}
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLCanvasElement
import io.circe.generic.auto._
import models.Person

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success

object EntryPoint {
  def main(args: Array[String]): Unit = {
    println("hi")

    val myPerson = Var(Person("Alice", 23, "an address"))

    val myForm = DeriveForm.gen[Person]

    def postThePerson(person: Person): Unit = {
      MakeCall.post[Person, String]("api/a-person", Map(), person).onComplete {
        case Failure(exception) => throw exception
        case Success(value)     => println(value)
      }
    }

    def throwConfetti(): Unit = {
      confetti
        .create(
          dom.document
            .getElementById("my-canvas")
            .asInstanceOf[HTMLCanvasElement]
        )
        .apply()
    }

    val app = div(
      form(
        myForm.render(myPerson),
        onSubmit.preventDefault.mapTo(myPerson.now) --> (p => postThePerson(p)),
        input(tpe := "submit", value := "Click")
      ),
      child <-- myPerson.signal.map(_.toString),
      button("Confetti!", onClick.mapTo(()) --> { _ => throwConfetti() })
    )

    render(dom.document.getElementById("root"), app)

  }
}
