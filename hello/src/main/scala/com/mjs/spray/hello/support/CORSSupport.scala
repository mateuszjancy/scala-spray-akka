package com.mjs.spray.hello.support

import akka.actor.ActorLogging
import spray.http.HttpHeaders._
import spray.http.HttpMethods._
import spray.http.{AllOrigins, HttpMethod, HttpMethods, HttpResponse}
import spray.routing._

//https://coderwall.com/p/0izzta/cors-directive-for-spray
//https://gist.github.com/joseraya/176821d856b43b1cfe19
trait CORSSupport {
  this: HttpService with ActorLogging =>

  private val allowOriginHeader = `Access-Control-Allow-Origin`(AllOrigins)
  private val optionsCorsHeaders = List(
    `Access-Control-Allow-Headers`(
      "Access-Control-Allow-Origin, Access-Control-Allow-Methods, Access-Control-Allow-Credentials,Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent"
    ),
    `Access-Control-Max-Age`(1728000))

  def cors[T]: Directive0 = mapRequestContext { ctx => ctx.withRouteResponseHandling({
    //It is an option requeset for a resource that responds to some other method
    case Rejected(x) => {
      log.debug("Rejected: {}", ctx.request.method)
      if (ctx.request.method.equals(HttpMethods.OPTIONS) && !x.filter(_.isInstanceOf[MethodRejection]).isEmpty) {
        val allowedMethods: List[HttpMethod] = x.filter(_.isInstanceOf[MethodRejection]).map(rejection => {
          rejection.asInstanceOf[MethodRejection].supported
        })
        ctx.complete(HttpResponse().withHeaders(
          `Access-Control-Allow-Methods`(OPTIONS, allowedMethods: _*) :: allowOriginHeader :: optionsCorsHeaders
        ))
      }
    }
  }).withHttpResponseHeadersMapped { headers =>
    allowOriginHeader :: headers

  }
  }
}
