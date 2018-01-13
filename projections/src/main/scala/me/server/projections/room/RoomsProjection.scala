package me.server.projections.room

import akka.actor.ActorSystem
import me.server.domain_api.rooms_api.Room
import me.server.projections_api.rooms_api.{GetAllRooms, GetRoomById}
import me.server.utils.cqrs.ProjectionActor
import me.server.utils.ddd.{Aggregate, AggregateId, OrganizationId}
import me.server.utils.documentStore.DocumentStore

import scala.concurrent.ExecutionContext

class RoomsProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[Room])
                     (implicit system: ActorSystem, implicit val ec: ExecutionContext) extends ProjectionActor {

  def persistenceId = projectionId

  override val receiveCommand: Receive = {
    case m: GetAllRooms => sender() ! getAllRooms(m.organizationId)
    case m: GetRoomById => sender() ! getRoomById(m.roomId, m.organizationId)
    case _ => ()
  }

  def getAllRooms(organizationId: OrganizationId): List[Aggregate[Room]] = {
    documentStore.getAll(organizationId).toList
  }

  def getRoomById(roomId: AggregateId, organizationId: OrganizationId): Option[Aggregate[Room]] = {
    documentStore.getDocumentById(roomId,organizationId)
  }
}
