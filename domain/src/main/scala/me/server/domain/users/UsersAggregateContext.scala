package me.server.domain.users

import akka.actor.AbstractActor.Receive
import me.server.domain.users_api._
import me.server.utils.cqrs.{CommandException, Event, MyCommand, MyEvent}
import me.server.utils.ddd.AggregateContext

class UsersAggregateContext() extends AggregateContext[User] {

  def receiveCommand(command: MyCommand): Event = command match {
    case c: CreateUser =>
      UserCreated(UserId(0),c.email,c.password,c.firstName,c.lastName)
    case c: UpdateUser =>
      UserUpdated(c.email,c.password,c.firstName,c.lastName)
    case c: DeleteUser =>
      UserDeleted()
    case _ => throw CommandException.unknownCommand
}

  def receiveEvents(event: Event): User = event match {
    case _ => User.empty
  }

  def initialAggregate(): User = User.empty

}
