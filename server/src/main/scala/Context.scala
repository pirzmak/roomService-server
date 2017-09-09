import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import me.server.domain.users.UsersAggregateContext
import me.server.domain.users_api.{CreateUser, UserCreated, UserId}
import me.server.frontend.{FrontendServer, MainRestService}
import me.server.frontend.http.rest.UsersServiceRoute
import user.UserProjection

class Context {

  def init() = {
    implicit val system = ActorSystem("System")

    implicit val ec = global

    val userContextActor = system.actorOf(Props(new UsersAggregateContext()), "userAggregateContext")
    val userProjection = system.actorOf(Props(new UserProjection("userProjection", "userAggregateContext")), "userProjection")

    val usersServiceRoute = new UsersServiceRoute(userContextActor)
    val mainRestServiceActor = new MainRestService(usersServiceRoute)

    val frontend = new FrontendServer(mainRestServiceActor)(system)

    frontend
  }

  val (frontend:FrontendServer) = init()
}
