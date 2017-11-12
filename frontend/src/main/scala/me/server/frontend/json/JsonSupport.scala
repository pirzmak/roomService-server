package me.server.frontend.json

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import me.server.domain_api.reservations_api._
import me.server.domain_api.users_api.{CreateUser, PersonInfo, PersonalData, UserId}
import me.server.utils.Aggregate
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.projections_api.reservations_api.GetAllReservations
import spray.json._


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val aggregateIdJsonFormat = jsonFormat1(AggregateId)
  implicit val versionIdJsonFormat = jsonFormat1(AggregateVersion)

  implicit val statusResponseJsonFormat = jsonFormat1(StatusResponse.apply)
  implicit val commandSuccessResultJsonFormat = jsonFormat4(CommandResult)

  implicit val localDateJsonFormat: JsonFormat[LocalDate] =
    new JsonFormat[LocalDate] {
      private val formatter = DateTimeFormatter.ISO_DATE

      override def write(x: LocalDate): JsValue = JsString(x.format(formatter))

      override def read(value: JsValue): LocalDate = value match {
        case JsString(x) => LocalDate.parse(x)
        case x => deserializationError("Wrong time format of " + x)
      }
    }

  //User
  implicit val userIdJsonFormat = jsonFormat1(UserId.apply)

  implicit val createUserJsonFormat = jsonFormat4(CreateUser)

  //Reservations
  implicit val personalDataJsonFormat = jsonFormat1(PersonalData)
  implicit val personInfoJsonFormat = jsonFormat5(PersonInfo.apply)
  implicit val reservationJsonFormat = jsonFormat8(Reservation.apply)
  implicit val aggregateJsonFormat = jsonFormat3(Aggregate.apply[Reservation])

  implicit val getAllReservationsJsonFormat = jsonFormat0(GetAllReservations)

  implicit val createReservationJsonFormat = jsonFormat5(CreateReservation)
  implicit val changeDateReservationJsonFormat = jsonFormat4(ChangeDate)
  implicit val changeClientInfoReservationJsonFormat = jsonFormat7(ChangeClientInfo)
  implicit val changeRoomReservationJsonFormat = jsonFormat3(ChangeRoom)
  implicit val changeDiscountReservationJsonFormat = jsonFormat3(ChangeDiscount)
  implicit val changeLoanReservationJsonFormat = jsonFormat3(ChangeLoan)
  implicit val deleteReservationJsonFormat = jsonFormat2(DeleteReservation)
  implicit val activeLoanReservationJsonFormat = jsonFormat2(ActiveReservation)
}
