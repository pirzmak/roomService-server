import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import me.server.domain.users.{Storage, UsersAggregateContext}
import me.server.domain.users_api.{CreateUser, User, UserId}
import me.server.utils.{AggregateId, CommandResult, StatusResponse, VersionId}
import org.scalatest._


class UsersSpec() extends TestKit(ActorSystem("UsersSpec")) with ImplicitSender with GivenWhenThen
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  Given("Empty userStorage")

  class UserStorageMock extends Storage[User] {
    var users: List[User] = Nil

    def add(id: AggregateId, document: User): Unit = {
      users = document :: users
    }
  }

  val mock = new UserStorageMock

  When("Actor user added msg")
  "An User actor" must {

    "send back messages unchanged" in {
      val echo = system.actorOf(Props(new UsersAggregateContext()), "userAggregateContext")
      echo ! CreateUser(UserId(2), "adam", "haslo")
      expectMsg(CommandResult(StatusResponse.success, UserId(2), VersionId(1), ""))
      Then("User added")
      assert(mock.users.nonEmpty)
      assert(mock.users.exists(_.username == "adam"))
    }
  }


}
