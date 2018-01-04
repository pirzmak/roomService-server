package me.server.domain_api.rooms_api

import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}
import me.server.utils.cqrs.{Command, CommandResult, FirstCommand, MyCommand}


case class CreateRoom(organizationId: OrganizationId,
                      info: RoomInfo, bedsNr: Int, costPerPerson: Long) extends FirstCommand[Room, CommandResult]

case class ChangeRoomInfo(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId,
                          name: String, description: String) extends Command[Room, CommandResult]

case class ChangeBedsNr(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId,
                        bedsNr: Int) extends Command[Room, CommandResult]

case class ChangeRoomCost(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId,
                          costPerPerson: Long) extends Command[Room, CommandResult]

case class DeleteRoom(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId) extends Command[Room, CommandResult]

case class ActiveRoom(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId) extends Command[Room, CommandResult]