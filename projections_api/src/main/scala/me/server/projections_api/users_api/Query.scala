package me.server.projections_api.users_api

import me.server.utils.ddd.AggregateId

case class FindUserById(id: AggregateId)

case class GetAllUsers()
