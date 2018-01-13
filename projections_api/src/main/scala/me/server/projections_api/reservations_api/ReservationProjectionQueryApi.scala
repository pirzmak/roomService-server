package me.server.projections_api.reservations_api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import me.server.domain_api.reservations_api.Reservation
import me.server.utils.ddd.Aggregate

import scala.concurrent.Future

class ReservationProjectionQueryApi(projection: ActorRef)(implicit akkaTimeout: Timeout) {

  def getAllReservations(query: GetAllReservations): Future[List[Aggregate[Reservation]]] = {
    (projection ? query).mapTo[List[Aggregate[Reservation]]]
  }

  def getReservationsFromTo(query: GetReservationsFromTo): Future[List[Reservation]] = {
    (projection ? query).mapTo[List[Reservation]]
  }

  def getReservationById(query: GetReservationById): Future[Option[Aggregate[Reservation]]] = {
    (projection ? query).mapTo[Option[Aggregate[Reservation]]]
  }

}
