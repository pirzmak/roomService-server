package me.server.domain.users

import me.server.domain.users_api.CreateUser

object UsersCommandHandler {
  def handleCreateUser(onSuccess:()=>Unit, c: CreateUser): Unit = {
    onSuccess()
  }
}
