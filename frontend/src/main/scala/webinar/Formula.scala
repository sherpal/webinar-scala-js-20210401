package webinar

/** Generously Stolen from
  * https://github.com/kitlangton/formula-example/blob/master/src/main/scala/formula/Formula.scala
  */
import com.raquo.laminar.api.L._
import magnolia._

import scala.language.experimental.macros

object DeriveForm {
  type Typeclass[A] = Form[A]

  def combine[A](caseClass: CaseClass[Form, A]): Form[A] = new Form[A] {
    override def render(variable: Var[A]): HtmlElement = div(
      caseClass.parameters.map { param =>
        param.typeclass
          .labelled(param.label)
          .render(
            variable.zoom(a => param.dereference(a))(value =>
              caseClass.construct { p =>
                if (p == param) value
                else p.dereference(variable.now())
              }
            )(unsafeWindowOwner)
          )
      }
    )
  }

  implicit def gen[A]: Form[A] = macro Magnolia.gen[A]
}

sealed trait Form[A] { self =>
  implicit val owner = unsafeWindowOwner

  def labelled(str: String): Form[A] = new Form[A] {
    override def render(variable: Var[A]): HtmlElement =
      div(
        cls("input-group"),
        label(str),
        self.render(variable)
      )
  }

  def ~[B](that: Form[B]): Form[(A, B)] = new Form[(A, B)] {
    override def render(variable: Var[(A, B)]): HtmlElement =
      div(
        self.render(variable.zoom(_._1)(_ -> variable.now()._2)),
        that.render(variable.zoom(_._2)(variable.now()._1 -> _))
      )
  }

  def xmap[B](to: A => B)(from: B => A): Form[B] = new Form[B] {
    override def render(variable: Var[B]): HtmlElement =
      self.render(variable.zoom[A](from)(to))
  }

  def render(variable: Var[A]): HtmlElement
}

object Form {
  implicit val string: Form[String] = new Form[String] {
    override def render(variable: Var[String]): HtmlElement =
      input(
        controlled(
          value <-- variable,
          onInput.mapToValue --> variable
        )
      )
  }

  implicit val int: Form[Int] = string.xmap(_.toInt)(_.toString)
}
