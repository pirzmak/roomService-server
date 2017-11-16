package me.server.projections.rooms_occupancy

import java.time.LocalDate

import akka.actor.ActorSystem
import me.server.domain_api.reservations_api.{ReservationCreated}
import me.server.projections_api.rooms_occupancy_api.{CheckRoomOccupancy, FindFreeRooms, GetRoomOccupancyById, RoomsOccupancy}
import me.server.utils.cqrs.{ EventsListener, MyEvent, ProjectionActor}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.DocumentStore

import scala.concurrent.ExecutionContext

class RoomsOccupancyProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[RoomsOccupancy])
                              (implicit system: ActorSystem, implicit val ec: ExecutionContext)
  extends ProjectionActor {

  def persistenceId = projectionId

  val reservationEventListener = new EventsListener("ReservationManager", eventListening)

  def eventListening(event: MyEvent) = event.event match {
    case e: ReservationCreated => addNewRoomOccupancy(event.aggregateId, e)
    case _ => ()
  }

  override val receiveCommand: Receive = {
    case m: GetRoomOccupancyById => sender() ! getRoomsOccupancyById(m.roomId)
    case m: CheckRoomOccupancy => sender() ! checkRoomOccupancy(m.roomId, m.from, m.to)
    case m: FindFreeRooms => sender() ! findFreeRooms(m.from, m.to)
    case _ => ()
  }

  def getRoomsOccupancyById(id: AggregateId): List[(LocalDate,LocalDate)] = {
    if(documentStore.getDocumentById(id).isDefined)  documentStore.getDocumentById(id).get.aggregate.occupancy else List.empty
  }

  def checkRoomOccupancy(id: AggregateId, from: LocalDate, to: LocalDate): Boolean = {
    if(documentStore.getDocumentById(id).isDefined)  {
      !documentStore.getDocumentById(id).get.aggregate.occupancy.exists(date => date._1.isBefore(to) && date._2.isAfter(from))
    } else false
  }

  def findFreeRooms(from: LocalDate, to: LocalDate): List[AggregateId] = {
    documentStore.getAll.map(d => d.aggregate)
      .filter(ro => !ro.occupancy.exists(date => date._1.isBefore(to) && date._2.isAfter(from)))
      .map(ro => ro.roomId).toList
  }

  def addNewRoomOccupancy(id: AggregateId, e: ReservationCreated): Unit = {
    documentStore.upsertDocument(id, AggregateVersion(0), RoomsOccupancy(id, documentStore.getDocumentById(id).get.aggregate.occupancy))
  }

}
