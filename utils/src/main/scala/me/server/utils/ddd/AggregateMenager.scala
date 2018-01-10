package me.server.utils.ddd

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.persistence.PersistentActor
import me.server.utils.cqrs._
import akka.util.Timeout
import me.server.utils.DocumentStore

import scala.concurrent.ExecutionContext

case class AggregateMenagereState(aggregatesActors: List[AggregateId] = Nil) {
  def updated(aggregate: AggregateId): AggregateMenagereState = copy(aggregate :: aggregatesActors)
  def size: Int = aggregatesActors.length
  override def toString: String = aggregatesActors.reverse.toString
}

case class NewAggregateAdded(id: AggregateId)

class AggregateManager[AGGREGATE](actorId: String,
                                  aggregateContext: AggregateContext[AGGREGATE],
                                  documentStore : DocumentStore[AGGREGATE])
                                 (implicit val ec: ExecutionContext) extends PersistentActor {
  def persistenceId = actorId

  val aggregateTypeName = actorId + "_aggregate"

  val aggregatesActors = scala.collection.mutable.HashMap[Long, ActorRef]()

  val receiveCommand: Receive = {
    case c: FirstCommand[_,_] =>
      val newAggregateId = AggregateId(numEvents)
      persist(NewAggregateAdded(newAggregateId)) {
        event: NewAggregateAdded =>
          updateState(event.id)
        this.handleFirstCommand(c,sender(),event.id, c.organizationId)
      }
    case c: Command[_,_] =>
      aggregatesActors.get(c.aggregateId.asLong) match {
        case Some(actor) => actor ! CommandWithSender(sender(),c)
        case None => throw new Exception("Aggregate with id " + c.aggregateId + " not exist")
      }
    case _ => throw CommandException.unknownCommand
  }

  private def handleFirstCommand(command: FirstCommand[_,_], sender: ActorRef, newId: AggregateId, organizationId: OrganizationId): Unit = {

    val firstActor = createAggregateActorsIfNeeded(newId, organizationId)

    firstActor ! CommandWithSender(sender,command)
  }

  private def createAggregateActorsIfNeeded(aggregateId: AggregateId, organizationId: OrganizationId): ActorRef = {
    aggregatesActors.getOrElse(aggregateId.asLong, {
      aggregatesActors.update(aggregateId.asLong, createAggregateActors(aggregateId, organizationId))
      aggregatesActors(aggregateId.asLong)
    })
  }

  private def createAggregateActors(aggregateId: AggregateId, organizationId: OrganizationId) : ActorRef = {
    context.child(aggregateTypeName + "_AggregateRepository_" + aggregateId.asLong).getOrElse(
      context.actorOf(Props(new AggregateRepositoryActor[AGGREGATE](aggregateTypeName + "_AggregateRepository_" + aggregateId.asLong,
        aggregateId, organizationId, aggregateContext, documentStore) {}),aggregateTypeName + "_AggregateRepository_" + aggregateId.asLong)
    )
  }

  val receiveRecover: Receive = {
    case e: Any => println(e)
  }

  var state = AggregateMenagereState()

  def numEvents = state.size

  def updateState(aggregateId: AggregateId): Unit =
    state = state.updated(aggregateId)

}
