package users_api

import me.server.utils.AggregateId

case class GetUserById(id: AggregateId)

case class GetAllUsers()
