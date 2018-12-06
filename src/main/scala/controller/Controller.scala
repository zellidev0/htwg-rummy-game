package controller

import model.ContState._
import model.{ContState, _}
import util.Observable

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {
  var stateM: state = MENU
  var userDidSomething = false

  def userFinishedPlay(): Unit = {
    if (!userDidSomething) {
      desk = desk.takeTile(currentP)
      userDidSomething = false
      swState(P_FINISHED)
      return
    }
    if (desk.checkTable())
      if (desk.currentPlayerWon()) swState(P_WON) else swState(P_FINISHED)
    else
      swState(TABLE_NOT_CORRECT)
  }

  def moveTile(tile: String, tile2: String): Unit = {
    desk = desk.moveTwoTilesOnDesk(currentP, regexToTile(tile), regexToTile(tile2))
    userDidSomething = true
    notifyObservers()
  } /*t*/

  def layDownTile(tile: String): Unit = {
    if (!currentP.board.contains(regexToTile(tile))) {
      swState(P_DOES_NOT_OWN_TILE)
      return
    }
    desk = desk.putDownTile(currentP, regexToTile(tile))
    userDidSomething = true
    notifyObservers()
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

  def currentP: Player = desk.currentP /*t*/

  def addPlayerAndInit(newName: String, max: Int): Unit = {
    if (!hasLessThan4Players) {
      swState(ENOUGH_PS)
      return
    }
    desk = desk.addPlayer(Player(newName, desk.amountOfPlayers, Board(SortedSet[Tile]()), if (desk.players.nonEmpty) State.WAIT else State.TURN))
    for (_ <- 1 to max) {
      desk = desk.takeTile(desk.players.find(_.number == desk.amountOfPlayers - 1).get)
    }
    swState(INSERTED_NAME)
  } /*t*/

  def swState(c: ContState.Value): Unit = {
    stateM = c
    notifyObservers()
  }

  def hasLessThan4Players: Boolean = desk.hasLessThan4Players

  def nextP: Player = desk.nextP /*t*/

  def switchToNextPlayer(): Unit = {
    desk = desk.switchToNextPlayer(currentP, nextP)
    swState(P_TURN)
  } /*t*/

  def nameInputFinished(): Unit = if (desk.hasCorrectAmountOfPlayers) swState(START) else swState(NOT_ENOUGH_PS)

  def getTileSet: Set[SortedSet[Tile]] = desk.sets

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
  } /*t*/

  def getAmountOfPlayers: Int = desk.amountOfPlayers

  def setsOnDeskAreCorrect: Boolean = desk.checkTable()


}
