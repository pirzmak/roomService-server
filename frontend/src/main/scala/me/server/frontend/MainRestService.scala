package me.server.frontend

import akka.http.scaladsl.server.Directives._
import me.server.frontend.http.rest.UsersServiceRoute
import me.server.utils.CorsSupport

class MainRestService(usersServiceRoute: UsersServiceRoute) extends CorsSupport {

  val routes =
      pathPrefix("home") {
        corsHandler {
          usersServiceRoute.routes
        }
      }

}
