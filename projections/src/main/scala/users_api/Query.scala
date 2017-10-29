package users_api

import me.server.utils.ddd.AggregateId

case class GetUserById(id: AggregateId)

case class GetAllUsers()
