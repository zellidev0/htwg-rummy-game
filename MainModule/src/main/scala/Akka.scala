import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.{ ExecutionContextExecutor, Future }
import scala.io.StdIn

class Akka(var connector: UIConnector.type) extends UIInterface with Observer {
  connector.add(this)

  private implicit val system: ActorSystem             = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(route, "localhost", 9000)

  println(s"Server online at http://localhost:9000/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

  override def processInput(input: String): Unit                        = ???
  override def handleNameInput(name: String): ControllerInterface       = ???
  override def handleOnTurnFinished(input: String): ControllerInterface = ???
  override def handleOnTurn(input: String): ControllerInterface         = ???
  override def handleMenuInput(input: String): ControllerInterface      = ???
  override def updated(controller: ControllerInterface): Unit           = ???
}
