package controller

import controller.ContState._
import model._
import util.{Observable, UndoManager}

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {
  var cState: state = MENU
  private val undoManager = new UndoManager
  var userPutTileDown = 0

  /*userFinishedPlay fully tested*/
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


  /*moveTile fully tested*/
  def moveTile(tile1: String, tile2: String): Unit = {
    if (!desk.setsContains(regexToTile(tile1)) || !desk.setsContains(regexToTile(tile2))) {
      swState(TILE_NOT_ON_TABLE)
      swState(ContState.P_TURN)
    } else {
      undoManager.doStep(new MoveTileCommand(tile1, tile2, this))
    }
  }

  /*layDownTile fully tested*/
  def layDownTile(tile: String): Unit = {
    if (!currentP.hasTile(regexToTile(tile))) {
      swState(P_DOES_NOT_OWN_TILE)
      swState(ContState.P_TURN)
    } else {
      undoManager.doStep(new LayDownCommand(tile, this))
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
  /*currentP fully tested*/
  def currentP: Player = desk.currentP
  def swState(c: ContState.Value): Unit = {
    cState = c
    notifyObservers()
  }
  /*previousP fully tested*/
  def previousP: Player = desk.previousP
  /*nextP fully tested*/
  def nextP: Player = desk.nextP
  /*addPlayerAndInit fully tested*/
  def addPlayerAndInit(newName: String, max: Int): Unit = {
    if (!hasLessThan4Players) {
      swState(ENOUGH_PS)
    } else {
      undoManager.doStep(new NameCommand(newName, max, this))
    }
  }
  /*hasLessThan4Players fully tested*/
  def hasLessThan4Players: Boolean = desk.lessThan4P

  /*createDesk fully tested*/
  def createDesk(amount: Int): Unit = {
    var bagOfTiles: Set[Tile] = Set[Tile]()
    for (number <- 1 to amount;
         color <- Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
         ident <- 0 to 1) {
      bagOfTiles += Tile(number, color, ident)
    }
    desk = Desk(Set[Player](), bagOfTiles, Set[SortedSet[Tile]]())
    swState(CREATED)
    swState(ContState.INSERTING_NAMES)
  }

  /*switchToNextPlayer fully tested*/
  def switchToNextPlayer(): Unit = undoManager.doStep(new SwitchPlayerCommand(this))

  /*nameInputFinished fully tested*/
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

  /*getTileSet fully tested*/
  def getTileSet: Set[SortedSet[Tile]] = desk.sets

  /*getAmountOfPlayers fully tested*/
  def getAmountOfPlayers: Int = desk.amountOfPlayers

  /*setsOnDeskAreCorrect fully tested*/
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
