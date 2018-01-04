package me.server.frontend.json

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import me.server.domain_api.reservations_api._
import me.server.domain_api.rooms_api._
import me.server.domain_api.users_api.{CreateUser, PersonInfo, PersonalData, UserId}
import me.server.utils.Aggregate
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}
import me.server.projections_api.reservations_api.GetAllReservations
import me.server.projections_api.rooms_api.GetAllRooms
import spray.json._


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol { //TODO add types
  implicit val aggregateIdJsonFormat = jsonFormat1(AggregateId)
  implicit val organizationIdJsonFormat = jsonFormat1(OrganizationId)
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
  implicit val createUserJsonFormat = jsonFormat5(CreateUser)

  //Reservations
  implicit val personalDataJsonFormat = jsonFormat1(PersonalData)
  implicit val personInfoJsonFormat = jsonFormat5(PersonInfo.apply)
  implicit val reservationJsonFormat = jsonFormat8(Reservation.apply)
  implicit val aggregateReservationJsonFormat = jsonFormat4(Aggregate.apply[Reservation])

  implicit val getAllReservationsJsonFormat = jsonFormat1(GetAllReservations)

  implicit val createReservationJsonFormat = jsonFormat6(CreateReservation)
  implicit val changeDateReservationJsonFormat = jsonFormat5(ChangeDate)
  implicit val changeClientInfoReservationJsonFormat = jsonFormat8(ChangeClientInfo)
  implicit val changeRoomReservationJsonFormat = jsonFormat4(ChangeRoom)
  implicit val changeDiscountReservationJsonFormat = jsonFormat4(ChangeDiscount)
  implicit val changeLoanReservationJsonFormat = jsonFormat4(ChangeLoan)
  implicit val deleteReservationJsonFormat = jsonFormat3(DeleteReservation)
  implicit val activeLoanReservationJsonFormat = jsonFormat3(ActiveReservation)

  //Rooms
  implicit val roomInfoJsonFormat = jsonFormat2(RoomInfo)
  implicit val roomJsonFormat = jsonFormat4(Room.apply)
  implicit val aggregateRoomJsonFormat = jsonFormat4(Aggregate.apply[Room])

  implicit val getAllRoomsJsonFormat = jsonFormat1(GetAllRooms)

  implicit val createRoomJsonFormat = jsonFormat4(CreateRoom)
  implicit val changeRoomInfoJsonFormat = jsonFormat5(ChangeRoomInfo)
  implicit val changeBedsNrJsonFormat = jsonFormat4(ChangeBedsNr)
  implicit val changeRoomCostJsonFormat = jsonFormat4(ChangeRoomCost)
  implicit val deleteRoomJsonFormat = jsonFormat3(DeleteRoom)
  implicit val activeRoomJsonFormat = jsonFormat3(ActiveRoom)
}
