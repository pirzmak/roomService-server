package me.server.frontend

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer
import me.server.utils.Config

class FrontendServer(mainRestService: MainRestService)(implicit val actorSystem: ActorSystem) extends Config {

  def start(): Unit = {
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val clientRouteLogged = DebuggingDirectives.logRequestResult("Client ReST", Logging.InfoLevel)(mainRestService.routes)

    Http().bindAndHandle(clientRouteLogged, httpHost, httpPort)

    println("Server started",httpHost,httpPort )

  }


}
