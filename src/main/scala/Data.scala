import cats.data.NonEmptyList
import org.mongodb.scala.MongoClient

import scala.concurrent.{ExecutionContext, Future}

object Episode extends Enumeration {
  val NEWHOPE, EMPIRE, JEDI = Value
}

trait Character {
  def id: String

  def name: Option[String]

  def friends: List[String]

  def appearsIn: List[Episode.Value]
}

case class Human(
                  id: String,
                  name: Option[String],
                  friends: List[String],
                  appearsIn: List[Episode.Value],
                  homePlanet: Option[String]) extends Character

case class Droid(
                  id: String,
                  name: Option[String],
                  friends: List[String],
                  appearsIn: List[Episode.Value],
                  primaryFunction: Option[String]) extends Character

//case class CharacterRepo(mongo: MongoClient) {
case class CharacterRepo() {

  import CharacterRepo._

  def getHero(episode: Option[Episode.Value])(implicit ex: ExecutionContext): Future[Character] =
    episode.map(_ => getHuman("1000").map(_.getOrElse(droids.last))).getOrElse(Future.successful(droids.last))

  def getHuman(id: String): Future[Option[Human]] = Future.successful(humans.find(c ⇒ c.id == id))

  def getDroid(id: String): Future[Option[Droid]] = Future.successful(droids.find(c ⇒ c.id == id))
}

object CharacterRepo {
  val humans = List(
    Human(
      id = "1000",
      name = Some("Luke Skywalker"),
      friends = List("1002", "1003", "2000", "2001"),
      appearsIn = List(Episode.NEWHOPE, Episode.EMPIRE, Episode.JEDI),
      homePlanet = Some("Tatooine")),
    Human(
      id = "1001",
      name = Some("Darth Vader"),
      friends = List("1004"),
      appearsIn = List(Episode.NEWHOPE, Episode.EMPIRE, Episode.JEDI),
      homePlanet = Some("Tatooine")),
    Human(
      id = "1002",
      name = Some("Han Solo"),
      friends = List("1000", "1003", "2001"),
      appearsIn = List(Episode.NEWHOPE, Episode.EMPIRE, Episode.JEDI),
      homePlanet = None),
    Human(
      id = "1003",
      name = Some("Leia Organa"),
      friends = List("1000", "1002", "2000", "2001"),
      appearsIn = List(Episode.NEWHOPE, Episode.EMPIRE, Episode.JEDI),
      homePlanet = Some("Alderaan")),
    Human(
      id = "1004",
      name = Some("Wilhuff Tarkin"),
      friends = List("1001"),
      appearsIn = List(Episode.NEWHOPE, Episode.EMPIRE, Episode.JEDI),
      homePlanet = None)
  )

  val droids = List(
    Droid(
      id = "2000",
      name = Some("C-3PO"),
      friends = List("1000", "1002", "1003", "2001"),
      appearsIn = List(Episode.NEWHOPE, Episode.EMPIRE, Episode.JEDI),
      primaryFunction = Some("Protocol")),
    Droid(
      id = "2001",
      name = Some("R2-D2"),
      friends = List("1000", "1002", "1003"),
      appearsIn = List(Episode.NEWHOPE, Episode.EMPIRE, Episode.JEDI),
      primaryFunction = Some("Astromech"))
  )

  case class Order(orderNumber: String, orderLines: NonEmptyList[OrderLine])

  case class OrderLine(lineNumber: Int, itemId: String)

  case class OrderRepo(mongo: MongoClient) {
    def getByOrderNumber(orderNumber: String): Future[Option[Order]] = Future.successful(
      Some(
        Order(
          orderNumber = orderNumber,
          orderLines = NonEmptyList.one(
            OrderLine(
              lineNumber = 0,
              itemId = "YX_0000000001"
            )
          )
        )
      )
    )
  }

}
