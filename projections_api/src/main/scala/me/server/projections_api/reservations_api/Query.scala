package me.server.projections_api.reservations_api

import java.time.LocalDate

import me.server.utils.ddd.{AggregateId, OrganizationId}

case class GetAllReservations(organizationId: OrganizationId)

case class GetReservationById(id: AggregateId, organizationId: OrganizationId)

case class GetReservationsFromTo(from: LocalDate, to: LocalDate, organizationId: OrganizationId)
