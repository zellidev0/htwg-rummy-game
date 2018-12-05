package controller

import model.{ContState, _}
import util.Observable

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {
  var state: ContState.Value = ContState.MENU
  var userDidSomething = false

  def userFinishedPlay(): Unit = {
    if (!userDidSomething) {
      desk = desk.takeTile(currentP)
      userDidSomething = false
      swState(ContState.PLAYER_FINISHED)
      return
    }
    if (desk.checkTable())
      if (desk.currentPlayerWon()) swState(controllerState = ContState.PLAYER_WON) else swState(controllerState = ContState.PLAYER_FINISHED)
    else
      swState(controllerState = ContState.TABLE_NOT_CORRECT)
  }

  def moveTile(tile: String, tile2: String): Unit = {
    desk = desk.moveTwoTilesOnDesk(currentP, regexToTile(tile), regexToTile(tile2))
    userDidSomething = true
    notifyObservers()
  } /*t*/

  def layDownTile(tile: String): Unit = {
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
      swState(ContState.ENOUGH_PLAYERS)
      return
    }
    val playerNumber = desk.amountOfPlayers
    val p = Player(newName, playerNumber, Board(SortedSet[Tile]()), if (desk.players.nonEmpty) State.WAIT else State.TURN)
    desk = desk.addPlayer(p)
    for (_ <- 1 to max) {
      desk = desk.takeTile(desk.findPlayer(playerNumber).getOrElse(throw new IllegalArgumentException("Player not found")))
    }
    swState(ContState.INSERTED_NAME)
  } /*t*/

  def switchToNextPlayer(): Unit = {
    desk = desk.switchToNextPlayer(currentP, nextP)
    swState(ContState.PLAYER_TURN)
  } /*t*/

  def hasMoreThan1Player: Boolean = desk.hasMoreThan1Player

  def hasLessThan4Players: Boolean = desk.hasLessThan4Players

  def nextP: Player = desk.nextP /*t*/

  def nameInputFinished(): Unit = if (desk.hasCorrectAmountOfPlayers) swState(ContState.START) else swState(ContState.NOT_ENOUGH_PLAYERS)

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
    swState(ContState.CREATED)
  } /*t*/

  def getTileSet: Set[SortedSet[Tile]] = desk.sets

  def swState(controllerState: ContState.Value): Unit = {
    state = controllerState
    notifyObservers()
  }

  def getAmountOfPlayers: Int = desk.amountOfPlayers

  def setsOnDeskAreCorrect: Boolean = desk.setsOnDeskAreCorrect


}
