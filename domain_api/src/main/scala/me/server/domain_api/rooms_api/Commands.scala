package me.server.domain_api.rooms_api

import java.time.LocalDate

import me.server.domain_api.users_api.{PersonInfo, PersonalData}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.cqrs.{Command, CommandResult, FirstCommand, MyCommand}


case class CreateRoom(info: RoomInfo, bedsNr: Int, costPerPerson: Long) extends FirstCommand[Room, CommandResult]

case class ChangeRoomInfo(aggregateId:AggregateId, expectedVersion: AggregateVersion, name: String, description: String) extends Command[Room, CommandResult]

case class ChangeBedsNr(aggregateId:AggregateId, expectedVersion: AggregateVersion, bedsNr: Int) extends Command[Room, CommandResult]

case class ChangeRoomCost(aggregateId:AggregateId, expectedVersion: AggregateVersion, costPerPerson: Long) extends Command[Room, CommandResult]

case class DeleteRoom(aggregateId:AggregateId, expectedVersion: AggregateVersion) extends Command[Room, CommandResult]

case class ActiveRoom(aggregateId:AggregateId, expectedVersion: AggregateVersion) extends Command[Room, CommandResult]