package player

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
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
  private val INTERFACE = "localhost"
  private val PORT = 9002

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val playerController = new PlayerController
  private val fileIo = new FileIOJson()
  private val bindingFuture = Http().bindAndHandle(
    pathPrefix("players")(path("switchToNext")(switchToNextPath()) ~ path("add")(addPath())),
    INTERFACE,
    PORT)

  println("Running PlayerService on port: " + PORT)

  def main(args: Array[String]): Unit = {}

  private[player] def addPath(): Route = post(entity(as[String]) { input =>
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
