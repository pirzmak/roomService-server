package me.server.projections_api.rooms_occupancy_api

import java.time.LocalDate

import me.server.utils.ddd.AggregateId

case class GetRoomOccupancyById(roomId: AggregateId)

case class CheckRoomOccupancy(roomId: AggregateId, from: LocalDate, to: LocalDate)

case class FindFreeRooms(from: LocalDate, to: LocalDate)

