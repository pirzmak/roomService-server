package me.server.frontend

import akka.http.scaladsl.server.Directives._
import me.server.frontend.http.rest.{ReservationsServiceRoute, RoomsServiceRoute, UsersServiceRoute}
import me.server.utils.CorsSupport

class MainRestService(reservationsServiceRoute: ReservationsServiceRoute,
                      roomsServiceRoute: RoomsServiceRoute) extends CorsSupport {

  val routes =
    pathPrefix("") {
      corsHandler {
        reservationsServiceRoute.routes
      }
    } ~
      pathPrefix("rooms") {
        corsHandler {
          roomsServiceRoute.routes
        }
      }
}
