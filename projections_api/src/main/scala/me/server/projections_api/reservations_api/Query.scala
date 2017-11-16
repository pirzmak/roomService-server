package me.server.projections_api.reservations_api

import java.time.LocalDate

import me.server.utils.ddd.AggregateId

case class GetAllReservations()

case class GetReservationById(id: AggregateId)

case class GetReservationsFromTo(from: LocalDate, to: LocalDate)
