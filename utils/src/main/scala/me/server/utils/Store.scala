package me.server.utils

import me.server.utils.ddd.{AggregateId, AggregateVersion}

abstract class Store[T] {
  def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: T)
  def getDocumentById(aggregateId: AggregateId): Option[Aggregate[T]]
  def getAll: Iterable[Aggregate[T]]
}

case class Aggregate[T] (aggregateId: AggregateId, version: AggregateVersion, aggregate: T)
