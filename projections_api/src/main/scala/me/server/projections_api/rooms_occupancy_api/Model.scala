package me.server.projections_api.rooms_occupancy_api

import java.time.LocalDate

import me.server.utils.ddd.AggregateId

case class RoomsOccupancy(roomId: AggregateId, occupancy: List[(LocalDate,LocalDate)])
