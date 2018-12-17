package controller

import model.ContState._
import model.{ContState, _}
import util.{Observable, UndoManager}

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {
  var stateM: state = MENU
  private val undoManager = new UndoManager
  var userPutTileDown = 0
  def userFinishedPlay(): Unit = {
    if (userPutTileDown == 0) {
      undoManager.doStep(new TakeTileCommand(this))
    } else if (desk.checkTable()) {
      undoManager.doStep(new FinishedCommand(userPutTileDown, this))
    } else {
      swState(TABLE_NOT_CORRECT)
      swState(ContState.P_TURN)
    }
  }
  //TODO NEVER GONNA GIVE YOU UP
  //TODO NEVER GONNA LET YOU DOWN
  //TODO YOU JUST GOT RICK ROLLED BOI <3
  def moveTile(tile1: String, tile2: String): Unit = {
    if (!desk.setsContains(regexToTile(tile1)) || !desk.setsContains(regexToTile(tile2))) {
      swState(TILE_NOT_ON_TABLE)
      swState(ContState.P_TURN)
      return
    }
    undoManager.doStep(new MoveTileCommand(tile1, tile2, this))
  } /*t*/

  def layDownTile(tile: String): Unit = {
    if (!currentP.hasTile(regexToTile(tile))) {
      swState(P_DOES_NOT_OWN_TILE)
      swState(ContState.P_TURN)
      return
    }
    undoManager.doStep(new LayDownCommand(tile, this))
  } /*t*/

  private[controller] def regexToTile(regexString: String): Tile = {
    val color = regexString.charAt(regexString.length - 2) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    Tile(Integer.parseInt(regexString.substring(0, regexString.length - 2)), color, Integer.parseInt(regexString.charAt(regexString.length - 1).toString))
  } /*t*/


  def previousP: Player = desk.previousP

  def currentP: Player = desk.currentP /*t*/

  def nextP: Player = desk.nextP /*t*/

  def addPlayerAndInit(newName: String, max: Int): Unit = {
    if (!hasLessThan4Players) {
      swState(ENOUGH_PS)
      swState(ContState.INSERTING_NAMES)
      return
    }
    undoManager.doStep(new NameCommand(newName, max, this))
  } /*t*/

  def swState(c: ContState.Value): Unit = {
    stateM = c
    notifyObservers()
  }
  def hasLessThan4Players: Boolean = desk.lessThan4P

  def createDesk(amount: Int): Unit = {
    val colorSet = Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE)
    var bagOfTiles: Set[Tile] = Set[Tile]()
    for (number <- 1 to amount) {
      for (color <- colorSet) {
        for (ident <- 0 to 1) {
          bagOfTiles += Tile(number, color, ident)
        }
      }
    }
    desk = Desk(Set[Player](), bagOfTiles, Set[SortedSet[Tile]]())
    swState(CREATED)
    swState(ContState.INSERTING_NAMES)

  } /*t*/
  def switchToNextPlayer(): Unit = undoManager.doStep(new SwitchPlayerCommand(this))
  /*t*/
  def nameInputFinished(): Unit = {
    if (desk.correctAmountOfPlayers) {
      undoManager.emptyStack
      swState(START)
      swState(P_TURN)
    } else {
      swState(NOT_ENOUGH_PS)
      swState(ContState.INSERTING_NAMES)
    }
  }

  def getTileSet: Set[SortedSet[Tile]] = desk.sets

  def getAmountOfPlayers: Int = desk.amountOfPlayers

  def setsOnDeskAreCorrect: Boolean = desk.checkTable()

  def removeTileFromSet(tile: Tile): Unit = desk = desk.removeFromTable(tile)

  def undo: Unit = {
    undoManager.undoStep
    notifyObservers()
  }

  def redo: Unit = {
    undoManager.redoStep
    notifyObservers()
  }

}
