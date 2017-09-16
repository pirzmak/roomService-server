package me.server.utils.cqrs

import me.server.utils.ddd.{AggregateId, AggregateVersion}

trait Event

case class MyEvent(event: Event, aggregateId: AggregateId = AggregateId(0), aggregateVersion: AggregateVersion = AggregateVersion(0))