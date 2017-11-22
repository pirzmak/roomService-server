package me.server.projections_api.rooms_api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import me.server.domain_api.rooms_api.Room
import me.server.utils.Aggregate

import scala.concurrent.Future

class RoomsProjectionQueryApi(projection: ActorRef)(implicit akkaTimeout: Timeout) {

  def getAllRooms(query: GetAllRooms): Future[List[Aggregate[Room]]] = {
    (projection ? query).mapTo[List[Aggregate[Room]]]
  }

}