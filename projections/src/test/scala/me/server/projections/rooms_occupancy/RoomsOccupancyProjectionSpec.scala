package me.server.projections.rooms_occupancy

import java.time.LocalDate

import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import me.server.domain.reservations.ReservationsAggregateContext
import me.server.projections_api.rooms_occupancy_api.{CheckRoomOccupancy, FindFreeRooms, GetRoomOccupancyById, RoomsOccupancy}
import me.server.utils.MockDocumentStore
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class RoomsOccupancyProjectionSpec extends TestKit(ActorSystem("ReservaationsProjectionSpec"))  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  implicit val timeout = Timeout(5 seconds)
  implicit val ec = global

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  "An RoomsOccupancy actor" must {

    "get result with 0 aggregate" in {
      val documentStore = new MockDocumentStore[RoomsOccupancy]
      val commandHandler = system.actorOf(Props(new RoomsOccupancyProjection("Test","TestP",documentStore)))

      commandHandler ! GetRoomOccupancyById(AggregateId(1))
      expectMsg(List.empty)
    }

    "get result with 1 aggregate" in {
      val documentStore = new MockDocumentStore[RoomsOccupancy]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),RoomsOccupancy(AggregateId(0), List((LocalDate.now(), LocalDate.now().plusDays(7)))))
      val commandHandler = system.actorOf(Props(new RoomsOccupancyProjection("Test","TestP",documentStore)))

      commandHandler ! GetRoomOccupancyById(AggregateId(0))
      expectMsg(List((LocalDate.now(), LocalDate.now().plusDays(7))))
    }

    "get result for 1 room and specific date" in {
      val documentStore = new MockDocumentStore[RoomsOccupancy]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),RoomsOccupancy(AggregateId(0), List((LocalDate.now(), LocalDate.now().plusDays(7)))))
      val commandHandler = system.actorOf(Props(new RoomsOccupancyProjection("Test","TestP",documentStore)))

      commandHandler ! CheckRoomOccupancy(AggregateId(0),LocalDate.now(), LocalDate.now().plusDays(7))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),LocalDate.now(), LocalDate.now().plusDays(5))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),LocalDate.now().plusDays(4), LocalDate.now().plusDays(7))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),LocalDate.now().plusDays(7), LocalDate.now().plusDays(12))
      expectMsg(true)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),LocalDate.now().plusDays(10), LocalDate.now().plusDays(12))
      expectMsg(true)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),LocalDate.now().minusDays(10), LocalDate.now())
      expectMsg(true)
    }

    "find free room" in {
      val documentStore = new MockDocumentStore[RoomsOccupancy]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),RoomsOccupancy(AggregateId(0), List((LocalDate.now(), LocalDate.now().plusDays(7)),(LocalDate.now().plusDays(12), LocalDate.now().plusDays(15)))))
      documentStore.insertDocument(AggregateId(1),AggregateVersion(1),RoomsOccupancy(AggregateId(1), List((LocalDate.now().plusDays(12), LocalDate.now().plusDays(15)))))
      val commandHandler = system.actorOf(Props(new RoomsOccupancyProjection("Test","TestP",documentStore)))

      commandHandler ! FindFreeRooms(LocalDate.now().plusDays(5), LocalDate.now().plusDays(12))
      expectMsg(List(AggregateId(1)))
      commandHandler ! FindFreeRooms(LocalDate.now().plusDays(7), LocalDate.now().plusDays(12))
      expectMsg(List(AggregateId(1),AggregateId(0)))
    }
  }

  "An Reservation actor with 2 spcific date" must {
//    val documentStore = new MockDocumentStore[Reservation]
//    val dateFrom1 = LocalDate.of(2017,6,6)
//    val dateTo1 = LocalDate.of(2017,6,15)
//    val dateFrom2 = LocalDate.of(2017,7,10)
//    val dateTo2 = LocalDate.of(2017,7,18)
//    documentStore.insertDocument(AggregateId(0),AggregateVersion(1),Reservation(dateFrom1,dateTo1,PersonInfo.empty,AggregateId(0),0,None,None,false))
//    documentStore.insertDocument(AggregateId(0),AggregateVersion(1),Reservation(dateFrom2,dateTo2,PersonInfo.empty,AggregateId(0),0,None,None,false))
//
//    "get result from 2017.06.01 to 2017.06.12" in {
//      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))
//
//      commandHandler ! GetReservationsFromTo(LocalDate.of(2017,6,1),LocalDate.of(2017,6,12))
//      expectMsg(List(Aggregate(AggregateId(0),AggregateVersion(1),Reservation(dateFrom1,dateTo1,PersonInfo.empty,AggregateId(0),0,None,None,false))))
//    }
//
//    "get result from 2017.06.01 to 2017.06.03" in {
//      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))
//
//      commandHandler ! GetReservationsFromTo(LocalDate.of(2017,6,1),LocalDate.of(2017,6,3))
//      expectMsg(List.empty)
//    }
//
//    "get result from 2017.06.01 to 2017.09.03" in {
//      val commandHandler = system.actorOf(Props(new ReservationProjection("Test","TestP",documentStore)))
//
//      commandHandler ! GetReservationsFromTo(LocalDate.of(2017,6,1),LocalDate.of(2017,6,3))
//      expectMsg(List(Aggregate(AggregateId(0),AggregateVersion(1),Reservation(dateFrom1,dateTo1,PersonInfo.empty,AggregateId(0),0,None,None,false)),
//        Aggregate(AggregateId(0),AggregateVersion(1),Reservation(dateFrom2,dateTo2,PersonInfo.empty,AggregateId(0),0,None,None,false))))
//    }
  }

}
