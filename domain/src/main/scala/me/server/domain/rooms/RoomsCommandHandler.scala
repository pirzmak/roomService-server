package me.server.domain.rooms

import me.server.domain_api.rooms_api._
import me.server.utils.cqrs.{CommandFailure, CommandResponse, CommandSuccess}

object RoomsCommandHandler {
  def handleCreateRoom(c: CreateRoom): CommandResponse = {
    CommandSuccess(RoomCreated(c.info, c.bedsNr, c.costPerPerson, false))
  }

  def handleChangeRoomInfo(c: ChangeRoomInfo): CommandResponse = {
    CommandSuccess(RoomInfoChanged(c.name, c.description))
  }

  def handleChangeBedsNr(c: ChangeBedsNr): CommandResponse = {
    if(c.bedsNr >= 0)
      CommandSuccess(BedsNrChanged(c.bedsNr))
    else
      CommandFailure("Beds number can't be smaller than 0")
  }

  def handleChangeRoomCost(c: ChangeRoomCost): CommandResponse = {
    if(c.costPerPerson >= 0)
      CommandSuccess(RoomCostChanged(c.costPerPerson))
    else
      CommandFailure("Room cost can't be smaller than 0")
  }

  def handleDeleteRoom(c: DeleteRoom): CommandResponse = {
    //Todo validate if empty
    CommandSuccess(RoomDeleted())
  }

  def handleActiveRoom(c: ActiveRoom): CommandResponse = {
    CommandSuccess(RoomActived())
  }
}
