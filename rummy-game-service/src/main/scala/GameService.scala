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
      val tile = checkCorrectTile(input, "tile")
      complete(checkCorrectDesk(input) match {
        case Some(desk) => handleCorrect(gameController.putTileDown(desk, tile.get))
        case None => handleWrong()
      })
    })) ~
      path("moveTile")(post(entity(as[String]) { input =>
        val from = checkCorrectTile(input, "from")
        val to = checkCorrectTile(input,"to")
        complete(checkCorrectDesk(input) match {
          case Some(desk) if from.isDefined && to.isDefined =>
              handleCorrect(gameController.moveTile(desk, from.get, to.get))
          case None => handleWrong()
        })
      })) ~
      path("create")(post(entity(as[String]) { _ =>
        complete(handleCorrect(gameController.createDefaultTable(12)))
      }))
  }

  private val bindingFuture = Http().bindAndHandle(gameRoute, INTERFACE, PORT)

  def main(args: Array[String]): Unit = {}

  private def checkCorrectDesk(input: String) =
    Json.parse(input).\("desk").toOption match {
      case Some(value) => fileIo.jsonToDesk(value)
      case None => None
    }

  private def checkCorrectTile(input: String, what:String) =
    Json.parse(input).\(what).toOption match {
      case Some(value) => Tile.stringToTile(value.toString())
      case None => None
    }

  private def handleCorrect(c: DeskInterface) =
    HttpResponse(OK, entity = fileIo.deskToJson(c).toString())

  private def handleWrong() =
    HttpResponse(InternalServerError)

  private def kill() =
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
