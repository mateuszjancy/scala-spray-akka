package com.mjs.spray.hello

import akka.actor.ActorSystem
import spray.http.HttpHeaders.RawHeader
import spray.routing.SimpleRoutingApp

object OAuth2 extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("my-system")

  startServer(interface = "localhost", port = 9090) {

    path("oauth2") {
      get {
        respondWithHeader(RawHeader("Access-Control-Allow-Origin", "*")) {
          complete {
            "OAuth2 hello"
          }
        }
      }
    }
  }
}