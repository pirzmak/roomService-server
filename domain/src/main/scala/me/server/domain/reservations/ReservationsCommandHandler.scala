package me.server.domain.reservations

import java.time.LocalDate

import me.server.domain.reservations_api._
import me.server.utils.cqrs.{CommandFailure, CommandResponse, CommandSuccess}
import me.server.utils.ddd.AggregateId

object ReservationsCommandHandler {
  def handleCreateReservation(c: CreateReservation): CommandResponse = {
    val errors = reservationValidate(c.from, c.to, c.roomId)
    if(errors.isEmpty)
      CommandSuccess(ReservationCreated(c.from, c.to, c.clientInfo, c.roomId, c.discount))
    else
      CommandFailure(errors.toString())
  }

  def handleChangeDate(c: ChangeDate, reservation: Reservation): CommandResponse = {
    var errors = dateValidate(c.from.getOrElse(reservation.from), c.to.getOrElse(reservation.to))
    if(c.from.isEmpty && c.to.isEmpty)
        errors = "Date cant't be empty" :: errors
    if(errors.isEmpty)
      CommandSuccess(DateChanged(c.from, c.to))
    else
      CommandFailure(errors.toString())
  }

  def handleChangeClientInfo(c: ChangeClientInfo): CommandResponse = {
    CommandSuccess(ClientInfoChanged(c.firstName,c.lastName,c.email,c.phone,c.personalData))
  }

  def handleChangeRoom(c: ChangeRoom, reservation: Reservation): CommandResponse = {
    //Todo roomValidation
    CommandSuccess(RoomChanged(reservation.roomId,c.roomId))
  }

  def handleChangeDiscount(c: ChangeDiscount): CommandResponse = {
    CommandSuccess(DiscountChanged(c.discount))
  }

  def handleChangeLoan(c: ChangeLoan): CommandResponse = {
    CommandSuccess(DiscountChanged(c.loan))
  }

  def handleDeleteReservation(c: DeleteReservation): CommandResponse = {
    CommandSuccess(ReservationDeleted())
  }

  def handleActiveReservation(c: ActiveReservation): CommandResponse = {
    CommandSuccess(ReservationActived())
  }

  private def reservationValidate(from: LocalDate, to: LocalDate, roomId: AggregateId): List[String] = {
    var errors: List[String] = List.empty
    errors ::: dateValidate(from, to)
    //TODO check if room exists and is free
    errors
  }

  private def dateValidate(from: LocalDate, to: LocalDate): List[String] = {
    var errors: List[String] = List.empty
    if(!from.isBefore(to))
      errors = "Error in reservation date" :: errors
    errors
  }
}
