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

trait AggregateId {
  def asLong: Long
}

case class VersionId(id: Long)

case class CommandResult(status: StatusResponse, id: AggregateId, version: VersionId, message: String)
