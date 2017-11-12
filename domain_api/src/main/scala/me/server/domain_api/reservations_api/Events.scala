package me.server.domain_api.reservations_api

import java.time.LocalDate

import me.server.domain_api.users_api.{PersonInfo, PersonalData}
import me.server.utils.cqrs.Event
import me.server.utils.ddd.AggregateId

sealed trait ReservationEvent extends Event

case class ReservationCreated(from: LocalDate, to: LocalDate, clientInfo: PersonInfo, roomId: AggregateId, discount: Option[Long]) extends ReservationEvent

case class DateChanged(from: Option[LocalDate], to: Option[LocalDate]) extends ReservationEvent

case class ClientInfoChanged(firstName: Option[String], lastName: Option[String], email: Option[String], phone: Option[String], personalData: Option[PersonalData]) extends ReservationEvent

case class RoomChanged(oldRoomId: AggregateId, newRoomId: AggregateId) extends ReservationEvent

case class DiscountChanged(discount: Option[Long]) extends ReservationEvent

case class LoanChanged(loan: Option[Long]) extends ReservationEvent

case class ReservationDeleted() extends ReservationEvent

case class ReservationActived() extends ReservationEvent
