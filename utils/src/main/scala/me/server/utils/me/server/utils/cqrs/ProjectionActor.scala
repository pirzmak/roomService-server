package me.server.utils.me.server.utils.cqrs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.PersistentActor
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.persistence.query.scaladsl._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source


trait EventEvaluator {
  def eventEvaluator(event : MyEvent){}
}

trait receiverQuery {
  def receiveQuery()
}

abstract class ProjectionActor(projectionId: String, aggregateId: String)(implicit system: ActorSystem)
  extends PersistentActor with EventEvaluator{
  def persistenceId = projectionId

  lazy val readJournal = PersistenceQuery(system).readJournalFor("inmemory-read-journal")
    .asInstanceOf[ReadJournal
    with CurrentPersistenceIdsQuery
    with CurrentEventsByPersistenceIdQuery
    with CurrentEventsByTagQuery
    with EventsByPersistenceIdQuery
    with EventsByTagQuery]

  val source: Source[EventEnvelope, NotUsed] =
    readJournal.eventsByPersistenceId(aggregateId, 0, Long.MaxValue)

  implicit val mat = ActorMaterializer()

  source.runForeach { event =>  event.event match {
    case e: MyEvent => eventEvaluator(e)
    case _ => throw new Exception("Dupa")
  } }

  val receiveCommand: Receive = {
    case _ => ()
  }

  val receiveRecover: Receive = {
    case _ =>()
  }
}
