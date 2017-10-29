package me.server.domain.users

import me.server.domain.users_api.UserCreated

object UsersEventHandler {
  def handleUserCreated(onSuccess:()=>Unit, e: UserCreated): Unit = {
    onSuccess()
  }
}

