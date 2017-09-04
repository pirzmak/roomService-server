package me.server.frontend.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import me.server.domain.users_api.{CreateUser, UserId}
import me.server.utils.{AggregateId, CommandResult, StatusResponse, VersionId}
import spray.json._


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val statusResponseJsonFormat = jsonFormat1(StatusResponse.apply)
  implicit val userIdJsonFormat = jsonFormat1(UserId.apply)
  implicit object AggregateIdJsonFormat extends RootJsonFormat[AggregateId] {
    def write(a: AggregateId) = a match {
      case u: UserId => u.toJson
    }
    def read(value: JsValue) =
      value.asJsObject.fields("kind") match {
        case JsString("userId") => value.convertTo[UserId]
      }
  }
  implicit val versionIdJsonFormat = jsonFormat1(VersionId)
  implicit val commandSuccessResultJsonFormat = jsonFormat4(CommandResult)
  implicit val createUserJsonFormat = jsonFormat3(CreateUser)
}
