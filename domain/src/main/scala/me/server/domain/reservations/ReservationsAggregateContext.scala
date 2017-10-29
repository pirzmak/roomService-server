package me.server.domain.reservations

import me.server.domain.reservations_api._
import me.server.domain.users_api._
import me.server.utils.cqrs._
import me.server.utils.ddd.AggregateContext

class ReservationsAggregateContext() extends AggregateContext[Reservation] {

  def receiveCommand(command: MyCommand, reservation: Reservation): CommandResponse = command match {
    case c: CreateReservation => ReservationsCommandHandler.handleCreateReservation(c)
    case c: ChangeDate => ReservationsCommandHandler.handleChangeDate(c, reservation)
    case c: ChangeClientInfo => ReservationsCommandHandler.handleChangeClientInfo(c)
    case c: ChangeRoom => ReservationsCommandHandler.handleChangeRoom(c, reservation)
    case c: ChangeDiscount => ReservationsCommandHandler.handleChangeDiscount(c)
    case c: ChangeLoan => ReservationsCommandHandler.handleChangeLoan(c)
    case c: DeleteReservation => ReservationsCommandHandler.handleDeleteReservation(c)
    case c: ActiveReservation => ReservationsCommandHandler.handleActiveReservation(c)

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
