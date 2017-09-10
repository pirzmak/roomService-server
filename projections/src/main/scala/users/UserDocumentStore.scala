package users

import me.server.domain.users_api.User
import me.server.utils.{Aggregate, AggregateId, AggregateVersion, Store}

class UserDocumentStore extends Store[User]{
  var users : List[Aggregate[User]] = Nil

  override def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: User): Unit =
    users = Aggregate(aggregateId,aggregateVersion,aggregate) :: users

  override def getAll: Iterable[Aggregate[User]] = users

  override def getDocumentById(aggregateId: AggregateId): Option[Aggregate[User]] = users.find(_.aggregateId.asLong == aggregateId.asLong)


}
