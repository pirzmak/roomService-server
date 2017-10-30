package reservations

import java.time.LocalDate

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import akka.dispatch.ExecutionContexts.global
import me.server.domain.reservations.ReservationsAggregateContext
import me.server.domain.reservations_api._
import me.server.domain.users_api.PersonInfo
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.tests.TestAggregateRepositoryActor
import me.server.utils.{Aggregate, MockDocumentStore}
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen, Matchers, WordSpecLike}
import reservations_api.{GetAllReservations, GetReservationsFromTo}

import scala.concurrent.duration._

class ReservationProjectionSpec extends TestKit(ActorSystem("ReservaationsProjectionSpec"))  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  implicit val timeout = Timeout(5 seconds)
  implicit val ec = global

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val reservationAggContext = new ReservationsAggregateContext()

  "An Reservation actor" must {

    "get result with 0 aggregate" in {
      val documentStore = new MockDocumentStore[Reservation]
      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))

      commandHandler ! GetAllReservations()
      expectMsg(List.empty)
    }

    "get result with 1 aggregate" in {
      val documentStore = new MockDocumentStore[Reservation]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),Reservation.empty)
      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))

      commandHandler ! GetAllReservations()
      expectMsg(List(Aggregate(AggregateId(0),AggregateVersion(1),Reservation.empty)))
    }

    "get result with 2 aggregate" in {
      val documentStore = new MockDocumentStore[Reservation]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),Reservation.empty)
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),Reservation.empty)
      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))

      commandHandler ! GetAllReservations()
      expectMsg(List(Aggregate(AggregateId(0),AggregateVersion(1),Reservation.empty),Aggregate(AggregateId(0),AggregateVersion(1),Reservation.empty)))
    }
  }

  "An Reservation actor with 2 spcific date" must {
    val documentStore = new MockDocumentStore[Reservation]
    val dateFrom1 = LocalDate.of(2017,6,6)
    val dateTo1 = LocalDate.of(2017,6,15)
    val dateFrom2 = LocalDate.of(2017,7,10)
    val dateTo2 = LocalDate.of(2017,7,18)
    documentStore.insertDocument(AggregateId(0),AggregateVersion(1),Reservation(dateFrom1,dateTo1,PersonInfo.empty,AggregateId(0),0,None,None,false))
    documentStore.insertDocument(AggregateId(0),AggregateVersion(1),Reservation(dateFrom2,dateTo2,PersonInfo.empty,AggregateId(0),0,None,None,false))

    "get result from 2017.06.01 to 2017.06.12" in {
      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))

      commandHandler ! GetReservationsFromTo(LocalDate.of(2017,6,1),LocalDate.of(2017,6,12))
      expectMsg(List(Aggregate(AggregateId(0),AggregateVersion(1),Reservation(dateFrom1,dateTo1,PersonInfo.empty,AggregateId(0),0,None,None,false))))
    }

    "get result from 2017.06.01 to 2017.06.03" in {
      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))

      commandHandler ! GetReservationsFromTo(LocalDate.of(2017,6,1),LocalDate.of(2017,6,3))
      expectMsg(List.empty)
    }

    "get result from 2017.06.01 to 2017.09.03" in {
      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))

      commandHandler ! GetReservationsFromTo(LocalDate.of(2017,6,1),LocalDate.of(2017,6,3))
      expectMsg(List(Aggregate(AggregateId(0),AggregateVersion(1),Reservation(dateFrom1,dateTo1,PersonInfo.empty,AggregateId(0),0,None,None,false)),
        Aggregate(AggregateId(0),AggregateVersion(1),Reservation(dateFrom2,dateTo2,PersonInfo.empty,AggregateId(0),0,None,None,false))))
    }
  }

}