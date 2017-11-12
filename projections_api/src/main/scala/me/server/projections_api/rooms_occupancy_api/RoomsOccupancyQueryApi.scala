package me.server.projections_api.rooms_occupancy_api

import java.time.LocalDate

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import me.server.utils.ddd.AggregateId

import scala.concurrent.Future

class RoomsOccupancyQueryApi(projection: ActorRef)(implicit akkaTimeout: Timeout) {

  def getRoomsOccupancyById(query: GetRoomOccupancyById): Future[List[(LocalDate,LocalDate)]] = {
    (projection ? query).mapTo[List[(LocalDate,LocalDate)]]
  }

  def checkRoomOccupancy(query: CheckRoomOccupancy): Future[Boolean] = {
    (projection ? query).mapTo[Boolean]
  }

  def findFreeRooms(query: FindFreeRooms): Future[List[AggregateId]] = {
    (projection ? query).mapTo[List[AggregateId]]
  }

}
