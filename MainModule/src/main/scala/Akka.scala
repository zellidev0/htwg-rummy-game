import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.deskComp.deskBaseImpl.deskImpl.Tile

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

class Akka(var connector: UIConnector.type) extends UIInterface with Observer {
  connector.add(this)

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route =
    concat(
      pathPrefix("player") {
        path("finish") {
          get {
            complete {
              if (connector.contr.state == ControllerState.INSERTING_NAMES) {
                connector.updateController(connector.contr.nameInputFinished())
              }
              HttpResponse(OK, entity = connector.controller.toJson.toString())
            }
          }
        } ~
          path("undo") {
            get {
              complete {
                if (connector.contr.state == ControllerState.INSERTING_NAMES) {
                  connector.updateController(connector.contr.undo())
                }
                HttpResponse(OK, entity = connector.controller.toJson.toString())
              }
            }
          } ~
          path("redo") {
            get {
              complete {
                if (connector.contr.state == ControllerState.INSERTING_NAMES) {
                  connector.updateController(connector.contr.redo())
                }
                HttpResponse(OK, entity = connector.controller.toJson.toString())
              }
            }
          } ~
          path("add") {
            post {
              entity(as[String]) { name =>
                complete {
                  if (connector.contr.state == ControllerState.INSERTING_NAMES) {
                    connector.updateController(connector.contr.addPlayerAndInit(name, 12))
                  }
                  HttpResponse(OK, entity = connector.controller.toJson.toString())
                }
              }
            }
          }
      },
      pathPrefix("next") {
        path("switch") {
          get {
            complete {
              if (connector.contr.state == ControllerState.NEXT_TYPE_N) {
                connector.updateController(connector.contr.switchToNextPlayer())
              }
              HttpResponse(OK, entity = connector.controller.toJson.toString())
            }
          }
        } ~
          path("store") {
            get {
              complete {
                if (connector.contr.state == ControllerState.NEXT_TYPE_N) {
                  connector.updateController(connector.contr.storeFile())
                }
                HttpResponse(OK, entity = connector.controller.toJson.toString())
              }
            }
          }
      },
      pathPrefix("game") {
        path("done") {
          get {
            complete {
              if (connector.contr.state == ControllerState.P_TURN) {
                connector.updateController(connector.contr.userFinishedPlay())
              }
              HttpResponse(OK, entity = connector.controller.toJson.toString())
            }
          }
        } ~
          path("layDown") {
            get {
              entity(as[String]) { tile =>
                complete {
                  if (connector.contr.state == ControllerState.P_TURN) {
                    connector.updateController(connector.contr.layDownTile(Tile.stringToTile(tile.split(" ").apply(1)).get))
                  }
                  HttpResponse(OK, entity = connector.controller.toJson.toString())
                }
              }
            }
          } ~
          path("moveTiles") {
            get {
              entity(as[String]) { tiles =>
                complete {
                  if (connector.contr.state == ControllerState.P_TURN) {
                    connector.updateController(connector.contr.moveTile(Tile.stringToTile(tiles.split(" t ").apply(0).split(" ").apply(1)).get,
                      Tile.stringToTile(tiles.split(" t ").apply(1)).get))
                  }
                  HttpResponse(OK, entity = connector.controller.toJson.toString())
                }
              }
            }
          } ~
          path("undo") {
            get {
              complete {
                if (connector.contr.state == ControllerState.P_TURN) {
                  connector.updateController(connector.contr.undo())
                }
                HttpResponse(OK, entity = connector.controller.toJson.toString())
              }
            }
          } ~
          path("redo") {
            get {
              complete {
                if (connector.contr.state == ControllerState.P_TURN) {
                  connector.updateController(connector.contr.redo())
                }
                HttpResponse(OK, entity = connector.controller.toJson.toString())
              }
            }
          }
      },
      pathPrefix("menu") {
        path("create") {
          get {
            complete {
              if (connector.contr.state == ControllerState.MENU) {
                connector.updateController(connector.contr.createDesk(elements + 1))
              }
              HttpResponse(OK, entity = connector.controller.toJson.toString())
            }
          }
        } ~
          path("load") {
            get {
              complete {
                if (connector.contr.state == ControllerState.MENU) {
                  connector.updateController(connector.contr.loadFile())
                }
                HttpResponse(OK, entity = connector.controller.toJson.toString())
              }
            }
          } ~
          path("wrong") {
            get {
              complete {
                if (connector.contr.state == ControllerState.MENU) {
                  connector.updateController()
                }
                HttpResponse(OK, entity = connector.controller.toJson.toString())
              }
            }
          }
      }
    )

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(route, "localhost", 9000)

  println(s"Server online at http://localhost:9000/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

  override def processInput(input: String): Unit = ???

  override def handleNameInput(name: String): ControllerInterface = ???

  override def handleOnTurnFinished(input: String): ControllerInterface = ???

  override def handleOnTurn(input: String): ControllerInterface = ???

  override def handleMenuInput(input: String): ControllerInterface = ???

  override def updated(controller: ControllerInterface): Unit = {}
}
