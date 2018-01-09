package example

import example.fixtures.OrderFixture
import org.scalatest.{MustMatchers, WordSpec}

class MonoidSpec extends WordSpec with MustMatchers with OrderFixture {

  import cats.Monoid

  "Monoid" must {

    "work on Int" in {
      // TODO: add the import for 'int' monoids
      import cats.instances.int._

      Monoid[Int].combine(1, 2) must be(3)
      Monoid.apply[Int].combineAll(List(1, 2, 3)) must be(6)
      Monoid.apply[Int].combineN(1, 10) must be(10)

      Monoid[Int].combine(
        15,
        Monoid.apply[Int].empty
      ) must be(15)
    }

    "work on Strings" in {
      // TODO: add the import for 'string' monoids
      import cats.instances.string._
      Monoid[String].combine("hello", "world") must be("helloworld")
      Monoid.apply[String].combineAll(List("one", "two", "three")) must be("onetwothree")
      Monoid.apply[String].combineN("hello", 3) must be("hellohellohello")

      // TODO: add the import for 'option' syntax
      import cats.syntax.option._
      Monoid
        .apply[String]
        .combineAllOption(
          List("one", "two", "three")
        ) must be("onetwothree".some)
      Monoid
        .apply[String]
        .combineAllOption(List.empty[String]) must be(none[String])
    }

    "work on Boolean" in {
      import BooleanMonoidInstances._
      // TODO: implement ANDMonoid, ORMonoid
      Monoidal(ANDMonoid).combine(true, true) must be(true)
      Monoidal(ORMonoid).combine(false, true) must be(true)
    }

    "work on Order" in {
      // TODO: add the implicit monoid for Orders in the order companion object
      Monoid[Order].combine(
        orderCosting70,
        orderCosting30
      ) must be(Order(100, 30))
    }

    "allow the special |+| syntax" in {
      // TODO: find the right import
      import cats.syntax.semigroup._

      import cats.instances.string._
      ("hello" |+| "world") must be("helloworld")

      import cats.syntax.option._
      import cats.instances.option._
      ("hello".some |+| "world".some) must be("helloworld".some)

      // Remember that Monoids are Semigroups
      (orderCosting70 |+| orderCosting30) must be(Order(100, 30))
    }

    "work on Map[T]" in {
      val headers1 = Map(
        "cache-control" -> "max-age=315360000, public, immutable",
        "content-encoding" -> "gzip",
        "content-type" -> "font/woff2"
      )
      val headers2 = Map(
        "date" -> "Sun, 19 Nov 2017 04:02:22 GMT",
        "last-modified" -> "Thu, 16 Nov 2017 22:27:50 GMT",
        "server" -> "meinheld/0.6.1"
      )

      // TODO: find the right imports for string and maps
      import cats.instances.string._
      import cats.instances.map._
      import cats.syntax.semigroup._
      (headers1 |+| headers2) must be(headers1 ++ headers2)
    }

    "work on Option[Int]" in {
      // TODO: find the correct imports to make this work
      import cats.syntax.option._
      import cats.instances.int._
      import cats.instances.option._

      Monoid[Option[Int]].combine(1.some, 2.some) must be(3.some)
    }

    "sum Monoid[T] in a generic way" which {

      // TODO: implement the generic Calculator.add that work on Monoid[T]

      "uses Monoid[Int]" in {
        // TODO: find the correct imports to make this work
        import cats.instances.int._
        Calculator.add(List(1, 2, 3, 4)) must be(10)
      }

      "uses Monoid[Option[Int]" in {
        // TODO: find the correct imports to make this work
        import cats.instances.int._
        import cats.instances.option._
        import cats.syntax.option._
        Calculator.add(List(1.some, 2.some, 3.some, 4.some)) must be(10.some)
      }

      "uses Monoid[Order]" in {
        val orders = List(
          Order(2.3, 1),
          Order(1.7, 1),
          Order(2.5, 1),
          Order(3.5, 1)
        )
        Calculator.add(orders) must be(Order(10, 4))
      }

    }

  }

}
