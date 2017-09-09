package users


import akka.actor.ActorSystem
import me.server.domain.users_api.{User, UserCreated}
import me.server.utils.Store
import me.server.utils.me.server.utils.cqrs.{MyEvent, ProjectionActor}

import scala.concurrent.ExecutionContext

class UserProjection(projectionId: String, aggregateId: String, documentStore: Store[User])(implicit system: ActorSystem,implicit val ec: ExecutionContext)
  extends ProjectionActor(projectionId, aggregateId) {

  override def eventEvaluator(event: MyEvent) = event.event match {
    case e: UserCreated => ()
    case _ => ()
  }

  override val receiveCommand: Receive = {
    case _ =>()
  }
}
