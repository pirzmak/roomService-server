package user


import akka.actor.ActorSystem
import me.server.domain.users_api.UserCreated
import me.server.utils.me.server.utils.cqrs.{MyEvent, ProjectionActor}

import scala.concurrent.ExecutionContext



class UserProjection(projectionId: String, aggregateId: String)(implicit system: ActorSystem,implicit val ec: ExecutionContext)
  extends ProjectionActor(projectionId, aggregateId) {
  override def eventEvaluator(event: MyEvent) = event.event match {
    case e: UserCreated => ()
    case _ => ()
  }
}
