package rummy.controller.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import model.DeskInterface
import model.deskComp.deskBaseImpl.TileInterface
import model.fileIO.FileIOJson
import play.api.libs.json.{JsObject, Json}
import rummy.controller.ControllerInterface
import rummy.util.AnswerState._
import rummy.util.ControllerState.{apply => _, _}
import rummy.util.{AnswerState, ControllerState, UndoManager}

import scala.collection.immutable.SortedSet
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success, Try}

case class Controller(desk: DeskInterface,
                      answer: AnswerState.Value,
                      state: ControllerState.Value,
                      undoMgr: UndoManager = UndoManager())
  extends ControllerInterface {

  type TileI = TileInterface
  type DeskI = DeskInterface
  type ControllerI = ControllerInterface

  //docker
//  private val URL_PLAYER = "http://game:9001/players/"
//  private val URL_GAME = "http://game:9001/game/"

  //local
  private val URL_PLAYER = "http://localhost:9001/players/"
  private val URL_GAME = "http://localhost:9001/game/"

  private val fileIO = new FileIOJson()
  private implicit val system: ActorSystem = ActorSystem()
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  override def switchToNextPlayer(): ControllerI = {
    sendRequest(
      formerDesk = fileIO.deskToJson(desk),
      url = URL_PLAYER + "switchToNext",
      answerState = SWITCHED_TO_NEXT,
      answerStateWrong = COULD_NOT_PARSE,
      controllerState = P_TURN, ControllerState.INSERTING_NAMES)
  }

  override def nameInputFinished(): ControllerI =
    if (desk.correctAmountOfPlayers) copy(desk, answer = INSERTING_NAMES_FINISHED, state = P_TURN)
    else copy(desk, answer = NOT_ENOUGH_PLAYERS, state = INSERTING_NAMES)

  override def addPlayerAndInit(name: String, amountOfTiles: Int): ControllerI =
    if (desk.lessThan4P) {
      sendRequest(
        formerDesk = fileIO.deskToJson(desk).+("name", Json.toJson(name)),
        url = URL_PLAYER + "add",
        answerState = ADDED_PLAYER,
        answerStateWrong = COULD_NOT_PARSE,
        controllerState = ControllerState.INSERTING_NAMES, ControllerState.INSERTING_NAMES)
      //todo: add (undoMgr = undoMgr.putOnStack(desk)) on return
    }
    else copy(desk, answer = ENOUGH_PLAYER, state = INSERTING_NAMES)

  // todo add tiles put down
  override def userFinishedPlay(): ControllerI =
    if (desk.checkTable() && desk.currentPlayerWon()) copy(desk, answer = P_WON, state = KILL)
    else if (desk.bagOfTiles.isEmpty) copy(desk, answer = BAG_IS_EMPTY, state = P_TURN)
    //else if (desk.checkTable() && desk.currentPlayerWon()) copy(desk, answer = P_WON, state = KILL)
    else
      copy(desk = desk.takeTileFromBagToPlayer(desk.getCurrentPlayer, desk.getTileFromBag.get),
        answer = TOOK_TILE,
        state = NEXT_TYPE_N,
        undoMgr.putOnStack(desk))

  // Game Service

  override def createDesk(amount: Int): ControllerI = sendRequest(
    formerDesk = fileIO.deskToJson(desk),
    url = URL_GAME + "create",
    answerState = CREATED_DESK,
    answerStateWrong = COULD_NOT_PARSE,
    controllerState = INSERTING_NAMES, MENU)


  override def moveTile(tile: TileI, to: TileI): ControllerI = {
    sendRequest(
      formerDesk = fileIO.deskToJson(desk)
        .+("from", Json.toJson(tile.toString))
        .+("to", Json.toJson(to.toString)),
      url = URL_GAME + "moveTile",
      answerState = MOVED_TILE,
      answerStateWrong = COULD_NOT_PARSE,
      controllerState = ControllerState.P_TURN, ControllerState.P_TURN)
    //todo undoMgr = undoMgr.putOnStack(desk)
  }

  override def layDownTile(tile: TileI): ControllerI = {
    sendRequest(
      formerDesk = fileIO.deskToJson(desk).+("tile", Json.toJson(tile.toString)),
      url = URL_GAME + "putTileDown",
      answerState = PUT_TILE_DOWN,
      answerStateWrong = COULD_NOT_PARSE,
      controllerState = ControllerState.P_TURN, ControllerState.P_TURN)
    //todo  undoMgr = undoMgr.putOnStack(desk)
  }

  override def currentPlayerName: String =
    desk.getCurrentPlayer.name

  // Views
  override def viewOfTable: Set[SortedSet[TileI]] =
    desk.tableView

  override def viewOfBoard: SortedSet[TileI] =
    desk.boardView

  //UndoManager

  override def undo(): ControllerI = {
    val tuple = undoMgr.undoStep()
    copy(desk = tuple._2.getOrElse(desk), answer = UNDO, state, undoMgr = tuple._1)
  }

  override def redo(): ControllerI = {
    val tuple = undoMgr.redoStep()
    copy(desk = tuple._2.getOrElse(desk), answer = REDO, state, undoMgr = tuple._1)
  }

  // FileIo

  override def storeFile(): ControllerI = {
    fileIO.save(desk)
    copy()
  }

  override def loadFile(): ControllerI = fileIO.load match {
    case Some(desk) => copy(desk, answer = LOADED_FILE, state = P_TURN)
    case None => createDesk(12)
  }

  override def toJson: JsObject = Json.obj("state" -> state, "answer" -> answer, "desk" -> fileIO.toJson(desk))

  private def sendRequest(formerDesk: JsObject, url: String,
                          answerState: AnswerState.Value,
                          answerStateWrong: AnswerState.Value,
                          controllerState: ControllerState.Value,
                          controllerStateWrong: ControllerState.Value): ControllerInterface = {

    val request = HttpRequest(POST, uri = url, entity = formerDesk.toString())
    val response: HttpResponse =
      Await.result(Http().singleRequest(request), Duration.fromNanos(20000000000L))
    val result: String =
      Await.result(Unmarshal(response.entity).to[String], Duration.fromNanos(20000000000L))

    println("Parsed ------", Json.parse(result))

    Try(Json.parse(result)) match {
      case Failure(exception) =>
        println(exception);
        copy(desk = desk, answerStateWrong, controllerStateWrong);
      case Success(value) => fileIO.jsonToDesk(value) match {
        case Some(d) =>
          copy(desk = d, answer = answerState, state = controllerState)
        case _ =>
          copy(desk = desk, answerStateWrong, controllerStateWrong)
      }
    }
  }
}
