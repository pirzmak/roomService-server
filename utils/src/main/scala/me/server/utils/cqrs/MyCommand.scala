package me.server.utils.cqrs

import akka.actor.ActorRef
import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}

trait MyCommand

abstract class FirstCommand[AGGREGATE, COMMAND_RESULT] extends MyCommand {
  val organizationId: OrganizationId
}

abstract class Command[AGGREGATE, COMMAND_RESULT] extends MyCommand {
  val aggregateId: AggregateId
  val expectedVersion: AggregateVersion
  val organizationId: OrganizationId
}

case class CommandWithSender(sender: ActorRef, command: MyCommand)





