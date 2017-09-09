package me.server.utils

sealed abstract class Store[T] {
  def getDocumentById(aggregateId: AggregateId):T
  def getAll: Iterable[T]
}
