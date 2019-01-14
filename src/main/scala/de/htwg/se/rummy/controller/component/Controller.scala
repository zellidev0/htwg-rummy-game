package de.htwg.se.rummy.controller.component

import java.nio.file.{Files, Paths}

import com.google.inject.Guice
import de.htwg.se.rummy.RummyModule
import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.ContState._
import de.htwg.se.rummy.controller.component.command._
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Color, Tile}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}
import de.htwg.se.rummy.model.fileIO.FileIOInterface
import de.htwg.se.rummy.util.UndoManager
import javax.inject.Inject
import net.codingwell.scalaguice.InjectorExtensions._

//import de.htwg.se.rummy.model.fileIO.json.FileIO

import scala.collection.SortedSet


class Controller @Inject()(var desk: DeskInterface) extends ControllerInterface {

  var cState = MENU
  private val undoManager = new UndoManager
  val injector = {
    Guice.createInjector(new RummyModule)
  }
  private val fileIO = injector.instance[FileIOInterface]
  var userPutTileDown = 0

  /*userFinishedPlay fully tested*/
  override def userFinishedPlay(): Unit = {
    if (userPutTileDown == 0) {
      if (desk.bagOfTiles.isEmpty) {
        swState(BAG_IS_EMPTY)
        swState(ContState.MENU)
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
      swState(ContState.P_TURN)
    }
  }
  override def swState(c: ContState.Value): Unit = {
    cState = c
    notifyObservers()
  }
  /*moveTile fully tested*/
  override def moveTile(tile1: String, tile2: String): Unit = {
    val t = Tile(-1, Color.RED, -1)
    if (!desk.setsContains(t.stringToTile(tile1)) || !desk.setsContains(t.stringToTile(tile2))) {
      swState(CANT_MOVE_THIS_TILE)
      swState(ContState.P_TURN)
    } else {
      undoManager.doStep(new MoveTileCommand(tile1, tile2, this))
    }
  }

  /*regexToTile fully tested*/
  private[controller] def regexToTile(regex: String): Tile = {
    val color = regex.charAt(regex.length - 2) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    Tile(Integer.parseInt(regex.substring(0, regex.length - 2)), color, Integer.parseInt(regex.charAt(regex.length - 1).toString))
  }
  /*layDownTile fully tested*/
  override def layDownTile(tile: String): Unit = {
    val t = Tile(-1, Color.RED, -1)
    if (!currentP.hasTile(t.stringToTile(tile))) {
      swState(P_DOES_NOT_OWN_TILE)
      swState(ContState.P_TURN)
    } else {
      undoManager.doStep(new LayDownCommand(tile, this))
    }
  }
  /*currentP fully tested*/
  override def currentP: PlayerInterface = desk.currentP
  /*previousP fully tested*/
  override def previousP: PlayerInterface = desk.previousP

  /*nextP fully tested*/
  override def nextP: PlayerInterface = desk.nextP

  /*addPlayerAndInit fully tested*/
  override def addPlayerAndInit(newName: String, max: Int): Unit = {
    if (!hasLessThan4Players) {
      swState(ENOUGH_PS)
    } else {
      undoManager.doStep(new NameCommand(newName, max, this))
    }
  }

  /*hasLessThan4Players fully tested*/
  override def hasLessThan4Players: Boolean = desk.lessThan4P

  /*createDesk fully tested*/
  override def createDesk(amount: Int): Unit = {
    var bagOfTiles: Set[TileInterface] = Set[TileInterface]()
    for (number <- 1 to amount;
         color <- Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
         ident <- 0 to 1) {
      bagOfTiles += Tile(number, color, ident)
    }
    desk = deskBaseImpl.Desk(Set[PlayerInterface](), bagOfTiles, Set[SortedSet[TileInterface]]())
    swState(CREATED)
    swState(ContState.INSERTING_NAMES)
  }

  /*switchToNextPlayer fully tested*/
  override def switchToNextPlayer(): Unit = undoManager.doStep(new SwitchPlayerCommand(this))

  /*nameInputFinished fully tested*/
  override def nameInputFinished(): Unit = {
    if (desk.correctAmountOfPlayers) {
      undoManager.emptyStack
      swState(START)
      swState(P_TURN)
    } else {
      swState(NOT_ENOUGH_PS)
      swState(ContState.INSERTING_NAMES)
    }
  }

  /*getTileSet fully tested*/
  override def getTileSet: Set[SortedSet[TileInterface]] = desk.viewOfSet

  /*getAmountOfPlayers fully tested*/
  override def getAmountOfPlayers: Int = desk.amountOfPlayers

  /*setsOnDeskAreCorrect fully tested*/
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
    val oldState = cState
    swState(STORE_FILE)
    swState(oldState)
  }

  override def loadFile: Unit = {
    if (Files.exists(Paths.get("/home/julian/Documents/se/rummy/desk.json"))) {
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

