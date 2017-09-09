package me.server.domain.users

import akka.persistence.PersistentActor
import me.server.domain.users_api._
import me.server.utils.me.server.utils.cqrs.MyEvent
import me.server.utils.{CommandResult, StatusResponse}

import scala.concurrent.ExecutionContext

case class UsersAggregateState(events: List[MyEvent] = Nil) {
  def updated(evt: MyEvent): UsersAggregateState = copy(evt :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}

class UsersAggregateContext()(implicit val ec: ExecutionContext) extends PersistentActor {
  def persistenceId = "userAggregateContext"

  var state = UsersAggregateState()

  def numEvents = state.size

  def updateState(event: MyEvent): Unit =
    state = state.updated(event)

  val receiveCommand: Receive = {
    case c: CreateUser =>
      persist(MyEvent(UserCreated(UserId(0),c.email,c.password,c.firstName,c.lastName))) { event: MyEvent =>
        updateState(event)
        sender() ! CommandResult(StatusResponse.success,event.aggregateId,event.aggregateVersion,"")
      }
  }

  val receiveRecover: Receive = {
    case e: MyEvent => updateState(e)
  }

}
