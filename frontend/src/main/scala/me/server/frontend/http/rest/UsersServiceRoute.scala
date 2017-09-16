package me.server.frontend.http.rest

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.SecurityDirectives
import akka.util.Timeout
import me.server.domain.users_api.CreateUser
import me.server.frontend.json.JsonSupport
import akka.pattern.ask
import me.server.utils.cqrs.CommandResult

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext


class UsersServiceRoute(val userRepository: ActorRef)(implicit executionContext: ExecutionContext) extends SecurityDirectives with JsonSupport {

  implicit val timeout = Timeout(25 seconds)

  val routes = pathPrefix("users") {
    path("new") {
      post {
        entity(as[CreateUser]) { createUser =>
            complete((userRepository ? createUser).mapTo[CommandResult])
        }
      }
    }
  }
}
