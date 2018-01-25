package me.server.projections_api.rooms_occupancy_api

import java.time.LocalDate

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import me.server.utils.ddd.AggregateId

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class RoomsOccupancyQueryApi(projection: ActorRef)(implicit akkaTimeout: Timeout) {

  def getRoomsOccupancyById(query: GetRoomOccupancyById): Future[List[(LocalDate,LocalDate)]] = {
    (projection ? query).mapTo[List[(LocalDate,LocalDate)]]
  }

  def checkRoomOccupancy(query: CheckRoomOccupancy): Future[Boolean] = {
    (projection ? query).mapTo[Boolean]
  }

  def checkRoomOccupancyAsync(query: CheckRoomOccupancy): Boolean = {
    Await.result(projection ? query, 5 seconds) match {
      case b: Boolean => b
      case _ => throw new Exception("Match error")
    }
  }

  def findFreeRooms(query: FindFreeRooms): Future[List[AggregateId]] = {
    (projection ? query).mapTo[List[AggregateId]]
  }

}
