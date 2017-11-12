package me.server.utils.cqrs

import me.server.utils.ddd.AggregateId

trait Event

case class MyEvent(event: Event, aggregateId: AggregateId = AggregateId(0))