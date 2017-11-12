package me.server.domain_api.rooms_api

case class Room(info: RoomInfo, bedsNr: Int, costPerPerson: Long, deleted: Boolean)

case class RoomInfo(name: String, description: String)

object Room {
  val empty = Room(RoomInfo("", ""), 0, 0, false)
}
