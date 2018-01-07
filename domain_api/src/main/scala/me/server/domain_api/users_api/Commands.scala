package me.server.domain_api.users_api

import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}
import me.server.utils.cqrs.{Command, CommandResult, FirstCommand, MyCommand}


case class CreateUser(organizationId: OrganizationId,
                      email: String, password: String, firstName:String, lastName: String) extends FirstCommand[User, CommandResult]

case class UpdateUser(aggregateId: AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId,
                      email: Option[String], password: Option[String], firstName: Option[String], lastName: Option[String]) extends Command[User, CommandResult]

case class SetUserData(aggregateId: AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId,
                       sex: Option[String], address: Option[String], age: Option[Int], id: Option[String], phone: Option[String]) extends Command[User, CommandResult]


case class ChangeUserType(aggregateId: AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId,
                          userType: UserType) extends Command[User, CommandResult]

case class ActiveUser(aggregateId: AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId) extends Command[User, CommandResult]

case class ConfirmUserEmail(aggregateId: AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId) extends Command[User, CommandResult]

case class DeleteUser(aggregateId: AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId) extends Command[User, CommandResult]