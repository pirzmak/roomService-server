package me.server.utils.cqrs

import me.server.utils.ddd.{AggregateId, OrganizationId}

trait Event

case class MyEvent(event: Event, aggregateId: AggregateId = AggregateId(0), organizationId: OrganizationId = OrganizationId(0))