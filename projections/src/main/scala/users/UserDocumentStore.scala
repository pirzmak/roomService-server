package users

import me.server.domain.users_api.User
import me.server.utils.{Aggregate, AggregateId, AggregateVersion, Store}

class UserDocumentStore extends Store[User]{
  var users : List[Aggregate[User]] = Nil

  override def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: User): Unit =
    users = Aggregate(aggregateId,aggregateVersion,aggregate) :: users

  override def getAll: Iterable[User] = users.map(_.aggregate)

  override def getDocumentById(aggregateId: AggregateId): Option[User] = users.find(_.aggregateId == aggregateId).map(_.aggregate)


}
