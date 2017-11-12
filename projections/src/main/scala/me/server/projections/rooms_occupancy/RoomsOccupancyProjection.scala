package me.server.projections.rooms_occupancy

import java.time.LocalDate

import akka.actor.ActorSystem
import me.server.projections_api.rooms_occupancy_api.{CheckRoomOccupancy, FindFreeRooms, GetRoomOccupancyById, RoomsOccupancy}
import me.server.utils.cqrs.ProjectionActor
import me.server.utils.ddd.AggregateId
import me.server.utils.DocumentStore

import scala.concurrent.ExecutionContext

class RoomsOccupancyProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[RoomsOccupancy])
                              (implicit system: ActorSystem, implicit val ec: ExecutionContext) extends ProjectionActor(projectionId, aggregateId) {

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

}
