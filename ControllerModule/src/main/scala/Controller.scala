import AnswerState.{ Value => _, apply => _, _ }
import ControllerState._
import model.DeskInterface
import model.deskComp.deskBaseImpl.TileInterface
import model.fileIO.json.FileIO
import play.api.libs.json.{ JsNumber, JsObject, Json }

import scala.collection.immutable.SortedSet

case class Controller(private val desk: DeskInterface,
                      answer: AnswerState.Value,
                      state: ControllerState.Value,
                      undoMgr: UndoManager = UndoManager())
    extends ControllerInterface {

  type TileI       = TileInterface
  type DeskI       = DeskInterface
  type ControllerI = ControllerInterface

  private val fileIO        = new FileIO()
  private val playerService = PlayerService()
  private val gameService   = GameService()

  //PlayerService

  override def switchToNextPlayer(): ControllerI = copy(
    desk = playerService.switchToNextPlayer(desk),
    answer = NONE,
    state = P_TURN
  )

  override def nameInputFinished(): ControllerI =
    if (desk.correctAmountOfPlayers) copy(desk, answer = INSERTING_NAMES_FINISHED, state = P_TURN)
    else copy(desk, answer = NOT_ENOUGH_PLAYERS, state = INSERTING_NAMES)

  override def addPlayerAndInit(name: String, amountOfTiles: Int): ControllerI =
    if (desk.lessThan4P)
      copy(desk = playerService.addPlayerAndInit(desk, name, amountOfTiles),
           answer = ADDED_PLAYER,
           state = INSERTING_NAMES,
           undoMgr = undoMgr.putOnStack(desk))
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

  override def createDesk(amount: Int): ControllerI = copy(
    desk = gameService.createDefaultTable(12),
    answer = CREATED_DESK,
    state = INSERTING_NAMES
  )

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
    case None       => copy(desk = gameService.createDefaultTable(12), answer = CREATED_DESK, state = INSERTING_NAMES)
  }

  override def toJson: JsObject = Json.obj("state" -> state, "answer" -> answer, "desk" -> fileIO.toJson(desk))
}
