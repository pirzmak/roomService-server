package me.server.utils.ddd

import akka.persistence.PersistentActor
import me.server.utils.Store
import me.server.utils.cqrs._

case class RepositoryAggregateState[AGGREGATE_ROOT](events: List[MyEvent] = Nil,
                                                    aggregateVersion: AggregateVersion = AggregateVersion(0),
                                                    aggregateState: AGGREGATE_ROOT) {
  def updated(evt: MyEvent, aggregate: AGGREGATE_ROOT): RepositoryAggregateState[AGGREGATE_ROOT] = {
    copy(evt :: events, aggregateVersion.next, aggregate)
  }
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}

abstract class AggregateRepositoryActor[AGGREGATE_ROOT](id: String,
                                                        aggregateContext: AggregateContext[AGGREGATE_ROOT]) extends PersistentActor {
  def persistenceId = id

  var state = RepositoryAggregateState[AGGREGATE_ROOT](aggregateState = aggregateContext.initialAggregate())

  def numEvents = state.size

  def updateState(event: MyEvent, aggregate: AGGREGATE_ROOT): Unit =
    state = state.updated(event, aggregate)

  val receiveCommand: Receive = {
    case commandWithSender: CommandWithSender => commandWithSender.command match {
      case c: FirstCommand[_, _] => persist(MyEvent(aggregateContext.receiveCommand(c))) {
        event => {
          aggregateContext.receiveEvents(event.event)
          commandWithSender.sender ! CommandResult(StatusResponse.success, AggregateId(0), AggregateVersion(0), "")
        }
      }
      case c: Command[_, _] =>
        if (c.expectedVersion.version == state.aggregateVersion.next.version)
          commandWithSender.sender ! CommandResult(StatusResponse.failure, c.aggregateId, c.expectedVersion,
            "Expected version: " + state.aggregateVersion.next.version + " but get: " + c.expectedVersion)
        else
          persist(MyEvent(aggregateContext.receiveCommand(c))) { event =>
            updateState(event, aggregateContext.receiveEvents(event.event))
            commandWithSender.sender ! CommandResult(StatusResponse.success, c.aggregateId, c.expectedVersion, "")
          }
      case _ => throw CommandException.unknownCommand
    }
    case _ => throw CommandException.unknownCommand
  }

  val receiveRecover: Receive = {
    case _ => ()
  }
}
