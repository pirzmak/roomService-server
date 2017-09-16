import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import me.server.domain.users.UsersAggregateContext
import me.server.domain.users_api.{CreateUser, User}
import me.server.utils.ddd.{AggregateId, AggregateManager, AggregateVersion}
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import org.scalatest._


class UsersSpec() extends TestKit(ActorSystem("UsersSpec")) with ImplicitSender with GivenWhenThen
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  When("Actor user added msg")
  "An User actor" must {

    "get result" in {
      val userAggContext = new UsersAggregateContext()
      val commandHandler = system.actorOf(Props(new AggregateManager[User]("UserMenager",userAggContext)))
      commandHandler ! CreateUser("mail","pas", "adam", "haslo")
      expectMsg(CommandResult(StatusResponse.success, AggregateId(0), AggregateVersion(0), ""))
    }
  }
//
//  When("Actor user updated msg")
//  "An User actor" must {
//
//    "get result" in {
//      val echo = system.actorOf(Props(new UsersAggregateContext("userAggregateContext")), "userAggregateContext")
//      echo ! Upda("mail","pas", "adam", "haslo")
//      expectMsg(CommandResult(StatusResponse.success, AggregateId(0), AggregateVersion(0), ""))
//    }
//  }
//
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
