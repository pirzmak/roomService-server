package me.server.domain.reservations

import me.server.domain_api.reservations_api._
import me.server.projections_api.rooms_occupancy_api.RoomsOccupancyQueryApi
import me.server.utils.cqrs._
import me.server.utils.ddd.AggregateContext

class ReservationsAggregateContext(roomsOccupancyQueryApi: RoomsOccupancyQueryApi) extends AggregateContext[Reservation] {

  var reservationsCommandHandler = new ReservationsCommandHandler(roomsOccupancyQueryApi)

  def receiveCommand(command: MyCommand, reservation: Reservation): CommandResponse = command match {
    case c: CreateReservation => reservationsCommandHandler.handleCreateReservation(c)
    case c: ChangeDate => reservationsCommandHandler.handleChangeDate(c, reservation)
    case c: ChangeClientInfo => reservationsCommandHandler.handleChangeClientInfo(c)
    case c: ChangeRoom => reservationsCommandHandler.handleChangeRoom(c, reservation)
    case c: ChangeDiscount => reservationsCommandHandler.handleChangeDiscount(c)
    case c: ChangeLoan => reservationsCommandHandler.handleChangeLoan(c)
    case c: DeleteReservation => reservationsCommandHandler.handleDeleteReservation(c)
    case c: ActiveReservation => reservationsCommandHandler.handleActiveReservation(c)

    case _ => throw CommandException.unknownCommand
  }
  def receiveEvents(event: Event, reservation: Reservation): Reservation = event match {
    case e: ReservationCreated => ReservationsEventHandler.handleReservationCreated(e)
    case e: DateChanged => ReservationsEventHandler.handleDateChanged(e, reservation)
    case e: ClientInfoChanged => ReservationsEventHandler.handleClientInfoChanged(e, reservation)
    case e: RoomChanged => ReservationsEventHandler.handleRoomChanged(e, reservation)
    case e: DiscountChanged => ReservationsEventHandler.handleDiscountChanged(e, reservation)
    case e: LoanChanged => ReservationsEventHandler.handleLoanChanged(e, reservation)
    case e: ReservationDeleted => ReservationsEventHandler.handleReservationDeleted(e, reservation)
    case e: ReservationActived => ReservationsEventHandler.handleReservationActived(e, reservation)

    case _ => throw CommandException.unknownEvent
  }

  def initialAggregate(): Reservation = Reservation.empty

}
