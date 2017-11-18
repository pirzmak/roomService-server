package me.server.frontend.http.rest

import akka.http.scaladsl.server.Directives.{complete, get, path, pathPrefix}
import akka.http.scaladsl.server.directives.SecurityDirectives
import me.server.frontend.json.JsonSupport
import me.server.projections_api.reservations_api.GetAllReservations

import scala.concurrent.ExecutionContext

class RoomsServiceRoute()(implicit executionContext: ExecutionContext) extends SecurityDirectives with JsonSupport {


  val routes = {
    path("get-all") {
      get {
        complete("")
      }
    }
  }
}