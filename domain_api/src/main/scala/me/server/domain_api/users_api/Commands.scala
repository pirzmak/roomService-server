package me.server.domain_api.users_api

import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.cqrs.{Command, CommandResult, FirstCommand, MyCommand}


case class CreateUser(email: String, password: String, firstName:String, lastName: String) extends FirstCommand[User, CommandResult]

case class UpdateUser(aggregateId: AggregateId, expectedVersion: AggregateVersion, email: Option[String], password: Option[String], firstName: Option[String], lastName: Option[String]) extends Command[User, CommandResult]

case class DeleteUser(aggregateId: AggregateId, expectedVersion: AggregateVersion) extends Command[User, CommandResult]