package me.server.utils.cqrs

case class CommandException(msg: String) extends Exception(msg)

object CommandException {
  val unknownCommand = CommandException("Unknown Command")
  val unknownResponse = CommandException("Unknown Command response")
  val unknownEvent = CommandException("Unknown Event")
}