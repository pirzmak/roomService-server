package me.server.domain.users

import me.server.domain.users_api.{User, UserCreated, UserUpdated}

object UsersEventHandler {
  def handleUserCreated(e: UserCreated): User = {
    User(e.email,e.password,e.firstName,e.lastName,true)
  }
  def handleUserUpdated(user: User, e: UserUpdated): User = {
    user.copy(email = e.email.getOrElse(user.email), password = e.password.getOrElse(user.password),
      firstName = e.firstName.getOrElse(user.firstName), lastName = e.lastName.getOrElse(user.lastName))
  }
  def handleUserDeleted(user: User, e: UserCreated): User = {
    user.copy(active = false)
  }
}

