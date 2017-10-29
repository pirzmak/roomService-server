import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import me.server.domain.users.UsersAggregateContext
import me.server.domain.users_api.{CreateUser, DeleteUser, UpdateUser, User}
import me.server.utils.{DocumentStore, MockDocumentStore}
import me.server.utils.ddd.{AggregateId, AggregateManager, AggregateVersion}
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.tests.TestAggregateRepositoryActor

import scala.concurrent.duration._
import org.scalatest._


class UsersSpec() extends TestKit(ActorSystem("UsersSpec")) with ImplicitSender with GivenWhenThen
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  implicit val timeout = Timeout(5 seconds)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  val userAggContext = new UsersAggregateContext()
  val documentStore = new MockDocumentStore[User]

  When("Actor user added msg")
  "An User actor" must {

    "get result with 1 aggregate and 1 version" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[User](AggregateId(-1),userAggContext,documentStore)))

      commandHandler ! CreateUser("mail","pas", "adam", "haslo")
      expectMsgPF() {
        case CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(1), "") => ()
      }
    }
  }

  When("Actor user updated msg")
  "An User actor" must {

    "get result with 1 aggregate and 2 version" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[User](AggregateId(-1),userAggContext,documentStore)))

      commandHandler ! CreateUser("mail","pas", "adam", "haslo")
      commandHandler ! UpdateUser(AggregateId(-1), AggregateVersion(1),None ,None , None, Some("a"))
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(2), ""))
    }
  }

  When("Actor user deleted msg")
  "An User actor" must {

    "get result" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[User](AggregateId(-1),userAggContext,documentStore)))

      commandHandler ! CreateUser("mail","pas", "adam", "haslo")
      commandHandler ! DeleteUser(AggregateId(-1), AggregateVersion(1))
      expectMsgPF() {
        case CommandResult(StatusResponse.success, _, _, "") => ()
      }
    }
  }


}
