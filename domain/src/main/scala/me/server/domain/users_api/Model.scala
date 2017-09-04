package me.server.domain.users_api

import me.server.utils.{AggregateId, VersionId}

case class UserId(id: Long) extends AggregateId {
  def asLong: Long ={
    id
  }
}

case class User(id: UserId, version: VersionId, username: String, password: String, active: Boolean)

object User {
  val empty = User(UserId(0),VersionId(1),"","",true)
}
