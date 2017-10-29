package me.server.domain.users

import me.server.domain.users_api._
import me.server.utils.cqrs._
import me.server.utils.ddd.AggregateContext

class UsersAggregateContext() extends AggregateContext[User] {

  def receiveCommand(command: MyCommand, user: User): CommandResponse = command match {
    case c: CreateUser => UsersCommandHandler.handleCreateUser(c)
    case c: UpdateUser => UsersCommandHandler.handleUpdateUser(c)
    case c: DeleteUser => UsersCommandHandler.handleDeleteUser(c)

    case _ => throw CommandException.unknownCommand
}

  def receiveEvents(event: Event, user: User): User = event match {
    case e: UserCreated => UsersEventHandler.handleUserCreated(e)
    case e: UserUpdated => UsersEventHandler.handleUserUpdated(user,e)
      user.copy(loginEmail = e.email.getOrElse(user.loginEmail), password = e.password.getOrElse(user.password))
    case e: UserDeleted => UsersEventHandler.handleUserDeleted(user)

    case _ => throw CommandException.unknownEvent
  }

  def initialAggregate(): User = User.empty

}
