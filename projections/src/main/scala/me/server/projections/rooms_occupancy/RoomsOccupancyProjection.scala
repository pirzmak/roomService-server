package me.server.projections.rooms_occupancy

import java.time.LocalDate

import akka.actor.ActorSystem
import me.server.domain_api.reservations_api.{DateChanged, ReservationCreated, ReservationEvent, RoomChanged}
import me.server.projections_api.reservations_api.{GetReservationById, ReservationProjectionQueryApi}
import me.server.projections_api.rooms_occupancy_api._
import me.server.utils.cqrs.{EventsListener, MyEvent, ProjectionActor}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.DocumentStore

import scala.concurrent.ExecutionContext

class RoomsOccupancyProjection(projectionId: String, aggregateId: String,
                               documentStore: DocumentStore[RoomsOccupancy])
                              (implicit system: ActorSystem, implicit val ec: ExecutionContext)
  extends ProjectionActor {

  def persistenceId = projectionId

  val reservationEventListener = new EventsListener[ReservationEvent](eventListening)

  def eventListening(event: ReservationEvent, id: AggregateId) = event match {
    case e: ReservationCreated => addNewRoomOccupancy(id, e)
    case e: DateChanged => dateChanged(id, e)
    case e: RoomChanged => roomChanged(id, e)
    case _ => ()
  }

  def addNewRoomOccupancy(id: AggregateId, e: ReservationCreated): Unit = {
    documentStore.upsertDocument(e.roomId, RoomsOccupancy(ReservationInfo(e.roomId,e.from, e.to) :: getRoomsReservationInfoById(e.roomId)))
  }

  def dateChanged(id: AggregateId, e: DateChanged): Unit = {
    val roomReservation = documentStore.getAll.find(r => r.aggregate.occupancy.exists(o => o.reservationID == id))
    documentStore.upsertDocument(roomReservation.get.aggregateId, RoomsOccupancy(roomReservation.get.aggregate.occupancy.
      map(r => if(id.asLong == r.reservationID.asLong) ReservationInfo(id, e.from.getOrElse(r.from), e.to.getOrElse(r.to)) else r)))
  }

  def roomChanged(id: AggregateId, e: RoomChanged): Unit = {
    documentStore.upsertDocument(e.oldRoomId, RoomsOccupancy(getRoomsReservationInfoById(e.oldRoomId).filter(r => r.reservationID != id)))
    documentStore.upsertDocument(e.newRoomId,
      RoomsOccupancy(getRoomsReservationInfoById(e.oldRoomId).
        find(r => r.reservationID == id).get :: getRoomsReservationInfoById(e.newRoomId)) )
  }

  private def getRoomsReservationInfoById(id: AggregateId): List[ReservationInfo] ={
    if(documentStore.getDocumentById(id).isDefined)  documentStore.getDocumentById(id).get.aggregate.occupancy else List.empty
  }

  override val receiveCommand: Receive = {
    case m: GetRoomOccupancyById => sender() ! getRoomsOccupancyById(m.roomId)
    case m: CheckRoomOccupancy => sender() ! checkRoomOccupancy(m.roomId, m.from, m.to)
    case m: FindFreeRooms => sender() ! findFreeRooms(m.from, m.to)
    case _ => ()
  }

  def getRoomsOccupancyById(id: AggregateId): List[(LocalDate,LocalDate)] = {
    if(documentStore.getDocumentById(id).isDefined)
      documentStore.getDocumentById(id).get.aggregate.occupancy.map(o => (o.from, o.to)) else List.empty
  }

  def checkRoomOccupancy(id: AggregateId, from: LocalDate, to: LocalDate): Boolean = {
    if(documentStore.getDocumentById(id).isDefined)  {
      !documentStore.getDocumentById(id).get.aggregate.occupancy.exists(o => o.from.isBefore(to) && o.to.isAfter(from))
    } else false
  }

  def findFreeRooms(from: LocalDate, to: LocalDate): List[AggregateId] = {
    documentStore.getAll.map(d => (d.aggregateId,d.aggregate))
      .filter(myDual => !myDual._2.occupancy.exists(o => o.from.isBefore(to) && o.to.isAfter(from)))
      .map(myDual => myDual._1).toList
  }

}
