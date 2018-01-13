package me.server.utils.ddd

case class Aggregate[T](aggregateId: AggregateId, version: AggregateVersion, organizationId: OrganizationId, aggregate: T)
