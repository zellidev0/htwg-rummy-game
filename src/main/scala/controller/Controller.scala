package controller

import model.{ControllerState, _}
import util.Observable

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {
  var state: ControllerState.Value = ControllerState.MENU

  def moveTile(toMoveString: String): Unit = desk = desk.moveTile(currentPlayer, getTileFromRegex(toMoveString.split(" t ").apply(0).split(" ").apply(1)), getTileFromRegex(toMoveString.split(" t ").apply(1)));
  notifyObservers()

  def layDownTile(toInsert: String): Unit = desk = desk.layDownTileOnTable(currentPlayer, getTileFromRegex(toInsert.split(" ").apply(1)));
  notifyObservers()

  def currentPlayer: Player = desk.players.find(_.state == State.TURN).getOrElse(throw new IllegalAccessException("Could not find the current player"))

  def getTileFromRegex(regexString: String): Tile = {
    val color = regexString.charAt(1) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    Tile(Integer.parseInt(regexString.charAt(0).toString), color, Integer.parseInt(regexString.charAt(2).toString))
  }

  def takeFromBagOfTiles(): Unit = desk = desk.takeTileFromBag(currentPlayer)

  notifyObservers()

  def setPlayerName(newName: String): Unit = desk = desk.copy(players = desk.players + Player(newName, desk.players.size, Board(Set[Tile]()), if (desk.players.nonEmpty) State.WAIT else State.TURN))

  def switchToNextPlayer(): Unit = desk = desk.switchToNextPlayer(currentPlayer, getNextPlayer)

  def getNextPlayer: Player = if (currentPlayer.number + 1 == desk.players.size) desk.players.find(_.number == 0).getOrElse(throw new IllegalAccessError("Could not get next player!")) else desk.players.find(_.number == currentPlayer.number + 1).getOrElse(throw new IllegalAccessError("Could not get next player!"))

  def initPlayersWithStones(amountOfStones: Int): Unit = (1 to amountOfStones).foreach(_ => desk.players.foreach(p => desk = desk.takeTileFromBag(p)))

  def hasMoreThan1Player: Boolean = desk.hasMoreThan1Player

  def hasLessThan4Players: Boolean = desk.hasLessThan4Players

  def getAmountOfPlayers: Int = desk.getAmountOfPlayers

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

}
