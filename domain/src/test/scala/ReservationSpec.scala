import java.time.LocalDate

import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import me.server.domain.reservations.ReservationsAggregateContext
import me.server.domain.reservations_api._
import me.server.domain.users_api.PersonInfo
import me.server.utils.MockDocumentStore
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.tests.TestAggregateRepositoryActor
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen, Matchers, WordSpecLike}

import scala.concurrent.duration._

class ReservationSpec() extends TestKit(ActorSystem("ReservationSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  implicit val timeout = Timeout(5 seconds)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  val reservationAggContext = new ReservationsAggregateContext()
  val documentStore = new MockDocumentStore[Reservation]

  "An Reservation actor" must {

    "get result with 1 aggregate and 1 version" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Reservation](AggregateId(-1),reservationAggContext,documentStore)))

      commandHandler ! CreateReservation(LocalDate.now(), LocalDate.now().plusDays(2), PersonInfo.empty, AggregateId(0), None)
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(1), ""))
    }

    "get result with wrong date" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Reservation](AggregateId(-1),reservationAggContext,documentStore)))

      commandHandler ! CreateReservation(LocalDate.now().plusDays(1), LocalDate.now(), PersonInfo.empty, AggregateId(0), None)
      expectMsgPF() {
        case CommandResult(StatusResponse.failure, _, _, _) => ()
      }
    }
  }


  "An Reservation actor" must {

    "get result with 1 aggregate and next version" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Reservation](AggregateId(-1),reservationAggContext,documentStore)))
      var version = 1

      commandHandler ! CreateReservation(LocalDate.now(), LocalDate.now().plusDays(1), PersonInfo.empty, AggregateId(0), None)
      commandHandler ! ChangeDate(AggregateId(-1), AggregateVersion(1), Some(LocalDate.now()), None)
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(version), ""))
      version = version+1
      commandHandler ! ChangeClientInfo(AggregateId(-1), AggregateVersion(1),None, None, None, None, None)
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(version), ""))
      version = version+1
      commandHandler ! ChangeRoom(AggregateId(-1), AggregateVersion(1), AggregateId(-1))
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(version), ""))
      version = version+1
      commandHandler ! DeleteReservation(AggregateId(-1), AggregateVersion(1))
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(version), ""))
    }
  }
}
