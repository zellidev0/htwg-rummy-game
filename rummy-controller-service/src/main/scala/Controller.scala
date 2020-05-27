import AnswerState.{Value => _, apply => _, _}
import ControllerState._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import model.DeskInterface
import model.deskComp.deskBaseImpl.TileInterface
import model.fileIO.FileIOJson
import play.api.libs.json.{JsObject, Json}

import scala.collection.immutable.SortedSet
import scala.util.{Success, Try}

case class Controller(private val desk: DeskInterface,
                      answer: AnswerState.Value,
                      state: ControllerState.Value,
                      undoMgr: UndoManager = UndoManager())
  extends ControllerInterface {

  type TileI = TileInterface
  type DeskI = DeskInterface
  type ControllerI = ControllerInterface

  private val URL_PLAYER = "http://localhost:9001/"
  private val URL_GAME = "http://localhost:9002/"

  private val fileIO = new FileIOJson()
  private val playerService = PlayerService()
  private val gameService = GameService()
  private implicit val system: ActorSystem = ActorSystem()
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  override def switchToNextPlayer(): ControllerI = {
    sendRequest(
      formerDesk = fileIO.deskToJson(desk),
      url = URL_PLAYER + "switchToNext",
      answerState = SWITCHED_TO_NEXT,
      answerStateWrong = COULD_NOT_PARSE,
      controllerState = P_TURN)
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
        controllerState = ControllerState.INSERTING_NAMES)
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
      controllerState = ControllerState.INSERTING_NAMES)


  override def moveTile(tile: TileI, to: TileI): ControllerI = copy(
    desk = gameService.moveTile(desk, tile, to),
    answer = MOVED_TILE,
    state = P_TURN,
    undoMgr = undoMgr.putOnStack(desk)
  )

  override def layDownTile(tile: TileI): ControllerI = copy(
    desk = gameService.putTileDown(desk, tile),
    answer = PUT_TILE_DOWN,
    state = P_TURN,
    undoMgr = undoMgr.putOnStack(desk)
  )

  override def currentPlayerName: String =
    playerService.currentPlayerName(desk)

  // Views
  override def viewOfTable: Set[SortedSet[TileI]] =
    gameService.tableView(desk)

  override def viewOfBoard: SortedSet[TileI] =
    gameService.boardView(desk)

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
    case None => copy(desk = gameService.createDefaultTable(12), answer = CREATED_DESK, state = INSERTING_NAMES)
  }

  override def toJson: JsObject = Json.obj("state" -> state, "answer" -> answer, "desk" -> fileIO.toJson(desk))

  private def sendRequest(formerDesk: JsObject,
                          url: String,
                          answerState: AnswerState.Value,
                          answerStateWrong: AnswerState.Value,
                          controllerState: ControllerState.Value): ControllerInterface = {
    val request = HttpRequest(POST, uri = url, entity = formerDesk.toString())
    val responseFuture = Http().singleRequest(request)
    while (!responseFuture.isCompleted) {
      Thread.sleep(100)
    }
    Try(fileIO.jsonToDesk(Json.parse(responseFuture.value.get.get.entity.toString))) match {
      case Success(value) if value.isDefined =>
        copy(desk = value.get, answer = answerState, state = controllerState)
      case _ =>
        copy(desk = desk, answerStateWrong, controllerState)
    }
  }
}
