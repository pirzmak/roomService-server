package me.server.utils.cqrs

case class StatusResponse(name: String)

object StatusResponse {
  val success = StatusResponse("SUCCESS")
  val failure = StatusResponse("FAILURE")

  def byName(name: String): StatusResponse = name match {
    case "SUCCESS" => success
    case "FAILURE" => failure
  }
}