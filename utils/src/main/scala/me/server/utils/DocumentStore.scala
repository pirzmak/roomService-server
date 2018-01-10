package me.server.utils

import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}

abstract class DocumentStore[T] {
  def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, organizationId: OrganizationId, aggregate: T)
  def upsertDocument(aggregateId: AggregateId, organizationId: OrganizationId, aggregate: T)
  def getDocumentById(aggregateId: AggregateId, organizationId: OrganizationId): Option[Aggregate[T]]
  def getAll(organizationId: OrganizationId): Iterable[Aggregate[T]]
}

class MockDocumentStore[T] extends DocumentStore[T]{
  var documents : List[Aggregate[T]] = Nil

  override def insertDocument(aggregateId: AggregateId, aggregateVersion: AggregateVersion, organizationId: OrganizationId, aggregate: T): Unit =
    documents = Aggregate(aggregateId,aggregateVersion,organizationId,aggregate) :: documents

  override def upsertDocument(aggregateId: AggregateId, organizationId: OrganizationId, aggregate: T): Unit = {
    if(documents.exists(d => d.aggregateId.asLong == aggregateId.asLong && d.organizationId.asLong == organizationId.asLong))
      documents = documents.map(d => if(d.aggregateId == aggregateId && d.organizationId == organizationId)
        Aggregate(aggregateId,d.version.next,organizationId,aggregate) else d)
    else
      documents = Aggregate(aggregateId,AggregateVersion(1),organizationId,aggregate) :: documents
  }

  override def getAll(organizationId: OrganizationId): Iterable[Aggregate[T]] = documents.filter(_.organizationId == organizationId)

  override def getDocumentById(aggregateId: AggregateId, organizationId: OrganizationId): Option[Aggregate[T]] =
    documents.find(d => d.aggregateId.asLong == aggregateId.asLong && d.organizationId == organizationId)

}

case class Aggregate[T] (aggregateId: AggregateId, version: AggregateVersion, organizationId: OrganizationId, aggregate: T)
