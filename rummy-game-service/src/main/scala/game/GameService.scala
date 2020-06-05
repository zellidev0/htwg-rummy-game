package game

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import database.PlayerDao
import database.relational.RelationalDb
import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.Tile
import model.fileIO.FileIOJson
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContextExecutor

object GameService {
  var initAmountOfTiles: Int = 12
  private val INTERFACE = "0.0.0.0"
  private val PORT = 9001
  private val database: PlayerDao = RelationalDb;


  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val gameController = new GameController
  private val playerController = new PlayerController
  private val fileIo = new FileIOJson()

  private val bindingFuture = Http().bindAndHandle(pathPrefix("game")(
    path("putTileDown")(putTileDownPath()) ~
      path("moveTile")(moveTilePath()) ~
      path("create")(createPath())) ~
    pathPrefix("players")(
    path("switchToNext")(switchToNextPath())
      ~ path("add")(addPath())
      ~ path("save")(savePath())
      ~ path("load")(loadPath())) ,
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

  private[game] def savePath(): Route = post(entity(as[String]) { input =>
    println(s"PlayerService --- save request came in")
    val deskOption = fileIo.jsonToDesk(Json.parse(input))
    val response = deskOption match {
      case Some(value) =>
        val result = database.create(value)
        result match {
          case Some(desk) => handleCorrect(desk)
          case None => handleWrong("Could not save desk in database")
        }
      case None =>
        handleWrong("Desk are not correct")
    }
    complete(response)
  })

  private[game] def loadPath(): Route = post({
    println(s"PlayerService --- load request came in")
    complete(database.read() match {
      case Some(player) => HttpResponse(OK, entity = player.toString)
      case None => handleWrong("Could not find player in database")
    })
  })

  private[game] def addPath(): Route = post(entity(as[String]) { input =>
    println(s"PlayerService --- add request came in")
    val name = checkCorrectName(input)
    val deskOption = fileIo.jsonToDesk(Json.parse(input))
    val response = deskOption match {
      case Some(value) if name.isDefined =>
        handleCorrect(playerController.addPlayerAndInit(value, name.get.toString(), initAmountOfTiles))
      case None =>
        handleWrong("Name or desk are not correct")
    }
    complete(response)
  })

  private[game] def switchToNextPath(): Route = post(entity(as[String]) { input =>
    println(s"PlayerService --- switchToNextPath came in")
    val deskOption = fileIo.jsonToDesk(Json.parse(input))
    val response = deskOption match {
      case Some(value) => handleCorrect(playerController.switchToNextPlayer(value))
      case None => handleWrong("Desk has wrong format")
    }
    complete(response)
  })



  private[game] def checkCorrectName(input: String) =
    Json.parse(input).\("name").toOption



  private[game] def handleCorrect(c: DeskInterface) =
    HttpResponse(OK, entity = fileIo.deskToJson(c).toString())

  private[game] def handleWrong(string: String = "") =
    HttpResponse(InternalServerError, entity = string)

  private[game] def kill() =
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
