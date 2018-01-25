package me.server.projections.rooms_occupancy

import java.time.LocalDate

import akka.actor.ActorSystem
import me.server.domain_api.reservations_api.{DateChanged, ReservationCreated, ReservationEvent, RoomChanged}
import me.server.projections_api.rooms_occupancy_api._
import me.server.utils.cqrs.{EventsListener, ProjectionActor}
import me.server.utils.ddd.{AggregateId, OrganizationId}
import me.server.utils.documentStore.DocumentStore

import scala.concurrent.ExecutionContext

class RoomsOccupancyProjection(projectionId: String, aggregateId: String,
                               documentStore: DocumentStore[RoomsOccupancy])
                              (implicit system: ActorSystem, implicit val ec: ExecutionContext)
  extends ProjectionActor {

  def persistenceId = projectionId

  val reservationEventListener = new EventsListener[ReservationEvent](eventListening)

  def eventListening(event: ReservationEvent, id: AggregateId, organizationId: OrganizationId) = event match {
    case e: ReservationCreated => addNewRoomOccupancy(id, organizationId, e)
    case e: DateChanged => dateChanged(id, organizationId, e)
    case e: RoomChanged => roomChanged(id, organizationId, e)
    case _ => ()
  }

  def addNewRoomOccupancy(id: AggregateId, organizationId: OrganizationId, e: ReservationCreated): Unit = {
    documentStore.upsertDocument(e.roomId, organizationId, RoomsOccupancy(ReservationInfo(e.roomId,e.from, e.to) :: getRoomsReservationInfoById(e.roomId, organizationId)))
  }

  def dateChanged(id: AggregateId, organizationId: OrganizationId, e: DateChanged): Unit = {
    val roomReservation = documentStore.getAll(organizationId).find(r => r.aggregate.occupancy.exists(o => o.reservationID == id))
    documentStore.upsertDocument(roomReservation.get.aggregateId, organizationId, RoomsOccupancy(roomReservation.get.aggregate.occupancy.
      map(r => if(id.asLong == r.reservationID.asLong) ReservationInfo(id, e.from.getOrElse(r.from), e.to.getOrElse(r.to)) else r)))
  }

  def roomChanged(id: AggregateId, organizationId: OrganizationId, e: RoomChanged): Unit = {
    documentStore.upsertDocument(e.oldRoomId, organizationId, RoomsOccupancy(getRoomsReservationInfoById(e.oldRoomId, organizationId).filter(r => r.reservationID != id)))
    documentStore.upsertDocument(e.newRoomId, organizationId,
      RoomsOccupancy(getRoomsReservationInfoById(e.oldRoomId, organizationId).
        find(r => r.reservationID == id).get :: getRoomsReservationInfoById(e.newRoomId, organizationId)) )
  }

  private def getRoomsReservationInfoById(id: AggregateId, organizationId: OrganizationId): List[ReservationInfo] ={
    if(documentStore.getDocumentById(id,organizationId).isDefined)
      documentStore.getDocumentById(id,organizationId).get.aggregate.occupancy else List.empty
  }

  override val receiveCommand: Receive = {
    case m: GetRoomOccupancyById => sender() ! getRoomsOccupancyById(m.roomId, m.organizationId)
    case m: CheckRoomOccupancy => sender() ! checkRoomOccupancy(m.roomId, m.organizationId, m.from, m.to)
    case m: FindFreeRooms => sender() ! findFreeRooms(m.organizationId,m.from, m.to)
    case _ => ()
  }

  def getRoomsOccupancyById(id: AggregateId, organizationId: OrganizationId): List[(LocalDate,LocalDate)] = {
    if(documentStore.getDocumentById(id, organizationId).isDefined)
      documentStore.getDocumentById(id, organizationId).get.aggregate.occupancy.map(o => (o.from, o.to)) else List.empty
  }

  def checkRoomOccupancy(id: AggregateId, organizationId: OrganizationId, from: LocalDate, to: LocalDate): Boolean = {
    if(documentStore.getDocumentById(id, organizationId).isDefined)  {
      !documentStore.getDocumentById(id, organizationId).get.aggregate.occupancy.exists(o => o.from.isBefore(to) || o.to.isAfter(from))
    } else false
  }

  def findFreeRooms(organizationId: OrganizationId, from: LocalDate, to: LocalDate): List[AggregateId] = {
    documentStore.getAll(organizationId).map(d => (d.aggregateId,d.aggregate))
      .filter(myDual => !myDual._2.occupancy.exists(o => o.from.isBefore(to) && o.to.isAfter(from)))
      .map(myDual => myDual._1).toList
  }

}
