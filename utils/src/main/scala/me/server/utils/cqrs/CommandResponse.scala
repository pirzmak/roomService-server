package me.server.utils.cqrs

trait CommandResponse

case class CommandSuccess(event: Event) extends CommandResponse

case class CommandFailure(msg: String) extends CommandResponse
