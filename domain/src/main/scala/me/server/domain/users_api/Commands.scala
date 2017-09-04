package me.server.domain.users_api

sealed abstract class UserCommand

case class CreateUser(id: UserId, username: String, password: String) extends UserCommand

case class UpdateUser(id: UserId, username: Option[String], password: Option[String]) extends UserCommand

case class DeleteUser(id: UserId) extends UserCommand
