import java.time.LocalDate

import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.util.Timeout
import me.server.domain.reservations.ReservationsAggregateContext
import me.server.domain_api.reservations_api.Reservation
import me.server.domain.users.UsersAggregateContext
import me.server.domain_api.users_api.{PersonInfo, User}
import me.server.frontend.{FrontendServer, MainRestService}
import me.server.frontend.http.rest.{ReservationsServiceRoute, UsersServiceRoute}
import me.server.projections.reservations.ReservationProjection
import me.server.projections.rooms_occupancy.RoomsOccupancyProjection
import me.server.utils.MockDocumentStore
import me.server.utils.ddd.{AggregateId, AggregateManager, AggregateVersion}
import me.server.projections_api.reservations_api.ReservationProjectionQueryApi
import me.server.projections_api.rooms_occupancy_api.{RoomsOccupancy, RoomsOccupancyQueryApi}

import scala.concurrent.duration._

class Context {

  def init() = {
    implicit val system = ActorSystem("System")
    implicit val timeout = Timeout(25 seconds)

    implicit val ec = global

    //stores
    val reservationsDocumentStore = new MockDocumentStore[Reservation]
    reservationsDocumentStore.insertDocument(AggregateId(-2),AggregateVersion(1),
      Reservation(LocalDate.now(),LocalDate.now().plusWeeks(1),PersonInfo("Jon","Doe","","",None),AggregateId(1),10,None,None,false))

    val roomsOccupancyDocumentStore = new MockDocumentStore[RoomsOccupancy]


    //projections
    val reservationProjection = system.actorOf(Props(new ReservationProjection("ReservationProjection","ReservationManager",reservationsDocumentStore)))
    val reservationProjectionQueryApi = new ReservationProjectionQueryApi(reservationProjection)

    val roomsOccupancyProjection = system.actorOf(Props(new RoomsOccupancyProjection("RoomsOccupancyProjection","RoomsOccupancyManager",roomsOccupancyDocumentStore)))
    val roomsOccupancyQueryApi = new RoomsOccupancyQueryApi(roomsOccupancyProjection)

    val reservationsContextActor = new ReservationsAggregateContext(roomsOccupancyQueryApi)
    val commandHandler = system.actorOf(Props(new AggregateManager[Reservation]("ReservationManager",reservationsContextActor, reservationsDocumentStore)),"ReservationManagerActor")

    val reservationsServiceRoute = new ReservationsServiceRoute(commandHandler, reservationProjectionQueryApi)
    val mainRestServiceActor = new MainRestService(reservationsServiceRoute)

    val frontend = new FrontendServer(mainRestServiceActor)(system)

    frontend
  }

  val (frontend:FrontendServer) = init()
}
