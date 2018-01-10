package me.server.domain.rooms

import me.server.domain_api.reservations_api.Money
import me.server.domain_api.rooms_api._

object RoomsEventHandler {
  def handleRoomCreated(e: RoomCreated): Room = {
    Room(e.info, e.bedsNr, Money(e.costPerPerson, "PLN"), e.deleted)
  }

  def handleRoomInfoChanged(e: RoomInfoChanged, room: Room): Room = {
    room.copy(info = RoomInfo(e.name, e.description))
  }

  def handleBedsNrChanged(e: BedsNrChanged, room: Room): Room = {
    room.copy(bedsNr = e.bedsNr)
  }

  def handleRoomCostChanged(e: RoomCostChanged, room: Room): Room = {
    room.copy(costPerPerson = Money(e.costPerPerson, "PLN"))
  }

  def handleRoomDeleted(e: RoomDeleted, room: Room): Room = {
    room.copy(deleted = true)
  }

  def handleRoomActived(e: RoomActived, room: Room): Room = {
    room.copy(deleted = true)
  }
}

