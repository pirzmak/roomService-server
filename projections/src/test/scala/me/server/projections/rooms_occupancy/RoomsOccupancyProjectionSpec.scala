package me.server.projections.rooms_occupancy

import java.time.LocalDate

import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import me.server.projections_api.rooms_occupancy_api._
import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}
import me.server.utils.documentStore.MockDocumentStore
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

      commandHandler ! GetRoomOccupancyById(AggregateId(1),OrganizationId(0))
      expectMsg(List.empty)
    }

    "get result with 1 aggregate" in {
      val documentStore = new MockDocumentStore[RoomsOccupancy]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),OrganizationId(0),RoomsOccupancy(List(ReservationInfo(AggregateId(0), LocalDate.now(), LocalDate.now().plusDays(7)))))
      val commandHandler = system.actorOf(Props(new RoomsOccupancyProjection("Test","TestP",documentStore)))

      commandHandler ! GetRoomOccupancyById(AggregateId(0),OrganizationId(0))
      expectMsg(List((LocalDate.now(), LocalDate.now().plusDays(7))))
    }

    "get result for 1 room and specific date" in {
      val documentStore = new MockDocumentStore[RoomsOccupancy]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),OrganizationId(0),RoomsOccupancy(List(ReservationInfo(AggregateId(0), LocalDate.now(), LocalDate.now().plusDays(7)))))
      val commandHandler = system.actorOf(Props(new RoomsOccupancyProjection("Test","TestP",documentStore)))

      commandHandler ! CheckRoomOccupancy(AggregateId(0),OrganizationId(0),LocalDate.now(), LocalDate.now().plusDays(7))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),OrganizationId(0),LocalDate.now(), LocalDate.now().plusDays(5))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),OrganizationId(0),LocalDate.now().plusDays(4), LocalDate.now().plusDays(7))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),OrganizationId(0),LocalDate.now().plusDays(7), LocalDate.now().plusDays(12))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),OrganizationId(0),LocalDate.now().plusDays(10), LocalDate.now().plusDays(12))
      expectMsg(false)
      commandHandler ! CheckRoomOccupancy(AggregateId(0),OrganizationId(0),LocalDate.now().minusDays(10), LocalDate.now())
      expectMsg(false)
    }

    "find free room" in {
      val documentStore = new MockDocumentStore[RoomsOccupancy]
      documentStore.insertDocument(AggregateId(0),AggregateVersion(1),OrganizationId(0),RoomsOccupancy(List(ReservationInfo(AggregateId(0), LocalDate.now(), LocalDate.now().plusDays(7)),ReservationInfo(AggregateId(0), LocalDate.now().plusDays(12), LocalDate.now().plusDays(15)))))
      documentStore.insertDocument(AggregateId(1),AggregateVersion(1),OrganizationId(0),RoomsOccupancy(List(ReservationInfo(AggregateId(0),LocalDate.now().plusDays(12), LocalDate.now().plusDays(15)))))
      val commandHandler = system.actorOf(Props(new RoomsOccupancyProjection("Test","TestP",documentStore)))

      commandHandler ! FindFreeRooms(OrganizationId(0),LocalDate.now().plusDays(5), LocalDate.now().plusDays(12))
      expectMsg(List(AggregateId(1)))
      commandHandler ! FindFreeRooms(OrganizationId(0),LocalDate.now().plusDays(7), LocalDate.now().plusDays(12))
      expectMsg(List(AggregateId(1),AggregateId(0)))
    }
  }
}
