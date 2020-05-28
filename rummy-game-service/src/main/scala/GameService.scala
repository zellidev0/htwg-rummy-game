import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.Tile
import model.fileIO.FileIOJson
import play.api.libs.json._

import scala.concurrent.ExecutionContextExecutor


object GameService {
  private val INTERFACE = "localhost"
  private val PORT = 9001

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val gameController = new GameController
  private val fileIo = new FileIOJson()
  private val gameRoute: Route = pathPrefix("game") {
    path("putTileDown")(post(entity(as[String]) { input =>
      println("GameService - putTileDown: " + Json.prettyPrint(Json.parse(input)))
      val tile = checkCorrectTile(input, "tile")
      println("GameService - tile: " + tile)
      complete(
        if (tile.isEmpty) {
          handleWrong("tile not parsable")
        } else {
          fileIo.jsonToDesk(Json.parse(input)) match {
            case Some(desk) =>
              println("GameService - desk: " + desk)
              handleCorrect(gameController.putTileDown(desk, tile.get))
            case None =>
              handleWrong("desk not parsable")
          }
        })
    })) ~
      path("moveTile")(post(entity(as[String]) { input =>
        println("GameService - putTileDown: " + Json.prettyPrint(Json.parse(input)))
        val from = checkCorrectTile(input, "from")
        val to = checkCorrectTile(input, "to")
        complete(fileIo.jsonToDesk(Json.parse(input)) match {
          case Some(desk) if from.isDefined && to.isDefined =>
            handleCorrect(gameController.moveTile(desk, from.get, to.get))
          case None => handleWrong()
        })
      })) ~
      path("create")(post(entity(as[String]) { _ =>
        println("GameService - create: ")
        complete(handleCorrect(gameController.createDefaultTable(12)))
      }))
  }

  private val bindingFuture = Http().bindAndHandle(gameRoute, INTERFACE, PORT)

  def main(args: Array[String]): Unit = {}

  private def checkCorrectTile(input: String, what: String) =
    Json.parse(input).\(what).toOption match {
      case Some(value: JsValue) =>
        println("checkCorrectTile-value", value.as[String], value.as[String].length)
        Tile.stringToTile(value.as[String])
      case None => None
    }

  private def handleCorrect(c: DeskInterface) =
    HttpResponse(OK, entity = fileIo.deskToJson(c).toString())

  private def handleWrong(string: String = "") =
    HttpResponse(InternalServerError, entity = string)

  private def kill() =
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
