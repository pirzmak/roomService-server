import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import me.server.domain.users.UsersAggregateContext
import me.server.domain.users_api.{CreateUser, UpdateUser, User}
import me.server.utils.{DocumentStore, MockDocumentStore}
import me.server.utils.ddd.{AggregateId, AggregateManager, AggregateVersion}
import me.server.utils.cqrs.{CommandResult, StatusResponse}

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
  val commandHandler = system.actorOf(Props(new AggregateManager[User]("UserMenager",userAggContext,documentStore)),"test")

  When("Actor user added msg")
  "An User actor" must {

    "get result with 1 aggregate and 1 version" in {
      commandHandler ! CreateUser("mail","pas", "adam", "haslo")
      expectMsg(CommandResult(StatusResponse.success, AggregateId(1), AggregateVersion(1), ""))
    }
  }

  When("Actor user updated msg")
  "An User actor" must {

    "get result with 1 aggregate and 2 version" in {
      val documentStore = new MockDocumentStore[User]
      documentStore.insertDocument(AggregateId(1), AggregateVersion(0), User.empty)
      commandHandler ! UpdateUser(AggregateId(1), AggregateVersion(0),None ,None , None, Some("a"))
      expectMsg(CommandResult(StatusResponse.success, AggregateId(1), AggregateVersion(2), ""))
    }
  }

//  When("Actor user deleted msg")
//  "An User actor" must {
//
//    "get result" in {
//      val echo = system.actorOf(Props(new UsersAggregateContext("userAggregateContext")), "userAggregateContext")
//      echo ! CreateUser("mail","pas", "adam", "haslo")
//      expectMsg(CommandResult(StatusResponse.success, AggregateId(0), AggregateVersion(0), ""))
//    }
//  }


}
