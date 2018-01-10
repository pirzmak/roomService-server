package me.server.projections_api.rooms_api

import me.server.utils.ddd.{AggregateId, OrganizationId}

case class GetAllRooms(organizationId: OrganizationId)

case class GetRoomById(roomId: AggregateId, organizationId: OrganizationId)

