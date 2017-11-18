package me.server.projections_api.users_api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import me.server.domain_api.users_api.User

import scala.concurrent.Future

class UserProjectionQueryApi(projection: ActorRef)(implicit akkaTimeout: Timeout) {

  def findUserById(query: FindUserById): Future[User] = {
    (projection ? query).mapTo[User]
  }

  def getAllUsers(query: GetAllUsers): Future[List[User]] = {
    (projection ? query).mapTo[List[User]]
  }

}
