package me.server.domain.reservations

import java.time.LocalDate

import me.server.domain_api.reservations_api._
import me.server.projections_api.rooms_occupancy_api.{CheckRoomOccupancy, RoomsOccupancyQueryApi}
import me.server.utils.cqrs.{CommandFailure, CommandResponse, CommandSuccess}
import me.server.utils.ddd.{AggregateId, OrganizationId}

class ReservationsCommandHandler(roomOccupancyProjection: RoomsOccupancyQueryApi) {
  def handleCreateReservation(c: CreateReservation): CommandResponse = {
    val errors = reservationValidate(c.organizationId, c.from, c.to, c.roomId)
    if(errors.isEmpty)
      CommandSuccess(ReservationCreated(c.from, c.to, c.clientInfo, c.roomId, c.discount))
    else
      CommandFailure(errors.mkString(","))
  }

  def handleChangeDate(c: ChangeDate, reservation: Reservation): CommandResponse = {
    var errors = dateValidate(c.from.getOrElse(reservation.from), c.to.getOrElse(reservation.to))
    if(c.from.isEmpty && c.to.isEmpty)
        errors = "Date cant't be empty" :: errors
    if(errors.isEmpty)
      CommandSuccess(DateChanged(c.from, c.to))
    else
      CommandFailure(errors.mkString(","))
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

  private def reservationValidate(organizationId: OrganizationId, from: LocalDate, to: LocalDate, roomId: AggregateId): List[String] = {
    var errors: List[String] = List.empty
    val occupancy = roomOccupancyProjection.checkRoomOccupancyAsync(CheckRoomOccupancy(roomId, organizationId, from, to))
    if(!occupancy)
      "Room is occupied in this date" :: errors
    errors ::: dateValidate(from, to)
  }

  private def dateValidate(from: LocalDate, to: LocalDate): List[String] = {
    var errors: List[String] = List.empty
    if(!from.isBefore(to))
      errors = "Error in reservation date" :: errors
    errors
  }
}
