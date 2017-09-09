object Main extends App{

  import akka.dispatch.ExecutionContexts._

  implicit val ec = global

  override def main(args: Array[String]) {
    val context = new Context()
    context.frontend.start()
  }
}