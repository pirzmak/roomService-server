package me.server.utils.ddd

import me.server.utils.cqrs.{Event, MyCommand, MyEvent}

trait AggregateContext[ROOT] {
  def receiveCommand(command: MyCommand): Event
  def receiveEvents(event: Event): ROOT
  def initialAggregate(): ROOT
}
