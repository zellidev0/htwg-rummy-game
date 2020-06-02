package game

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.Tile
import model.fileIO.FileIOJson
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContextExecutor

object GameService {
  private val INTERFACE = "0.0.0.0"
  private val PORT = 9001

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val gameController = new GameController
  private val fileIo = new FileIOJson()

  private val bindingFuture = Http().bindAndHandle(pathPrefix("game")(
    path("putTileDown")(putTileDownPath()) ~
      path("moveTile")(moveTilePath()) ~
      path("create")(createPath())),
    INTERFACE,
    PORT)

  println("Running GameService on port: " + PORT)
  def main(args: Array[String]): Unit = {}

  private[game] def moveTilePath(): Route = post(entity(as[String]) { input =>
    println(s"GameService --- moveTile request came in")
    val from = checkCorrectTile(input, "from")
    val to = checkCorrectTile(input, "to")
    val deskOption = fileIo.jsonToDesk(Json.parse(input))
    val result = deskOption match {
      case Some(desk) if from.isDefined && to.isDefined =>
        handleCorrect(gameController.moveTile(desk, from.get, to.get))
      case None => handleWrong()
    }
    complete(result)
  })

  private[game] def createPath(): Route = post(entity(as[String]) { _ =>
    println(s"GameService --- createPath request came in")
    complete(handleCorrect(gameController.createDefaultTable(12)))
  })

  private[game] def putTileDownPath(): Route = post(entity(as[String]) { input =>
    println(s"GameService --- puTileDown request came in")
    val tile = checkCorrectTile(input, "tile")
    val deskOption = fileIo.jsonToDesk(Json.parse(input))
    val result = deskOption match {
      case Some(desk) if tile.isDefined =>
        handleCorrect(gameController.putTileDown(desk, tile.get))
      case None =>
        handleWrong("desk not parsable")
    }
    complete(result)
  })

  private[game] def checkCorrectTile(input: String, what: String) =
    Json.parse(input).\(what).toOption match {
      case Some(value: JsValue) =>
        println("checkCorrectTile-value", value.as[String], value.as[String].length)
        Tile.stringToTile(value.as[String])
      case None => None
    }

  private[game] def handleCorrect(c: DeskInterface) =
    HttpResponse(OK, entity = fileIo.deskToJson(c).toString())

  private[game] def handleWrong(string: String = "") =
    HttpResponse(InternalServerError, entity = string)

  private[game] def kill() =
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
