import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import me.server.domain.users.{UsersAggregateContext}
import me.server.domain.users_api.{CreateUser, User, UserId}
import me.server.utils.{AggregateId, CommandResult, StatusResponse, AggregateVersion}
import org.scalatest._


class UsersSpec() extends TestKit(ActorSystem("UsersSpec")) with ImplicitSender with GivenWhenThen
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  When("Actor user added msg")
  "An User actor" must {

    "send back messages unchanged" in {
      val echo = system.actorOf(Props(new UsersAggregateContext()), "userAggregateContext")
      echo ! CreateUser("mail","pas", "adam", "haslo")
      expectMsg(CommandResult(StatusResponse.success, AggregateId(0), AggregateVersion(0), ""))
    }
  }


}
