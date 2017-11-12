package me.server.frontend

import akka.http.scaladsl.server.Directives._
import me.server.frontend.http.rest.{ReservationsServiceRoute, UsersServiceRoute}
import me.server.utils.CorsSupport

class MainRestService(reservationsServiceRoute: ReservationsServiceRoute) extends CorsSupport {

  val routes =
      pathPrefix("") {
        corsHandler {
          reservationsServiceRoute.routes
        }
      }

}
