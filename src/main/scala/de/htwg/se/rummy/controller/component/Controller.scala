package de.htwg.se.rummy.controller.component

import java.nio.file.{Files, Paths}

import com.google.inject.{Guice, Injector}
import de.htwg.se.rummy.RummyModule
import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.command._
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Color, Tile}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}
import de.htwg.se.rummy.model.fileIO.FileIOInterface
import de.htwg.se.rummy.util.UndoManager
import net.codingwell.scalaguice.InjectorExtensions._

import scala.collection.immutable.SortedSet

class Controller(var desk: DeskInterface) extends ControllerInterface {

  val injector: Injector = Guice.createInjector(new RummyModule)

  private val undoManager = new UndoManager
  private val fileIO = injector.instance[FileIOInterface]

  override def userFinishedPlay(): Unit = {
    if (userPutTileDown == 0) {
      if (desk.bagOfTiles.isEmpty) {
        switchAnswerState(AnswerState.BAG_IS_EMPTY)
        switchControllerState(ControllerState.P_TURN)
      } else {
        undoManager.doStep(new TakeTileCommand(this))
      }
    } else if (desk.checkTable()) {
      if (desk.currentPlayerWon()) {
        switchAnswerState(AnswerState.P_WON)
        switchControllerState(ControllerState.KILL)
      } else {
        undoManager.doStep(new FinishedCommand(userPutTileDown, this))
      }
    } else {
      switchAnswerState(AnswerState.TABLE_NOT_CORRECT)
      switchControllerState(ControllerState.P_TURN)
    }
  }

  override def switchControllerState(c: ControllerState.Value): Unit = {
    controllerState = c
    notifyObservers()
  }

  override def switchAnswerState(c: AnswerState.Value): Unit = {
    answerState = c
    notifyObservers()
  }

  override def moveTile(thisTile: TileInterface, toThisTile: TileInterface): Unit = {
    if (!desk.tableContains(thisTile) || !desk.tableContains(toThisTile)) {
      switchAnswerState(AnswerState.CANT_MOVE_THIS_TILE)
      switchControllerState(ControllerState.P_TURN)
    } else {
      undoManager.doStep(new MoveTileCommand(thisTile, toThisTile, this))
    }
  }

  override def layDownTile(tile: TileInterface): Unit = {
    if (!getCurrentPlayer.hasTile(tile)) {
      switchAnswerState(AnswerState.P_DOES_NOT_OWN_TILE)
      switchControllerState(ControllerState.P_TURN)
    } else {
      undoManager.doStep(new LayDownCommand(tile, this))
    }
  }

  override def getCurrentPlayer: PlayerInterface = desk.getCurrentPlayer

  override def getPreviousPlayer: PlayerInterface = desk.getPreviousPlayer

  override def getNextPlayer: PlayerInterface = desk.getNextPlayer

  override def addPlayerAndInit(newName: String, max: Int): Unit = {
    if (!hasLessThan4Players) {
      switchAnswerState(AnswerState.ADDED_PLAYER)
      switchControllerState(ControllerState.ENOUGH_PLAYER)
    } else {
      undoManager.doStep(new NameCommand(newName, max, this))
    }
  }

  override def hasLessThan4Players: Boolean = desk.lessThan4P

  override def createDesk(amount: Int): Unit = {
    var bagOfTiles: Set[TileInterface] = Set[TileInterface]()
    for (number <- 1 to amount;
         color <- Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
         ident <- 0 to 1) {
      bagOfTiles += Tile(number, color, ident)
    }
    desk = deskBaseImpl.Desk(Set[PlayerInterface](), bagOfTiles, Set[SortedSet[TileInterface]]())
    switchAnswerState(AnswerState.CREATED_DESK)
    switchControllerState(ControllerState.INSERTING_NAMES)
  }

  override def switchToNextPlayer(): Unit = undoManager.doStep(new SwitchPlayerCommand(this))

  override def nameInputFinished(): Unit = {
    if (desk.correctAmountOfPlayers) {
      undoManager.emptyStack()
      switchAnswerState(AnswerState.INSERTING_NAMES_FINISHED)
      switchControllerState(P_TURN)
    } else {
      switchAnswerState(AnswerState.NOT_ENOUGH_PLAYERS)
      switchControllerState(ControllerState.INSERTING_NAMES)
    }
  }

  override def getAmountOfPlayers: Int = desk.amountOfPlayers

  override def setsOnDeskAreCorrect: Boolean = desk.checkTable()

  override def removeTileFromSet(tile: TileInterface): Unit = desk = desk.removeFromTable(tile)

  override def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  override def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }


  override def storeFile(): Unit = {
    fileIO.save(desk)
    switchAnswerState(AnswerState.STORED_FILE)
    switchControllerState(controllerState)
  }

  override def loadFile(): Unit = {
    if (Files.exists(Paths.get("/target/desk.json"))) {
      desk = fileIO.load
      switchAnswerState(AnswerState.LOADED_FILE)
      switchControllerState(P_TURN)
    } else {
      switchAnswerState(AnswerState.COULD_NOT_LOAD_FILE)
      createDesk(12)
    }
  }

  override def viewOfTable: Set[SortedSet[TileInterface]] = desk.tableView

  override def viewOfBoard: SortedSet[TileInterface] = desk.boardView

  override def currentControllerState: ControllerState.Value = controllerState

  override def currentAnswerState: AnswerState.Value = answerState


}

