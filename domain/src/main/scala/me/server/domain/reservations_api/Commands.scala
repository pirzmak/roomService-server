package me.server.domain.reservations_api

import java.time.LocalDate

import me.server.domain.users_api.{PersonInfo, PersonalData}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.cqrs.{Command, CommandResult, FirstCommand, MyCommand}


case class CreateReservation(from: LocalDate, to: LocalDate, clientInfo: PersonInfo, roomId: AggregateId, discount: Option[Long]) extends FirstCommand[Reservation, CommandResult]

case class ChangeDate(aggregateId:AggregateId, expectedVersion: AggregateVersion, from: Option[LocalDate], to: Option[LocalDate]) extends Command[Reservation, CommandResult]

case class ChangeClientInfo(aggregateId:AggregateId, expectedVersion: AggregateVersion, firstName: Option[String], lastName: Option[String], email: Option[String], phone: Option[String], personalData: Option[PersonalData]) extends Command[Reservation, CommandResult]

case class ChangeRoom(aggregateId:AggregateId, expectedVersion: AggregateVersion, roomId: AggregateId) extends Command[Reservation, CommandResult]

case class ChangeDiscount(aggregateId:AggregateId, expectedVersion: AggregateVersion, discount: Option[Long]) extends Command[Reservation, CommandResult]

case class ChangeLoan(aggregateId:AggregateId, expectedVersion: AggregateVersion, loan: Option[Long]) extends Command[Reservation, CommandResult]

case class DeleteReservation(aggregateId:AggregateId, expectedVersion: AggregateVersion) extends Command[Reservation, CommandResult]

case class ActiveReservation(aggregateId:AggregateId, expectedVersion: AggregateVersion) extends Command[Reservation, CommandResult]