package me.server.domain.users

import me.server.domain.users_api.CreateUser

object UsersCommandHandlers {
  def handleCreateUser(onSuccess:()=>Unit, c: CreateUser): Unit = {
    onSuccess()
  }
}
