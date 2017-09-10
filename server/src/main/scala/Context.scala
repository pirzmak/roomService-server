import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import me.server.domain.users.UsersAggregateContext
import me.server.frontend.{FrontendServer, MainRestService}
import me.server.frontend.http.rest.UsersServiceRoute
import users.{UserDocumentStore, UserProjection}
import users_api.GetUserById

class Context {

  def init() = {
    implicit val system = ActorSystem("System")

    implicit val ec = global

    val userContextActor = system.actorOf(Props(new UsersAggregateContext("userAggregateContext")), "userAggregateContext")
    val userStore = new UserDocumentStore()
    val userProjection = system.actorOf(Props(new UserProjection("userProjection", "userAggregateContext", userStore)), "userProjection")

    userProjection ! GetUserById

    val usersServiceRoute = new UsersServiceRoute(userContextActor)
    val mainRestServiceActor = new MainRestService(usersServiceRoute)

    val frontend = new FrontendServer(mainRestServiceActor)(system)

    frontend
  }

  val (frontend:FrontendServer) = init()
}
