package me.server.utils

abstract class Store[T] {
  def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: T)
  def getDocumentById(aggregateId: AggregateId): Option[T]
  def getAll: Iterable[T]
}

case class Aggregate[T] (aggregateId: AggregateId, version: AggregateVersion, aggregate: T)
