package me.server.domain_api.rooms_api

import me.server.domain_api.reservations_api.Money

case class Room(info: RoomInfo, bedsNr: Int, costPerPerson: Money, deleted: Boolean)

case class RoomInfo(name: String, description: String)

object Room {
  val empty = Room(RoomInfo("", ""), 0, Money.empty, false)
}
