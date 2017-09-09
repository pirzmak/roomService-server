package me.server.domain.users_api

import me.server.utils.me.server.utils.cqrs.Event

sealed trait UserEvent extends Event

case class UserCreated(id: UserId, email: String, password: String, firstName:String, lastName: String) extends UserEvent

case class UserUpdated(id: UserId, email: Option[String], password: Option[String], firstName: Option[String], lastName: Option[String]) extends UserEvent

case class UserDeleted(id: UserId) extends UserEvent
