package me.server.domain_api.reservations_api

import java.time.LocalDate

import me.server.domain_api.users_api.PersonInfo
import me.server.utils.ddd.AggregateId

case class Reservation(date: LocalDate, from: LocalDate, to: LocalDate, checkedDate: Option[LocalDate], unCheckDate: Option[LocalDate],
                       clientInfo: PersonInfo, roomId: AggregateId, cost: Money,
                       discount: Option[Long], loan: Option[Loan], deleted: Boolean)

object Reservation {
  val empty = Reservation(LocalDate.MIN, LocalDate.MIN, LocalDate.MAX, None, None,
    PersonInfo.empty, AggregateId(-1), Money.empty, None, None, false)
}

case class Money(amount: Long, currency: String)

object Money {
  val empty = Money(0, "")
}

case class Loan(amount: Money, date: LocalDate)




