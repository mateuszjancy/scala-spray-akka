package learnscalaforgreatgood

import akka.actor._
import akka.routing.RoundRobinPool
import org.json4s.DefaultFormats
import spray.httpx.Json4sSupport
import spray.routing._

import scala.util.{Failure, Success}

object Resource {
  def apply() = new Resource()
}

class Resource() extends HttpServiceActor with ActorLogging with Json4sSupport {

  import DomainConverter.formats
  import DomainSchema._

  implicit val system = context.system

  val json4sFormats = DefaultFormats

  val service = context.actorOf(Props(Service()).withRouter(RoundRobinPool(nrOfInstances = 5)))

  def receive = runRoute(api)

  //http://stackoverflow.com/a/22099919
  lazy val api = pathPrefix("api") {
    pathEndOrSingleSlash {
      get {
        complete(DomainSchema.all)
      } ~
        put {
          entity(as[Domain]) { domain =>
            //service ! (ctx, domain)
            complete(DomainSchema.put(domain))
          }
        }
    } ~
      pathPrefix("dropcreate") {
        pathEndOrSingleSlash {
          get {
            complete("dropcreate:ok")
          }
        }
      } ~
      pathPrefix(IntNumber) { k =>
        pathEndOrSingleSlash {
          get {
            complete(DomainSchema.get(k))
          }
        }
      }
  } ~
    pathPrefix("static") {
      pathEnd {
        getFromFile("web/index.html")
      }
    }
}
