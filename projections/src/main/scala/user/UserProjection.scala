package user

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.query.scaladsl._
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import me.server.domain.users_api.UserCreated


class UserProjection(implicit system: ActorSystem){
  lazy val readJournal = PersistenceQuery(system).readJournalFor("inmemory-read-journal")
    .asInstanceOf[ReadJournal
    with CurrentPersistenceIdsQuery
    with CurrentEventsByPersistenceIdQuery
    with CurrentEventsByTagQuery
    with EventsByPersistenceIdQuery
    with EventsByTagQuery]

  val source: Source[EventEnvelope, NotUsed] =
    readJournal.eventsByPersistenceId("usersAggregate", 0, Long.MaxValue)

  implicit val mat = ActorMaterializer()

  source.runForeach { event =>  event.event match {
    case e: UserCreated => println("User created")
  } }
}
