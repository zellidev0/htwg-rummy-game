package rummy.ui.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.deskComp.deskBaseImpl.deskImpl.Tile
import model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import play.api.libs.json.Json
import rummy.controller.impl.Controller
import rummy.controller.{ControllerInterface, ControllerJson}
import rummy.util.{AnswerState, ControllerState}

import scala.collection.immutable.SortedSet
import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

object Wui {
  //dpocker
//  private val INTERFACE = "0.0.0.0"
//  local
  private val INTERFACE = "127.0.0.1"
  private val PORT = 9000

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private[impl] val gameRoute: Route = pathPrefix("api") {
    path("createDesk")(post {
      println(s"received post on createDesk")
      complete(
        handleCorrect(Controller(Desk(
          players = List[PlayerInterface](),
          bagOfTiles = Set[TileInterface](),
          table = Set[SortedSet[TileInterface]]()),
          AnswerState.CREATE_DESK, ControllerState.MENU).createDesk(12)))
    }) ~
      path("nextPlayer")(post(entity(as[String]) { input =>
        println(s"received post on nextPlayer")
        complete(Try {
          ControllerJson.jsonToController(Json.parse(input))
        }.fold(ex => handleWrong("Could not parse body " + ex),
          controller => handleCorrect(controller.switchToNextPlayer())))
      })) ~
      path("nameInputFinished")(post(entity(as[String]) { input =>
        println(s"received post on nameInputFinished")
        complete(Try {
          ControllerJson.jsonToController(Json.parse(input))
        }.fold(ex => handleWrong("Could not parse body " + ex),
          controller => handleCorrect(controller.nameInputFinished())))
      })) ~
      path("addPlayer")(post(entity(as[String]) { input =>
        println(s"received post on addPlayer")
        complete(Try {
          ControllerJson.jsonToController(Json.parse(input))
        }.fold(ex => handleWrong("Could not parse body " + ex),
          controller => {
            val name = unmarshallName(input)
            handleCorrect(controller.addPlayerAndInit(name, 12))
          }))
      })) ~
      path("userFinishedPlay")(post(entity(as[String]) { input =>
        println(s"received post on userFinishedPlay")
        complete(Try {
          ControllerJson.jsonToController(Json.parse(input))
        }.fold(ex => handleWrong("Could not parse body " + ex),
          controller => handleCorrect(controller.userFinishedPlay())))
      })) ~
      path("moveTile")(post(entity(as[String]) { input =>
        println(s"received post on moveTile")
        complete(Try {
          ControllerJson.jsonToController(Json.parse(input))
        }.fold(ex => handleWrong("Could not parse body " + ex),
          controller => {
            val from = unmarshallTile(input, "from").get
            val to = unmarshallTile(input, "to").get
            val name = unmarshallName(input)
            handleCorrect(controller.moveTile(from, to))
          }))
      })) ~
      path("layDownTile")(post(entity(as[String]) { input =>
        println(s"received post on layDownTile")
        complete(Try {
          ControllerJson.jsonToController(Json.parse(input))
        }.fold(ex => handleWrong("Could not parse body " + ex),
          controller => {
            val tile = unmarshallTile(input, "tile").get
            val json = Json.parse(input)
            handleCorrect(controller.layDownTile(tile))
          }))
      }))
  }

  println(s"Running Wui on port: $PORT with interface $INTERFACE")
  private val bindingFuture = Http().bindAndHandle(gameRoute, INTERFACE, PORT)

  //  def main(args: Array[String]): Unit = {}


  private def unmarshallTile(input: String, what: String): Option[TileInterface] =
    Json.parse(input).\(what).toOption match {
      case Some(answer) => Tile.stringToTile(answer.toString())
      case None => None
    }

  private def unmarshallName(input: String) =
    (Json.parse(input) \ "name").as[String]

  private def handleCorrect(c: ControllerInterface) =
    HttpResponse(OK, entity = ControllerJson.controllerToJson(c).toString())

  private def handleWrong(error: String) =
    HttpResponse(InternalServerError, entity = error)

  private def kill() =
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
