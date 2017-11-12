package me.server.domain.users

import me.server.domain_api.users_api._
import me.server.utils.cqrs.{CommandResponse, CommandSuccess}

object UsersCommandHandler {
  def handleCreateUser(c: CreateUser): CommandResponse = {
    CommandSuccess(UserCreated(UserId(0),c.email,c.password,c.firstName,c.lastName))
  }

  def handleUpdateUser(c: UpdateUser): CommandResponse = {
    CommandSuccess(UserUpdated(c.email,c.password,c.firstName,c.lastName))
  }

  def handleDeleteUser(c: DeleteUser): CommandResponse = {
    CommandSuccess(UserDeleted())
  }
}
