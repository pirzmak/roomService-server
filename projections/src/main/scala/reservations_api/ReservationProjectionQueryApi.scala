package reservations_api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import me.server.domain.reservations_api.Reservation
import me.server.domain.users_api.User

import scala.concurrent.Future

class ReservationProjectionQueryApi(projection: ActorRef)(implicit akkaTimeout: Timeout) {

  def getAllReservations(query: GetAllReservations): Future[List[Reservation]] = {
    (projection ? query).mapTo[List[Reservation]]
  }

  def getReservationsFromTo(query: GetReservationsFromTo): Future[List[Reservation]] = {
    (projection ? query).mapTo[List[Reservation]]
  }

}
