package com.mjs.spray.hello.factorial.service

import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import akka.routing.RoundRobinPool
import com.mjs.spray.hello.factorial.service.SumService.Result
import org.json4s.DefaultFormats
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing.RequestContext

object CoordinatorService {
  def apply(ctx: RequestContext) = new CoordinatorService(ctx)
}

class CoordinatorService(ctx: RequestContext) extends Actor with ActorLogging with Json4sSupport {

  import context._

  val json4sFormats = DefaultFormats
  val factorial = context.actorOf(Props(FactorialService()).withRouter(RoundRobinPool(nrOfInstances = 5)))
  val sum = context.actorOf(Props(SumService()))

  def receive = {
    case (a, b) => {
      factorial ! a
      factorial ! b
    }
    case a: BigInt => {
      sum ! a
    }
    case Result(a) => {
      ctx.complete(OK, a)
      stop(self)
    }
    case ReceiveTimeout => {
      ctx.complete(GatewayTimeout, "Request timeout")
      stop(self)
    }
  }

  override val supervisorStrategy = OneForOneStrategy() {
    case e => {
      ctx.complete(InternalServerError, "Error")
      Stop
    }
  }
}
