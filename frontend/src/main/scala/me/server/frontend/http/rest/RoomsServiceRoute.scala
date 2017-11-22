package me.server.frontend.http.rest

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.directives.SecurityDirectives
import me.server.frontend.json.JsonSupport
import me.server.projections_api.rooms_api.{GetAllRooms, RoomsProjectionQueryApi}

import scala.concurrent.ExecutionContext

class RoomsServiceRoute(val roomsRepository: ActorRef,
                        val roomsProjectionQueryApi: RoomsProjectionQueryApi)(implicit executionContext: ExecutionContext) extends SecurityDirectives with JsonSupport {


  val routes = {
    path("get-all") {
      get {
        complete(roomsProjectionQueryApi.getAllRooms(GetAllRooms()))
      }
    }
  }
}