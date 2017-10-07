package users


import akka.actor.ActorSystem
import me.server.domain.users_api.{User, UserCreated}
import me.server.utils.cqrs.{MyEvent, ProjectionActor}
import me.server.utils.ddd.{AggregateId, AggregateVersion}
import me.server.utils.{Aggregate, DocumentStore}
import users_api.{GetAllUsers, GetUserById}

import scala.concurrent.ExecutionContext

class UserProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[User])(implicit system: ActorSystem, implicit val ec: ExecutionContext) {
//  extends ProjectionActor(projectionId, aggregateId) {
//
//  override def eventEvaluator(event: MyEvent) = event.event match {
//    case e: UserCreated => userCreated(event.aggregateId,e)
//    case _ => ()
//  }
//
//  def userCreated(id: AggregateId, created: UserCreated): Unit = {
//    documentStore.insertDocument(id, AggregateVersion(0), User(created.email, created.password,
//      created.firstName,created.lastName,false))
//  }
//
//  override val receiveCommand: Receive = {
//    case m: GetUserById => sender() ! getUserById(m.id)
//    case m: GetAllUsers => sender() ! getAllUsers()
//    case _ =>()
//  }
//
//  def getUserById(userId: AggregateId): Option[Aggregate[User]] = {
//    documentStore.getDocumentById(userId)
//  }
//
//  def getAllUsers(): List[Aggregate[User]] = {
//    documentStore.getAll.toList
//  }
}
