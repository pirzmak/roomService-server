package me.server.domain.rooms

import me.server.domain_api.rooms_api._
import me.server.utils.cqrs._
import me.server.utils.ddd.AggregateContext

class RoomsAggregateContext() extends AggregateContext[Room] {

  def receiveCommand(command: MyCommand, room: Room): CommandResponse = command match {
    case c: CreateRoom => RoomsCommandHandler.handleCreateRoom(c)
    case c: ChangeRoomInfo => RoomsCommandHandler.handleChangeRoomInfo(c)
    case c: ChangeBedsNr => RoomsCommandHandler.handleChangeBedsNr(c)
    case c: ChangeRoomCost => RoomsCommandHandler.handleChangeRoomCost(c)
    case c: DeleteRoom => RoomsCommandHandler.handleDeleteRoom(c)
    case c: ActiveRoom => RoomsCommandHandler.handleActiveRoom(c)

    case _ => throw CommandException.unknownCommand
  }
  def receiveEvents(event: Event, room: Room): Room = event match {
    case e: RoomCreated => RoomsEventHandler.handleRoomCreated(e)
    case e: RoomInfoChanged => RoomsEventHandler.handleRoomInfoChanged(e, room)
    case e: BedsNrChanged => RoomsEventHandler.handleBedsNrChanged(e, room)
    case e: RoomCostChanged => RoomsEventHandler.handleRoomCostChanged(e, room)
    case e: RoomDeleted => RoomsEventHandler.handleRoomDeleted(e, room)
    case e: RoomActived => RoomsEventHandler.handleRoomActived(e, room)

    case _ => throw CommandException.unknownEvent
  }

  def initialAggregate(): Room = Room.empty

}
