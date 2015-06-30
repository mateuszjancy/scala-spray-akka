package learnscalaforgreatgood

//import akka.actor.{Actor, ActorLogging, ActorRefFactory, Props}

import akka.actor._
import akka.routing.RoundRobinPool
import spray.routing.{ExceptionHandler, HttpService, RejectionHandler, RoutingSettings}
import spray.util.LoggingContext

object Resource {
  def apply() = new Resource()
}

class Resource extends HttpService with Actor with ActorLogging {
  implicit def actorRefFactory: ActorRefFactory = context

  implicit val system = context.system

  val service = context.actorOf(Props(Service()).withRouter(RoundRobinPool(nrOfInstances = 5)))

  def receive = runRoute(api)(ExceptionHandler.default, RejectionHandler.Default, context, RoutingSettings.default, LoggingContext.fromActorRefFactory)

  val api = path("api") {
    get { ctx =>
      service !(ctx, Domain("k", "v"))
    }
  }
}
