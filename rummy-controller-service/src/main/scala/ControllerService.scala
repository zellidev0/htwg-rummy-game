import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.deskComp.deskBaseImpl.deskImpl.Tile
import model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import model.fileIO.FileIOJson
import play.api.libs.json._

import scala.collection.immutable.SortedSet
import scala.concurrent.ExecutionContextExecutor


object ControllerService {
  private val INTERFACE = "localhost"
  private val PORT = 9001

  private implicit val system: ActorSystem = ActorSystem("my-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val fileIo = new FileIOJson()
  private val gameRoute: Route = pathPrefix("api") {
    path("switchToNextPlayer")(post(entity(as[String]) { input =>
      val answer = checkCorrectAnswer(input)
      val state = checkCorrectState(input)
      complete(checkCorrectDesk(input) match {
        case Some(desk) if answer.isDefined && state.isDefined =>
          handleCorrect(Controller(desk, answer.get, state.get).switchToNextPlayer())
        case None => handleWrong()
      })
    })) ~
      path("nameInputFinished")(post(entity(as[String]) { input =>
        val answer = checkCorrectAnswer(input)
        val state = checkCorrectState(input)
        complete(checkCorrectDesk(input) match {
          case Some(desk) if answer.isDefined && state.isDefined =>
            handleCorrect(Controller(desk, answer.get, state.get).nameInputFinished())
          case None => handleWrong()
        })
      })) ~
      path("addPlayer")(post(entity(as[String]) { input =>
        val answer = checkCorrectAnswer(input)
        val state = checkCorrectState(input)
        val name = checkCorrectName(input)
        complete(checkCorrectDesk(input) match {
          case Some(desk) if answer.isDefined && state.isDefined && name.isDefined =>
            handleCorrect(Controller(desk, answer.get, state.get).addPlayerAndInit(name.get, 12))
          case None => handleWrong()
        })
      })) ~
      path("userFinishedPlay")(post(entity(as[String]) { input =>
        val answer = checkCorrectAnswer(input)
        val state = checkCorrectState(input)
        complete(checkCorrectDesk(input) match {
          case Some(desk) if answer.isDefined && state.isDefined =>
            handleCorrect(Controller(desk, answer.get, state.get).userFinishedPlay())
          case None => handleWrong()
        })
      })) ~
      path("createDesk")(post(entity(as[String]) { input =>
        complete(
          handleCorrect(Controller(
            Desk(players = List[PlayerInterface](), bagOfTiles = Set[TileInterface](), table = Set[SortedSet[TileInterface]]()),
            AnswerState.CREATE_DESK, ControllerState.MENU).userFinishedPlay()))
      })) ~
      path("moveTile")(post(entity(as[String]) { input =>
        val answer = checkCorrectAnswer(input)
        val state = checkCorrectState(input)
        val from = checkCorrectTile(input, "from")
        val to = checkCorrectTile(input, "to")
        complete(checkCorrectDesk(input) match {
          case Some(desk) if answer.isDefined && state.isDefined && from.isDefined && to.isDefined=>
            handleCorrect(Controller(desk, answer.get, state.get).moveTile(from.get,to.get))
          case None => handleWrong()
        })
      })) ~
      path("layDownTile")(post(entity(as[String]) { input =>
        val answer = checkCorrectAnswer(input)
        val state = checkCorrectState(input)
        val tile = checkCorrectTile(input, "tile")
        complete(checkCorrectDesk(input) match {
          case Some(desk) if answer.isDefined && state.isDefined && tile.isDefined=>
            handleCorrect(Controller(desk, answer.get, state.get).layDownTile(tile.get))
          case None => handleWrong()
        })
      }))
  }

  private val bindingFuture = Http().bindAndHandle(gameRoute, INTERFACE, PORT)

  def main(args: Array[String]): Unit = {}

  private def checkCorrectDesk(input: String) =
    Json.parse(input).\("desk").toOption match {
      case Some(value) => fileIo.jsonToDesk(value)
      case None => None
    }

  private def checkCorrectAnswer(input: String): Option[AnswerState.Value] =
    Json.parse(input).\("answer").toOption match {
      case Some(answer) => Some(AnswerState.from(answer.toString()))
      case None => None
    }

  private def checkCorrectTile(input: String, what:String): Option[TileInterface] =
    Json.parse(input).\(what).toOption match {
      case Some(answer) => Tile.stringToTile(answer.toString())
      case None => None
    }

  private def checkCorrectState(input: String): Option[ControllerState.Value] =
    Json.parse(input).\("state").toOption match {
      case Some(state) => Some(ControllerState.from(state.toString()))
      case None => None
    }

  private def checkCorrectName(input: String) =
    Json.parse(input).\("name").toOption

  private def handleCorrect(c: ControllerInterface) =
    HttpResponse(OK, entity = controller.toJson.toString())

  private def handleWrong() =
    HttpResponse(InternalServerError, entity = controller.toJson.toString())

  private def kill() =
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
