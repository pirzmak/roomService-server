package me.server.projections_api.rooms_occupancy_api

import java.time.LocalDate

import me.server.utils.ddd.{AggregateId, OrganizationId}

case class GetRoomOccupancyById(roomId: AggregateId, organizationId: OrganizationId)

case class CheckRoomOccupancy(roomId: AggregateId, organizationId: OrganizationId, from: LocalDate, to: LocalDate)

case class FindFreeRooms(organizationId: OrganizationId, from: LocalDate, to: LocalDate)

