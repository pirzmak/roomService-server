package me.server.utils.ddd

import me.server.utils.cqrs.{CommandResponse, Event, MyCommand}

trait AggregateContext[ROOT] {
  def receiveCommand(command: MyCommand, aggregate: ROOT): CommandResponse
  def receiveEvents(event: Event, aggregate: ROOT): ROOT
  def initialAggregate(): ROOT
}
