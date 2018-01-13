package me.server.utils.cqrs

import me.server.utils.ddd.{AggregateId, AggregateVersion}

case class CommandResult(status: StatusResponse, id: AggregateId, version: AggregateVersion, message: String)
