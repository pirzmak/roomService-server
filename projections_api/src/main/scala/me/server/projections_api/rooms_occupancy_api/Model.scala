package me.server.projections_api.rooms_occupancy_api

import java.time.LocalDate

import me.server.utils.ddd.AggregateId

case class RoomsOccupancy(occupancy: List[ReservationInfo])

case class ReservationInfo(reservationID: AggregateId, from: LocalDate, to: LocalDate)