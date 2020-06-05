package player

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import player.database.PlayerDao
import player.database.relational.RelationalDb
// always replace this: import akka.http.scaladsl.server.Directives.{as, complete, entity, path, pathPrefix, post}
// with           this: import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.DeskInterface
import model.fileIO.FileIOJson
import play.api.libs.json.Json

import scala.concurrent.ExecutionContextExecutor

object PlayerService {
  var initAmountOfTiles: Int = 12
  private val INTERFACE = "0.0.0.0"
  private val PORT = 9002
  private val database: PlayerDao = RelationalDb;

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val playerController = new PlayerController
  private val fileIo = new FileIOJson()
  private val bindingFuture = Http().bindAndHandle(
    pathPrefix("players")(
      path("switchToNext")(switchToNextPath())
        ~ path("add")(addPath())
        ~ path("save")(savePath())
        ~ path("load")(loadPath())),
    INTERFACE,
    PORT)

  println("Running PlayerService on port: " + PORT)

  def main(args: Array[String]): Unit = {}


  private[player] def savePath(): Route = post(entity(as[String]) { input =>
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

  private[player] def loadPath(): Route = post({
    println(s"PlayerService --- load request came in")
    complete(database.read() match {
      case Some(player) => HttpResponse(OK, entity = player.toString)
      case None => handleWrong("Could not find player in database")
    })
  })

  private[player] def addPath(): Route = post(entity(as[String]) { input =>
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

  private[player] def switchToNextPath(): Route = post(entity(as[String]) { input =>
    println(s"PlayerService --- switchToNextPath came in")
    val deskOption = fileIo.jsonToDesk(Json.parse(input))
    val response = deskOption match {
      case Some(value) => handleCorrect(playerController.switchToNextPlayer(value))
      case None => handleWrong("Desk has wrong format")
    }
    complete(response)
  })

  private[player] def checkCorrectName(input: String) =
    Json.parse(input).\("name").toOption

  private[player] def handleCorrect(c: DeskInterface) =
    HttpResponse(OK, entity = fileIo.deskToJson(c).toString())

  private[player] def handleWrong(string: String) =
    HttpResponse(InternalServerError, entity = string)

  private def kill(): Unit = bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())

}
