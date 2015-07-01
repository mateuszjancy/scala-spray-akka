package learnscalaforgreatgood

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Domain(name: Int, value: String)

object DomainConverter extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val formats = jsonFormat2(Domain)
}

import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

object DomainSchema {
  import scala.util.{Try, Success, Failure}

  val db = Database.forConfig("skeleton-db")

  class Domains(tag: Tag) extends Table[Domain](tag, "DOMAIN") {
    def key = column[Int]("D_KEY", O.PrimaryKey)
    def value = column[String]("D_VALUE")

    def * = (key, value) <> (Domain.tupled, Domain.unapply)
  }

  val domains = TableQuery[Domains]

  def dropcreate() = {
    val statement = DBIO.seq(/*(domains.schema.drop), */domains.schema.create)
    db.run(statement)
  }

  def all = {
    db.run(domains.result)
  }

  def get(key: Int) = {
    val q = domains.filter(_.key === key)
    db.run(q.result)
  }

  def put(domain: Domain) = {
    val q = DBIO.seq(domains += domain)
    db.run(q)
  }
}
