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

import scala.collection.SortedSet

class Controller(var desk: DeskInterface) extends ControllerInterface {

  var userPutTileDown = 0
  var controllerState: ControllerState.Value = MENU
  val injector: Injector = {
    Guice.createInjector(new RummyModule)
  }
  private val undoManager = new UndoManager
  private val fileIO = injector.instance[FileIOInterface]

  override def userFinishedPlay(): Unit = {
    if (userPutTileDown == 0) {
      if (desk.bagOfTiles.isEmpty) {
        swState(BAG_IS_EMPTY)
        swState(ControllerState.MENU)
      } else {
        undoManager.doStep(new TakeTileCommand(this))
      }
    } else if (desk.checkTable()) {
      if (desk.currentPlayerWon()) {
        swState(P_WON)
        return
      }
      undoManager.doStep(new FinishedCommand(userPutTileDown, this))
    } else {
      swState(TABLE_NOT_CORRECT)
      swState(ControllerState.P_TURN)
    }
  }

  override def swState(c: ControllerState.Value): Unit = {
    controllerState = c
    notifyObservers()
  }

  override def moveTile(tile1: String, tile2: String): Unit = {
    val t = Tile(-1, Color.RED, -1)
    if (!desk.boardContains(t.stringToTile(tile1)) || !desk.boardContains(t.stringToTile(tile2))) {
      swState(CANT_MOVE_THIS_TILE)
      swState(ControllerState.P_TURN)
    } else {
      undoManager.doStep(new MoveTileCommand(tile1, tile2, this))
    }
  }

  override def layDownTile(tile: String): Unit = {
    val t = Tile(-1, Color.RED, -1)
    if (!currentP.hasTile(t.stringToTile(tile))) {
      swState(P_DOES_NOT_OWN_TILE)
      swState(ControllerState.P_TURN)
    } else {
      undoManager.doStep(new LayDownCommand(tile, this))
    }
  }

  override def currentP: PlayerInterface = desk.getCurrentPlayer

  override def previousP: PlayerInterface = desk.getPreviousPlayer

  override def nextP: PlayerInterface = desk.getNextPlayer

  override def addPlayerAndInit(newName: String, max: Int): Unit = {
    if (!hasLessThan4Players) {
      swState(ENOUGH_PS)
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
    swState(CREATED)
    swState(ControllerState.INSERTING_NAMES)
  }

  override def switchToNextPlayer(): Unit = undoManager.doStep(new SwitchPlayerCommand(this))

  override def nameInputFinished(): Unit = {
    if (desk.correctAmountOfPlayers) {
      undoManager.emptyStack
      swState(START)
      swState(P_TURN)
    } else {
      swState(NOT_ENOUGH_PS)
      swState(ControllerState.INSERTING_NAMES)
    }
  }

  override def getTileSet: Set[SortedSet[TileInterface]] = desk.viewOfSet

  override def getAmountOfPlayers: Int = desk.amountOfPlayers

  override def setsOnDeskAreCorrect: Boolean = desk.checkTable()

  override def removeTileFromSet(tile: TileInterface): Unit = desk = desk.removeFromTable(tile)

  override def undo: Unit = {
    undoManager.undoStep
    notifyObservers()
  }

  override def redo: Unit = {
    undoManager.redoStep
    notifyObservers()
  }

  override def viewOfBoard: SortedSet[TileInterface] = desk.viewOfBoard

  override def storeFile: Unit = {
    fileIO.save(desk)
    val oldState = controllerState
    swState(STORE_FILE)
    swState(oldState)
  }

  override def loadFile: Unit = {
    if (Files.exists(Paths.get("/target/desk.json"))) {
      desk = fileIO.load
      swState(LOAD_FILE)
      swState(START)
      swState(P_TURN)
    } else {
      swState(COULD_NOT_LOAD_FILE)
      createDesk(12)
    }


  }
}

