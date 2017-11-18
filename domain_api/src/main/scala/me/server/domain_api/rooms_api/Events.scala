package me.server.domain_api.rooms_api

import me.server.utils.cqrs.Event

sealed trait RoomEvent extends Event

case class RoomCreated(info: RoomInfo, bedsNr: Int, costPerPerson: Long, deleted: Boolean) extends RoomEvent

case class RoomInfoChanged(name: String, description: String) extends RoomEvent

case class BedsNrChanged(bedsNr: Int) extends RoomEvent

case class RoomCostChanged(costPerPerson: Long) extends RoomEvent

case class RoomDeleted() extends RoomEvent

case class RoomActived() extends RoomEvent
