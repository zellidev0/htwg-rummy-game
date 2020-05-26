import ControllerState.{ INSERTING_NAMES, MENU, NEXT_TYPE_N, P_TURN }
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.deskComp.deskBaseImpl.deskImpl.Tile

import scala.concurrent.{ ExecutionContextExecutor, Future }
import scala.io.StdIn

class Akka(var connector: UIConnector.type) extends UIInterface with Observer {
  connector.add(this)

  private implicit val system: ActorSystem                        = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer            = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val playerRoute: Route = pathPrefix("players") {
    path("finish")(get(complete(handle(connector.contr.nameInputFinished(), INSERTING_NAMES)))) ~
    path("undo")(get(complete(handle(connector.contr.undo(), INSERTING_NAMES)))) ~
    path("redo")(get(complete(handle(connector.contr.redo(), INSERTING_NAMES)))) ~
    path("add")(get(parameter('name) { name =>
      println(name)
      complete(handle(connector.contr.addPlayerAndInit(name, 12), INSERTING_NAMES))
    }))
  }
  val nextRoute: Route = pathPrefix("next") {
    path("switch")(get(complete(handle(connector.contr.switchToNextPlayer(), NEXT_TYPE_N)))) ~
    path("store")(get(complete(handle(connector.contr.storeFile(), NEXT_TYPE_N))))
  }
  val gameRoute: Route = pathPrefix("game") {
    path("done")(get(complete(handle(connector.contr.userFinishedPlay(), P_TURN)))) ~
    path("layDown")(get(parameter('tile) { t =>
      val tile = Tile.stringToTile(t)
      complete(if (tile.isDefined) {
        handle(connector.contr.layDownTile(tile.get), P_TURN)
      } else {
        HttpResponse(BadRequest, entity = s"tile has not valid format")
      })
    })) ~
    path("undo")(get(complete(handle(connector.contr.undo(), P_TURN)))) ~
    path("redo")(get(complete(handle(connector.contr.redo(), P_TURN)))) ~
    path("moveTile")(get(parameter('from, 'to) { (from, to) =>
      val tile1 = Tile.stringToTile(from)
      val tile2 = Tile.stringToTile(to)
      complete(if (tile1.isDefined && tile2.isDefined) {
        handle(connector.contr.moveTile(tile1.get, tile2.get), P_TURN)
      } else {
        HttpResponse(BadRequest, entity = s"tile has not valid format")
      })
    }))
  }
  val menuRoute: Route = pathPrefix("menu") {
    path("create")(get(complete(handle(connector.contr.createDesk(elements + 1), MENU)))) ~
    path("load")(get(complete(handle(connector.contr.loadFile(), MENU))))
  }
  val state: Route = pathPrefix("state") {
    complete(HttpResponse(OK, entity = connector.controller.toJson.toString()))
  }

  val route: Route = concat(playerRoute, nextRoute, gameRoute, menuRoute, state)

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(route, "localhost", 9000)

//  StdIn.readLine() // let it run until user presses return
//  bindingFuture
//    .flatMap(_.unbind()) // trigger unbinding from the port
//    .onComplete(_ => system.terminate()) // and shutdown when done

  override def processInput(input: String): Unit              = {}
  override def updated(controller: ControllerInterface): Unit = {}

  private def handle(controller: ControllerInterface, state: ControllerState.Value): HttpResponse =
    if (state == connector.contr.state) {
      connector.updateController(controller)
      HttpResponse(OK, entity = connector.controller.toJson.toString())
    } else {
      HttpResponse(InternalServerError, entity = s"Wrong server state. Use endpoint only when in ${state} state")
    }
}
