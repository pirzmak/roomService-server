package me.server.domain.reservations_api

import java.time.LocalDate

import me.server.domain.users_api.PersonInfo
import me.server.utils.ddd.AggregateId

case class Reservation(from: LocalDate, to: LocalDate, clientInfo: PersonInfo, roomId: AggregateId, cost: Long,
                       discount: Option[Long], loan: Option[Long], deleted: Boolean)

object Reservation {
  val empty = Reservation(LocalDate.MIN, LocalDate.MAX, PersonInfo.empty, AggregateId(-1), 0, None, None, false)
}
