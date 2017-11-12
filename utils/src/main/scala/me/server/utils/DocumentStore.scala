package me.server.utils

import me.server.utils.ddd.{AggregateId, AggregateVersion}

abstract class DocumentStore[T] {
  def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: T)
  def upsertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: T)
  def getDocumentById(aggregateId: AggregateId): Option[Aggregate[T]]
  def getAll: Iterable[Aggregate[T]]
}

class MockDocumentStore[T] extends DocumentStore[T]{
  var documents : List[Aggregate[T]] = Nil

  override def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: T): Unit =
    documents = Aggregate(aggregateId,aggregateVersion,aggregate) :: documents

  override def upsertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, aggregate: T): Unit = {
    if(documents.exists(_.aggregateId.asLong == aggregateId.asLong))
      documents = documents.map(d => if(d.aggregateId == aggregateId) Aggregate(aggregateId,aggregateVersion,aggregate) else d)
    else
      documents = Aggregate(aggregateId,aggregateVersion,aggregate) :: documents
  }

  override def getAll: Iterable[Aggregate[T]] = documents

  override def getDocumentById(aggregateId: AggregateId): Option[Aggregate[T]] = documents.find(_.aggregateId.asLong == aggregateId.asLong)

}

case class Aggregate[T] (aggregateId: AggregateId, version: AggregateVersion, aggregate: T)
