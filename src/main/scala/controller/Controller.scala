package controller

import model.{ControllerState, _}
import util.Observable

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {
  var state: ControllerState.Value = ControllerState.MENU

  def moveTile(tile: String, tile2: String): Unit = desk = desk.moveTwoTilesOnDesk(currentP, regexToTile(tile), regexToTile(tile));
  notifyObservers()

  def layDownTile(tile: String): Unit = desk = desk.putDownTile(currentP, regexToTile(tile));
  notifyObservers()

  def currentP: Player = desk.currentP

  private[controller] def regexToTile(regexString: String): Tile = {
    val color = regexString.charAt(1) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    Tile(Integer.parseInt(regexString.charAt(0).toString), color, Integer.parseInt(regexString.charAt(2).toString))
  }

  def takeATile(): Unit = desk = desk.takeTile(currentP);

  def addPlayerAndInit(newName: String): Unit = {
    val playerNumber = desk.amountOfPlayers
    val p = Player(newName, playerNumber, Board(SortedSet[Tile]()), if (desk.players.nonEmpty) State.WAIT else State.TURN)
    desk = desk.addPlayer(p)
    for (_ <- 1 to 12) {
      desk = desk.takeTile(desk.findPlayer(playerNumber).getOrElse(throw new IllegalArgumentException("Player not found")))
    }
  }

  def switchToNextPlayer(): Unit = desk = desk.switchToNextPlayer(currentP, nextP);
  notifyObservers()

  def hasMoreThan1Player: Boolean = desk.hasMoreThan1Player

  def hasLessThan4Players: Boolean = desk.hasLessThan4Players

  def nextP: Player = desk.nextP

  def switchControllerState(controllerState: ControllerState.Value): Unit = state = controllerState

  def hasCorrectAmountOfPlayers: Boolean = desk.hasCorrectAmountOfPlayers

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
  }

  def getAmountOfPlayers: Int = desk.amountOfPlayers

  def setsOnDeskAreCorrect: Boolean = desk.setsOnDeskAreCorrect


}
