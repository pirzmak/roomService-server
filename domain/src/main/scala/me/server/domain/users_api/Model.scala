package me.server.domain.users_api

import me.server.utils.{AggregateId, AggregateVersion}

case class UserId(id: Long) {
  def asLong: Long = id
}

case class User(id: UserId, version: AggregateVersion, email: String, password: String, firstName:String, lastName: String, active: Boolean)

object User {
  val empty = User(UserId(0),AggregateVersion(0),"","","","",true)
}
