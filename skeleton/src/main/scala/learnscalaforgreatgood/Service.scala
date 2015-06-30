package learnscalaforgreatgood

import akka.actor.{Actor, ActorLogging}
import org.json4s.DefaultFormats
import spray.httpx.Json4sSupport
import spray.routing.RequestContext

object Service {
  def apply() = new Service
}

class Service extends Actor with ActorLogging with Json4sSupport {

  val json4sFormats = DefaultFormats

  def receive = {
    case (ctx: RequestContext, domain: Domain) => {
      log.info(s"Request for: $domain")

      ctx.complete(domain)
    }
    case _ => {
      log.info("Invalid msg")
    }
  }
}
