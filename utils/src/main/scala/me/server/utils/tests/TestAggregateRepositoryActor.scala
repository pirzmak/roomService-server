package me.server.utils.tests

import me.server.utils.DocumentStore
import me.server.utils.cqrs._
import me.server.utils.ddd.{AggregateContext, AggregateId, AggregateRepositoryActor, OrganizationId}

class TestAggregateRepositoryActor[AGGREGATE_ROOT](aggregateId: AggregateId,
                                                   organizationId: OrganizationId,
                                                   events: List[Event],
                                                   aggregateContext: AggregateContext[AGGREGATE_ROOT],
                                                   documentStore: DocumentStore[AGGREGATE_ROOT]) extends AggregateRepositoryActor("TestMock", aggregateId, organizationId, aggregateContext, documentStore) {


  override def preStart(): Unit = {
    super.preStart()
    events.foreach(event => {
      val aggregate = aggregateContext.receiveEvents(event, state.aggregateState)
      documentStore.insertDocument(aggregateId, state.aggregateVersion, organizationId, aggregate)
      updateState(MyEvent(event, aggregateId), aggregate)
    })
  }

  override val receiveCommand: Receive = {
    case c: FirstCommand[_, _] =>
      handleCommand(c, CommandWithSender(sender, c))
    case c: Command[_, _] =>
      if (c.expectedVersion.version != state.aggregateVersion.next.version)
        sender ! CommandResult(StatusResponse.failure, c.aggregateId, c.expectedVersion,
          "Expected version: " + state.aggregateVersion.next.version + " but get: " + c.expectedVersion)
      else
        handleCommand(c, CommandWithSender(sender, c))
    case _ => throw CommandException.unknownCommand
  }
}
