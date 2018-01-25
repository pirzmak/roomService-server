package me.server.utils.cqrs

import akka.persistence.journal._

class MyEventAdapter extends EventAdapter  {
  //TODO tags for each type
  override def toJournal(event: Any): Any = event match {
    case MyEvent(e,a,o) =>
      println("k")
      Tagged(MyEvent(e,a,o), Set("myEvent"))
    case _ => event
  }
  override def fromJournal(event: Any, manifest: String): EventSeq = event match {
    case MyEvent(_,_,_) ⇒
      println("in")
      EventSeq.single(MyEvent)
  }
  override def manifest(event: Any): String = ""
}
