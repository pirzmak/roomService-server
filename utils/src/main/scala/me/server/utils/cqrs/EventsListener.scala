package me.server.utils.cqrs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.persistence.query.scaladsl._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

trait EventEvaluator{
  def eventEvaluator(callback: (MyEvent => Unit)) {}
}

class EventsListener(aggregateId: String, eventListening: (MyEvent) => Unit)(implicit system: ActorSystem) extends EventEvaluator {

  private lazy val readJournal = PersistenceQuery(system).readJournalFor("inmemory-read-journal")
    .asInstanceOf[ReadJournal
    with CurrentPersistenceIdsQuery
    with CurrentEventsByPersistenceIdQuery
    with CurrentEventsByTagQuery
    with EventsByPersistenceIdQuery
    with EventsByTagQuery]

  private val source: Source[EventEnvelope, NotUsed] =
    readJournal.eventsByPersistenceId(aggregateId, 0, Long.MaxValue)

  private implicit val mat = ActorMaterializer()

  source.runForeach { event => event.event match {
    case e: MyEvent => eventEvaluator(eventListening)
    case _ => throw new Exception("Dupa")
  }
  }

}
