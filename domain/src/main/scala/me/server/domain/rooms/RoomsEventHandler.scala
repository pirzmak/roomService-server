package me.server.domain.rooms

import me.server.domain_api.rooms_api._

object RoomsEventHandler {
  def handleRoomCreated(e: RoomCreated): Room = {
    Room(e.info, e.bedsNr, e.costPerPerson, e.deleted)
  }

  def handleRoomInfoChanged(e: RoomInfoChanged, room: Room): Room = {
    room.copy(info = RoomInfo(e.name, e.description))
  }

  def handleBedsNrChanged(e: BedsNrChanged, room: Room): Room = {
    room.copy(bedsNr = e.bedsNr)
  }

  def handleRoomCostChanged(e: RoomCostChanged, room: Room): Room = {
    room.copy(costPerPerson = e.costPerPerson)
  }

  def handleRoomDeleted(e: RoomDeleted, room: Room): Room = {
    room.copy(deleted = true)
  }

  def handleRoomActived(e: RoomActived, room: Room): Room = {
    room.copy(deleted = true)
  }
}

