package me.server.utils.cqrs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.query.{EventEnvelope, Offset, PersistenceQuery}
import akka.persistence.query.scaladsl._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import me.server.utils.ddd.{AggregateId, OrganizationId}

class EventsListener[TYPE](eventListening: (TYPE, AggregateId, OrganizationId) => Unit)(implicit system: ActorSystem) {

  private lazy val readJournal = PersistenceQuery(system).readJournalFor("inmemory-read-journal")
    .asInstanceOf[ReadJournal
    with CurrentPersistenceIdsQuery
    with CurrentEventsByPersistenceIdQuery
    with CurrentEventsByTagQuery
    with EventsByPersistenceIdQuery
    with EventsByTagQuery]

  private val source: Source[EventEnvelope, NotUsed] =
    readJournal.eventsByTag("myEvent", Offset.noOffset)
  private implicit val mat = ActorMaterializer()


  source.runForeach { event => event.event match {
    case e: MyEvent => e.event match {
      case ev: TYPE => eventListening(ev,e.aggregateId,e.organizationId)
      case _ => ()
    }
    case _ => throw new Exception("Dupa")
  }
  }
}
