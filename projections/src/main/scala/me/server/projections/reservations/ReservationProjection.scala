package me.server.projections.reservations

import java.time.LocalDate

import akka.actor.ActorSystem
import me.server.domain_api.reservations_api.Reservation
import me.server.projections_api.reservations_api.{GetAllReservations, GetReservationById, GetReservationsFromTo}
import me.server.utils.{Aggregate, DocumentStore}
import me.server.utils.cqrs.ProjectionActor
import me.server.utils.ddd.{AggregateId, OrganizationId}

import scala.concurrent.ExecutionContext

class ReservationProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[Reservation])
                           (implicit system: ActorSystem, implicit val ec: ExecutionContext) extends ProjectionActor {

  def persistenceId = projectionId

  override val receiveCommand: Receive = {
    case m: GetReservationById => sender() ! getReservationById(m.id, m.organizationId)
    case m: GetAllReservations => sender() ! getAllReservations(m.organizationId)
    case m: GetReservationsFromTo => sender() ! getReservationsFromTo(m.from, m.to, m.organizationId)
    case _ => ()
  }

  def getAllReservations(organizationId: OrganizationId): List[Aggregate[Reservation]] = {
    documentStore.getAll(organizationId).toList
  }

  def getReservationById(id: AggregateId, organizationId: OrganizationId): Option[Aggregate[Reservation]] = {
    var a = documentStore.getDocumentById(id, organizationId)
    documentStore.getDocumentById(id, organizationId)
  }

  def getReservationsFromTo(from: LocalDate, to: LocalDate, organizationId: OrganizationId): List[Aggregate[Reservation]] = {
    documentStore.getAll(organizationId).toList.filter(r => r.aggregate.from.isAfter(from.minusDays(1)) || r.aggregate.to.isBefore(to.plusDays(1)))
  }

}
