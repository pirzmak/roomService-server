package me.server.projections.users


import akka.actor.ActorSystem
import me.server.domain_api.users_api.User
import me.server.projections_api.users_api.{FindUserById, GetAllUsers}
import me.server.utils.cqrs.ProjectionActor
import me.server.utils.ddd.{Aggregate, AggregateId, OrganizationId}
import me.server.utils.documentStore.DocumentStore

import scala.concurrent.ExecutionContext

class UserProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[User])
                    (implicit system: ActorSystem, implicit val ec: ExecutionContext) extends ProjectionActor {

  def persistenceId = projectionId

  override val receiveCommand: Receive = {
    case m: FindUserById => sender() ! getUserById(m.id)
    case m: GetAllUsers => sender() ! getAllUsers()
    case _ =>()
  }

  def getUserById(userId: AggregateId): Option[Aggregate[User]] = {
    documentStore.getDocumentById(userId, OrganizationId(0))
  }

  def getAllUsers(): List[Aggregate[User]] = {
    documentStore.getAll(OrganizationId(0)).toList
  }
}
