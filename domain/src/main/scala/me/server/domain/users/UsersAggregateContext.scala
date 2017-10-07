package me.server.domain.users

import me.server.domain.users_api._
import me.server.utils.cqrs._
import me.server.utils.ddd.AggregateContext

class UsersAggregateContext() extends AggregateContext[User] {

  def receiveCommand(command: MyCommand): CommandResponse = command match {
    case c: CreateUser =>
      CommandSuccess(UserCreated(UserId(0),c.email,c.password,c.firstName,c.lastName))
    case c: UpdateUser =>
      CommandSuccess(UserUpdated(c.email,c.password,c.firstName,c.lastName))
    case c: DeleteUser =>
      CommandSuccess(UserDeleted())
    case _ => throw CommandException.unknownCommand
}

  def receiveEvents(event: Event, user: User): User = event match {
    case e: UserCreated =>
      User(e.email,e.password,e.firstName,e.lastName,true)
    case e: UserUpdated =>
      user.copy(email = e.email.getOrElse(user.email), password = e.password.getOrElse(user.password),
        firstName = e.firstName.getOrElse(user.firstName), lastName = e.lastName.getOrElse(user.lastName))
    case _ => User.empty
  }

  def initialAggregate(): User = User.empty

}
