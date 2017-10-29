package me.server.frontend.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import me.server.domain.users_api.{CreateUser, UserId}
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import spray.json._


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val aggregateIdJsonFormat = jsonFormat1(AggregateId)

  implicit val statusResponseJsonFormat = jsonFormat1(StatusResponse.apply)
  implicit val userIdJsonFormat = jsonFormat1(UserId.apply)

  implicit val versionIdJsonFormat = jsonFormat1(AggregateVersion)
  implicit val commandSuccessResultJsonFormat = jsonFormat4(CommandResult)
  implicit val createUserJsonFormat = jsonFormat4(CreateUser)
}
