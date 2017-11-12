package me.server.utils.cqrs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.PersistentActor
import akka.persistence.query.scaladsl._
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source


trait receiverQuery {
  def receiveQuery()
}

abstract class ProjectionActor extends PersistentActor {

  val receiveCommand: Receive = {
    case _ => ()
  }

  val receiveRecover: Receive = {
    case _ =>()
  }
}
