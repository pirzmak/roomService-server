import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import me.server.domain.users.UsersAggregateContext
import me.server.frontend.{FrontendServer, MainRestService}
import me.server.frontend.http.rest.UsersServiceRoute

class Context {

  def init() = {
    val system = ActorSystem("System")

    implicit val ec = global

    val userContextActor = system.actorOf(Props(new UsersAggregateContext()), "userAggregateContext")

    val usersServiceRoute = new UsersServiceRoute(userContextActor)

    val mainRestServiceActor = new MainRestService(usersServiceRoute)

    val frontend = new FrontendServer(mainRestServiceActor)(system)

    frontend
  }

  val (frontend:FrontendServer) = init()
}
