package me.server.utils.ddd

import akka.persistence.PersistentActor
import me.server.utils.DocumentStore
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
                                                        aggregateId: AggregateId,
                                                        aggregateContext: AggregateContext[AGGREGATE_ROOT],
                                                        documentStore: DocumentStore[AGGREGATE_ROOT]) extends PersistentActor {
  def persistenceId = id

  protected var state = RepositoryAggregateState[AGGREGATE_ROOT](aggregateState = aggregateContext.initialAggregate())

  private def numEvents = state.size

  def updateState(event: MyEvent, aggregate: AGGREGATE_ROOT): Unit =
    state = state.updated(event, aggregate)

  val receiveCommand: Receive = {
    case commandWithSender: CommandWithSender => commandWithSender.command match {
      case c: FirstCommand[_, _] =>
        handleCommand(c, commandWithSender)
      case c: Command[_, _] =>
        if (c.expectedVersion.version != state.aggregateVersion.next.version)
          commandWithSender.sender ! CommandResult(StatusResponse.failure, c.aggregateId, c.expectedVersion,
            "Expected version: " + state.aggregateVersion.next.version + " but get: " + c.expectedVersion.version)
        else
          handleCommand(c, commandWithSender)
      case _ => throw CommandException.unknownCommand
    }
    case _ => throw CommandException.unknownCommand
  }

  protected def handleCommand(command: MyCommand, commandWithSender: CommandWithSender) = {
    aggregateContext.receiveCommand(command, state.aggregateState) match {
      case CommandSuccess(e) =>
        persist(MyEvent(e,aggregateId)) {
          event => {
            val aggregate = aggregateContext.receiveEvents(e, state.aggregateState)
            documentStore.insertDocument(aggregateId,state.aggregateVersion,aggregate)
            updateState(event,aggregate)
            commandWithSender.sender ! CommandResult(StatusResponse.success, aggregateId, state.aggregateVersion, "")
          }
        }
      case CommandFailure(msg) => commandWithSender.sender ! CommandResult(StatusResponse.failure, aggregateId, state.aggregateVersion, msg)
      case _ => throw CommandException.unknownResponse
    }
  }

  val receiveRecover: Receive = {
    case event: MyEvent => {
      val aggregate = aggregateContext.receiveEvents(event.event, state.aggregateState)
      documentStore.insertDocument(aggregateId,state.aggregateVersion,aggregate)
      updateState(event,aggregate)
    }
    case _ => ()
  }

}
