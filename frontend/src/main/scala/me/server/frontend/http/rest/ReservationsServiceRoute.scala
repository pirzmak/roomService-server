package me.server.frontend.http.rest

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.SecurityDirectives
import akka.util.Timeout
import akka.pattern.ask
import me.server.domain_api.reservations_api._
import me.server.frontend.json.JsonSupport
import me.server.utils.cqrs.CommandResult
import me.server.projections_api.reservations_api.{GetAllReservations, ReservationProjectionQueryApi}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class ReservationsServiceRoute(val reservationsRepository: ActorRef,
                               val reservationsProjectionQueryApi: ReservationProjectionQueryApi)
                              (implicit executionContext: ExecutionContext) extends SecurityDirectives with JsonSupport {

  implicit val timeout = Timeout(25 seconds)

  val routes = {
    path("get-all") {
      get {
        complete(reservationsProjectionQueryApi.getAllReservations(GetAllReservations()))
      }
    } ~
      path("create") {
        post {
          entity(as[CreateReservation]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("change-date") {
        post {
          entity(as[ChangeDate]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("change-client-info") {
        post {
          entity(as[ChangeClientInfo]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("change-room") {
        post {
          entity(as[ChangeRoom]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("change-disocunt") {
        post {
          entity(as[ChangeDiscount]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("change-loan") {
        post {
          entity(as[ChangeLoan]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      }~
      path("delete") {
        post {
          entity(as[DeleteReservation]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("active") {
        post {
          entity(as[ActiveReservation]) { message =>
            complete((reservationsRepository ? message).mapTo[CommandResult])
          }
        }
      }
  }
}
