package me.server.projections.room

import akka.actor.ActorSystem
import me.server.domain_api.reservations_api.Reservation
import me.server.domain_api.rooms_api.Room
import me.server.projections_api.rooms_api.GetAllRooms
import me.server.utils.cqrs.ProjectionActor
import me.server.utils.{Aggregate, DocumentStore}

import scala.concurrent.ExecutionContext

class RoomsProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[Room])
                     (implicit system: ActorSystem, implicit val ec: ExecutionContext) extends ProjectionActor {

  def persistenceId = projectionId

  override val receiveCommand: Receive = {
    case m: GetAllRooms => sender() ! getAllRooms()
    case _ => ()
  }

  def getAllRooms(): List[Aggregate[Room]] = {
    documentStore.getAll.toList
  }
}
