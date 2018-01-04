package me.server.frontend.http.rest

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.directives.SecurityDirectives
import me.server.domain_api.rooms_api._
import me.server.frontend.json.JsonSupport
import me.server.projections_api.rooms_api.{GetAllRooms, GetRoomById, RoomsProjectionQueryApi}
import me.server.utils.cqrs.CommandResult
import akka.pattern.ask
import akka.util.Timeout
import me.server.utils.ddd.{AggregateId, OrganizationId}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class RoomsServiceRoute(val roomsRepository: ActorRef,
                        val roomsProjectionQueryApi: RoomsProjectionQueryApi)
                       (implicit executionContext: ExecutionContext) extends SecurityDirectives with JsonSupport {

  implicit val timeout = Timeout(25 seconds)

  val routes = {
    path("get-all") {
      get {
        complete(roomsProjectionQueryApi.getAllRooms(GetAllRooms(OrganizationId(0))))
      }
    } ~ path("get-by-id"/Segment) { id =>
      get {
        complete(roomsProjectionQueryApi.getRoomById(GetRoomById(AggregateId(id.toLong),OrganizationId(0))))
      }
    } ~ path("create") {
      post {
        entity(as[CreateRoom]) { message =>
          complete((roomsRepository ? message.copy(organizationId = OrganizationId(0))).mapTo[CommandResult])
        }
      }
    } ~ path("change-room-info") {
      post {
        entity(as[ChangeRoomInfo]) { message =>
          complete((roomsRepository ? message.copy(organizationId = OrganizationId(0))).mapTo[CommandResult])
        }
      }
    } ~ path("change-beds-nr") {
      post {
        entity(as[ChangeBedsNr]) { message =>
          complete((roomsRepository ? message.copy(organizationId = OrganizationId(0))).mapTo[CommandResult])
        }
      }
    } ~ path("change-room-cost") {
      post {
        entity(as[ChangeRoomCost]) { message =>
          complete((roomsRepository ? message.copy(organizationId = OrganizationId(0))).mapTo[CommandResult])
        }
      }
    } ~ path("delete") {
      post {
        entity(as[DeleteRoom]) { message =>
          complete((roomsRepository ? message.copy(organizationId = OrganizationId(0))).mapTo[CommandResult])
        }
      }
    } ~ path("active") {
      post {
        entity(as[ActiveRoom]) { message =>
          complete((roomsRepository ? message.copy(organizationId = OrganizationId(0))).mapTo[CommandResult])
        }
      }
    }
  }
}