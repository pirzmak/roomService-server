package users

import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout

import scala.concurrent.duration._
import me.server.domain.users.UsersAggregateContext
import me.server.domain.users_api.User
import me.server.utils.{Aggregate, AggregateId, AggregateVersion}
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen, Matchers, WordSpecLike}
import users_api.{GetAllUsers, GetUserById}


class UserProjectionSpec extends TestKit(ActorSystem("UserProjectionSpec"))  with ImplicitSender with GivenWhenThen
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  implicit val timeout = Timeout(5 seconds)
  implicit val ec = global

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val documentStore = new UserDocumentStore
  val contextId = "userAggregateContextTest"

  val userContextActor = system.actorOf(Props(new UsersAggregateContext(contextId)), contextId)
  val userStore = new UserDocumentStore()

  "An UserProjection with user in store" must {

    Given("UserProjection has users")
    userStore.insertDocument(AggregateId(0),AggregateVersion(0),User.empty)
    userStore.insertDocument(AggregateId(2),AggregateVersion(0),User.empty)
    val projectionId = "testProjectionTest1"
    val userProjection = system.actorOf(Props(new UserProjection(projectionId, contextId, userStore)), projectionId)

    "send back query result" in {
      When("UserStore getUserById with aggregateId in store")
      userProjection ! GetUserById(AggregateId(0))
      Then("Return some user")
      expectMsg(Some(Aggregate(AggregateId(0),AggregateVersion(0),User.empty)))

      When("UserProjection getUserById with no aggregateId in store")
      userProjection ! GetUserById(AggregateId(1))
      Then("Return None")
      expectMsg(None)

      When("UserProjection getAllUsers")
      userProjection ! GetAllUsers()
      Then("Return List")
      expectMsg(List(Aggregate(AggregateId(2),AggregateVersion(0),User.empty),Aggregate(AggregateId(0),AggregateVersion(0),User.empty)))
    }
  }

  "An UserProjection with no user in store" must {

    Given("UserStore has no user")
    val projectionId = "testProjectionTest2"
    val userProjection = system.actorOf(Props(new UserProjection(projectionId, contextId, userStore)), projectionId)

    "send back query result" in {
      When("UserProjection getUserById with no aggregateId in store")
      userProjection ! GetUserById(AggregateId(1))
      Then("Return None")
      expectMsg(None)
    }
  }
}
