package me.server.frontend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import me.server.utils.{Config}

class FrontendServer(mainRestService: MainRestService)(implicit val actorSystem: ActorSystem) extends Config {

  def start(): Unit = {
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    Http().bindAndHandle(mainRestService.routes, httpHost, httpPort)

    println("Server started")

  }


}
