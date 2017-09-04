package me.server.domain.users_api

sealed abstract class MyEvent

sealed abstract class UserEvent extends MyEvent

case class UserCreated(id: UserId, username: String, password: String) extends UserEvent

case class UserUpdated(id: UserId, username: Option[String], password: Option[String]) extends UserEvent

case class UserDeleted(id: UserId) extends UserEvent
