package me.server.domain.users_api

import me.server.utils.ddd.AggregateVersion

case class UserId(id: Long) {
  def asLong: Long = id
}

case class User(email: String, password: String, firstName:String, lastName: String, active: Boolean)

object User {
  val empty = User("","","","",true)
}
