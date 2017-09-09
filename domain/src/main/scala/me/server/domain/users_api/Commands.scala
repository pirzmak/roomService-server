package me.server.domain.users_api

sealed abstract class UserCommand

case class CreateUser(email: String, password: String, firstName:String, lastName: String) extends UserCommand

case class UpdateUser(id: UserId, email: Option[String], password: Option[String], firstName: Option[String], lastName: Option[String]) extends UserCommand

case class DeleteUser(id: UserId) extends UserCommand
