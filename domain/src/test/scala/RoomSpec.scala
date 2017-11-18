
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import akka.dispatch.ExecutionContexts.global
import akka.util.Timeout
import me.server.domain.rooms.RoomsAggregateContext
import me.server.domain_api.rooms_api._
import me.server.utils.MockDocumentStore
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.tests.TestAggregateRepositoryActor
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class RoomSpec() extends TestKit(ActorSystem("RoomSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  implicit val timeout = Timeout(5 seconds)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  val roomAggContext = new RoomsAggregateContext()
  val documentStore = new MockDocumentStore[Room]

  "An Room actor" must {

    "get result with 1 aggregate and 1 version" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Room](AggregateId(-1),List.empty,roomAggContext,documentStore)))

      commandHandler ! CreateRoom(RoomInfo("",""),2,50000)
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(1), ""))
    }
  }


  "An Room actor" must {

    "get result with 1 aggregate and next version" in {
      val listEvents: List[RoomEvent] = List(RoomCreated(RoomInfo("",""),2,50000,false), RoomInfoChanged( "", ""), RoomInfoChanged( "", ""))
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Room](AggregateId(-1),listEvents,roomAggContext,documentStore)))
      var version = 5


      commandHandler ! ChangeRoomInfo(AggregateId(-1), AggregateVersion(version), "", "")
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(version), ""))
    }
  }
}
