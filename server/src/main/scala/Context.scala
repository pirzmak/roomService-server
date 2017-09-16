import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import me.server.domain.users.UsersAggregateContext
import me.server.domain.users_api.{CreateUser, User}
import me.server.frontend.{FrontendServer, MainRestService}
import me.server.frontend.http.rest.UsersServiceRoute
import me.server.utils.ddd.AggregateManager
import users.{UserDocumentStore, UserProjection}
import users_api.GetUserById

class Context {

  def init() = {
    implicit val system = ActorSystem("System")

    implicit val ec = global

    val userContextActor = new UsersAggregateContext()
    val commandHandler = system.actorOf(Props(new AggregateManager[User]("UserMenager",userContextActor)))

    val usersServiceRoute = new UsersServiceRoute(commandHandler)
    val mainRestServiceActor = new MainRestService(usersServiceRoute)

    val frontend = new FrontendServer(mainRestServiceActor)(system)

    frontend
  }

  val (frontend:FrontendServer) = init()
}
