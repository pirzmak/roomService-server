package me.server.utils.ddd

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.persistence.PersistentActor
import me.server.utils.cqrs._
import akka.util.Timeout

import scala.concurrent.ExecutionContext

case class AggregateMenagereState(events: List[NewAggregateAdded] = Nil) {
  def updated(evt: NewAggregateAdded): AggregateMenagereState = copy(evt :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}

case class NewAggregateAdded()

class AggregateManager[AGGREGATE](actorId: String, aggregateContext: AggregateContext[AGGREGATE])(implicit val ec: ExecutionContext) extends PersistentActor {
  def persistenceId = actorId

  val aggregateTypeName = actorId + "_aggregate"

  val aggregatesActors = scala.collection.mutable.HashMap[Long, ActorRef]()

  val receiveCommand: Receive = {
    case c: FirstCommand[_,_] =>
      persist(NewAggregateAdded()) {
        event: NewAggregateAdded => updateState(event)
        this.handleFirstCommand(c)
      }
    case c: Command[_,_] =>
      aggregatesActors.get(c.aggregateId.asLong) match {
        case Some(actor) => actor ! CommandWithSender(sender(),c)
        case None => throw new Exception("Aggregate with id + " + c.aggregateId + " not exist")
      }
    case _ => throw CommandException.unknownCommand
  }

  private def handleFirstCommand(command: FirstCommand[_,_]): Unit = {

    val newAggregateId = numEvents
    val newActor = createAggregateActorsIfNeeded(AggregateId(newAggregateId))

    createAggregateActorsIfNeeded(AggregateId(newAggregateId)) ! CommandWithSender(sender(),command)
  }

  private def createAggregateActorsIfNeeded(aggregateId: AggregateId): ActorRef = {
    aggregatesActors.getOrElse(aggregateId.asLong, {
      createAggregateActors(aggregateId)
    })
  }

  private def createAggregateActors(aggregateId: AggregateId) : ActorRef = {
    context.child(aggregateTypeName + "_AggregateRepository_" + aggregateId.asLong).getOrElse(
      context.actorOf(Props(new AggregateRepositoryActor[AGGREGATE](aggregateTypeName + "_AggregateRepository_" + aggregateId.asLong,
                                                                    aggregateContext) {}))
    )
  }

  val receiveRecover: Receive = {
    case e: NewAggregateAdded => updateState(e)
    case e: Any => println(e)
  }

  var state = AggregateMenagereState()

  def numEvents = state.size

  def updateState(event: NewAggregateAdded): Unit =
    state = state.updated(event)

}
