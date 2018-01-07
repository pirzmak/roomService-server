package me.server.domain.reservations

import java.time.LocalDate

import me.server.domain_api.reservations_api._
import me.server.domain_api.users_api.PersonInfo


object ReservationsEventHandler {
  def handleReservationCreated(e: ReservationCreated): Reservation = {
    Reservation(LocalDate.now(), e.from, e.to, None, None, e.clientInfo, e.roomId, Money.empty, None, None, false)
  }

  def handleDateChanged(e: DateChanged, reservation: Reservation): Reservation = {
    reservation.copy(from = e.from.getOrElse(reservation.from), to = e.to.getOrElse(reservation.to))
  }

  def handleClientInfoChanged(e: ClientInfoChanged, r: Reservation): Reservation = {
    r.copy(clientInfo = PersonInfo(e.firstName.getOrElse(r.clientInfo.firstName),
      e.lastName.getOrElse(r.clientInfo.lastName),
      e.email.getOrElse(r.clientInfo.email),
      e.phone.getOrElse(r.clientInfo.phone),
      if(e.personalData.isDefined) e.personalData else r.clientInfo.personalData))
  }

  def handleRoomChanged(e: RoomChanged, reservation: Reservation): Reservation = {
    reservation.copy(roomId = e.newRoomId)
  }

  def handleDiscountChanged(e: DiscountChanged, reservation: Reservation): Reservation = {
    reservation.copy(discount = e.discount)
  }

  def handleLoanChanged(e: LoanChanged, reservation: Reservation): Reservation = {
    e.loan match {
      case Some(l) => reservation.copy(loan = Some(Loan(Money(l,"PLN"), LocalDate.now())))
      case None => reservation.copy(loan = None)
    }
  }

  def handleReservationDeleted(e: ReservationDeleted, reservation: Reservation): Reservation = {
    reservation.copy(deleted = true)
  }

  def handleReservationActived(e: ReservationActived, reservation: Reservation): Reservation = {
    reservation.copy(deleted = false)
  }
}

