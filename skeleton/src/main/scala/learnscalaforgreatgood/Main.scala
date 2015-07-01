package learnscalaforgreatgood

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Main extends App {
  implicit val system = ActorSystem("skeleton-app")
  val resource = system.actorOf(Props(Resource()), "skeleton-resource")

  IO(Http) ! Http.Bind(resource, "localhost", 8080)
  DomainSchema.dropcreate()
}
