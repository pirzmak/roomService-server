package me.server.utils


case class StatusResponse(name: String)
object StatusResponse {
  val success = StatusResponse("SUCCESS")
  val failure = StatusResponse("FAILURE")

  def byName(name: String): StatusResponse = name match {
    case "SUCCESS" => success
    case "FAILURE" => failure
  }
}

case class AggregateId(id: Long) {
  def asLong: Long = id
}

case class AggregateVersion(id: Long)

case class CommandResult(status: StatusResponse, id: AggregateId, version: AggregateVersion, message: String)
