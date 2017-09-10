package me.server.domain.users_api

import me.server.utils.{AggregateId, AggregateVersion}

sealed abstract class UserCommand

case class CreateUser(email: String, password: String, firstName:String, lastName: String) extends UserCommand

case class UpdateUser(id: AggregateId, expectedVersion: AggregateVersion, email: Option[String], password: Option[String], firstName: Option[String], lastName: Option[String]) extends UserCommand

case class DeleteUser(id: AggregateId, expectedVersion: AggregateVersion) extends UserCommand
