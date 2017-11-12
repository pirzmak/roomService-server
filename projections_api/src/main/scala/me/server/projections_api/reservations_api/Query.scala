package me.server.projections_api.reservations_api

import java.time.LocalDate

case class GetAllReservations()

case class GetReservationById()

case class GetReservationsFromTo(from: LocalDate, to: LocalDate)
