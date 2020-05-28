import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.DeskInterface
import model.deskComp.deskBaseImpl.Desk
import model.fileIO.FileIOJson
import play.api.libs.json._

import scala.concurrent.ExecutionContextExecutor


object PlayerService {
  private val INTERFACE = "localhost"
  private val PORT = 9002

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val playerController = new PlayerController
  private val fileIo = new FileIOJson()

  private val playerRoute: Route = pathPrefix("players") {
    path("switchToNext")(post(entity(as[String]) { input =>
      println("PlayerService - switch to next: " + Json.prettyPrint(Json.parse(input)))
      complete(fileIo.jsonToDesk(Json.parse(input)) match {
        case Some(value) => handleCorrect(playerController.switchToNextPlayer(value))
        case None => handleWrong()
      })
    })) ~
      path("add")(post(entity(as[String]) { input =>
        println("PlayerService - Add request: " + Json.prettyPrint(Json.parse(input)))
        val name = checkCorrectName(input)
        complete(fileIo.jsonToDesk(Json.parse(input)) match {
          case Some(value) if name.isDefined =>
            println("PlayerService - Value:", value)
            handleCorrect(playerController.addPlayerAndInit(value, name.get.toString(), 12))
          case None => handleWrong()
        })
      }))
  }

  private val bindingFuture = Http().bindAndHandle(playerRoute, INTERFACE, PORT)

  def main(args: Array[String]): Unit = {}

  private def checkCorrectName(input: String) =
    Json.parse(input).\("name").toOption

  private def handleCorrect(c: DeskInterface) =
    HttpResponse(OK, entity = fileIo.deskToJson(c).toString())

  private def handleWrong() =
    HttpResponse(InternalServerError)

  private def kill() =
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
