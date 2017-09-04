package me.server.domain.users

import akka.persistence.PersistentActor
import me.server.domain.users_api.{CreateUser, User, UserCreated, UserEvent}
import me.server.utils.{CommandResult, StatusResponse, VersionId}

import scala.concurrent.ExecutionContext

case class UsersAggregateState(events: List[UserEvent] = Nil) {
  def updated(evt: UserEvent): UsersAggregateState = copy(evt :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}

class UsersAggregateContext()(implicit val ec: ExecutionContext) extends PersistentActor {
  def persistenceId = "usersAggregate"

  var state = UsersAggregateState()

  def numEvents = state.size

  def updateState(event: UserEvent): Unit =
    state = state.updated(event)

  var users: List[User] = List.empty

  val receiveCommand: Receive = {
    case c: CreateUser =>
      persist(UserCreated(c.id,c.username,c.password)) { event =>
        updateState(event)
        sender() ! CommandResult(StatusResponse.success,event.id,VersionId(1),"")
      }
  }

  val receiveRecover: Receive = {
    case e: UserCreated =>
      users =  User(e.id, VersionId(1), e.username, e.password, true) :: users
      updateState(e)
  }

}
