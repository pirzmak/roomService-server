package me.server.utils.ddd

case class AggregateId(id: Long) {
  def asLong: Long = id
}

case class AggregateVersion(version: Long){
  def next: AggregateVersion = AggregateVersion(this.version + 1)
}
