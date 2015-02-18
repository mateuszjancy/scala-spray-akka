package com.mjs.spray.hello

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.IO
import com.mjs.spray.hello.factorial.service.{TestActor, CoordinatorService}
import com.mjs.spray.hello.support.{CORSSupport}
import spray.can.Http
import spray.http.HttpHeaders.{RawHeader, `Access-Control-Allow-Origin`}
import spray.http.{AllOrigins, StatusCodes}
import spray.routing.{HttpService, RequestContext}
import akka.actor._

//https://github.com/NET-A-PORTER/spray-actor-per-request/tree/master/src/main/scala/com/netaporter/routing
object FactorialResourceServer extends App {
  implicit val system = ActorSystem("rest-service-example")
  val restService = system.actorOf(Props(FactorialResourceActor()), "rest-endpoint")
  IO(Http) ! Http.Bind(restService, "localhost", 8080)
}

object FactorialResourceActor {
  def apply() = new FactorialResourceActor()
}

class FactorialResourceActor extends Actor with ActorLogging with FactorialResource {
  implicit def actorRefFactory = context
  def receive = runRoute(rest)
}

trait FactorialResource extends HttpService with CORSSupport {
  this: ActorLogging with Actor =>

  val rest = cors {
    path("factorial" / IntNumber / IntNumber) { (a, b) =>
      get { ctx =>
        withCoordinatorService(ctx) ! (a, b)
      }
    } ~
      path("login") {
        get {
          redirect("http://localhost:9090/oauth2", StatusCodes.PermanentRedirect)
        } ~
          post {
            redirect("http://localhost:9090/oauth2", StatusCodes.PermanentRedirect)
          }
      }
  }

  def withCoordinatorService(ctx: RequestContext) = {
    context.actorOf(Props(CoordinatorService(ctx)))
  }
}
