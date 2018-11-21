package controller

import model._
import util.Observable

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {
  def moveTile(toInsert: String): Unit = {
    assert(toInsert.matches("(m [1-13] [RBYG] [01] l [1-13] [RBYG] [01])"))

    var tiles = toInsert.split(" l ")
    var tile1 = getTileFromRegex(tiles.apply(0))
    var tile2 = getTileFromRegex(tiles.apply(1))
    desk = desk.moveTile(tile1, tile2)
  }


  def layDownTile(toInsert: String): Unit = {
    assert(toInsert.matches("(l [1-13] [RBYG] [01])"))
    val tile = getTileFromRegex(toInsert)

    if (getCurrentPlayer.board.contains(tile)) {
      desk = desk.layDownTileOnTable(getCurrentPlayer, tile)
    }
  }

  private def getCurrentPlayer: Player = {
    desk.players.find(p => p.state == State.TURN) match {
      case Some(value) => value
    }
  }

  private def getTileFromRegex(toInsert: String): Tile = {
    val splitted = toInsert.split(" ")
    val value = Integer.parseInt(splitted.apply(1))
    val color = splitted.apply(2) match {
      case "R" => Color.RED
      case "B" => Color.BLUE
      case "Y" => Color.YELLOW
      case "G" => Color.GREEN
    }
    val num = Integer.parseInt(splitted.apply(3))
    Tile(value, color, num)
  }

  def takeFromBagOfTiles() = desk = desk.takeTileFromBag(getCurrentPlayer)

  def setPlayerName(newName: String): Boolean = {
    if (!newName.matches("([A-Za-z]+)")) false
    if (desk.hasCorrectAmountOfPlayers) false
    desk = desk.copy(players = desk.players.+(Player(newName, desk.players.size, Board(Set[Tile]()), State.WAIT)))
    true
  }

  def createDesk(amountOfDifferentTiles: Int): Unit = {
    var bagOfTiles: Set[Tile] = Set[Tile]()

    for (number <- 1 to amountOfDifferentTiles) {
      for (color <- Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE)) {
        for (ident <- 0 to 1) {
          bagOfTiles += Tile(number, color, ident)
        }
      }
    }
    desk = Desk(Set[Player](), bagOfTiles, Set[SortedSet[Tile]]())
  }

  def switchToNextPlayer(): Unit = {
    val playerTuple = getNextPlayer
    desk = desk.switchToNextPlayer(playerTuple._1, playerTuple._2)
  }

  private def getNextPlayer: (Player, Player) = {
    val amountOfPlayers = desk.players.size
    val currentPlayerNumber = getCurrentPlayer.number
    var current: Player = null
    var next: Player = null
    desk.players.find(p => p.number == currentPlayerNumber) match {
      case Some(value) => current = value
    }
    if (currentPlayerNumber + 1 == amountOfPlayers) {
      desk.players.find(p => p.number == 0) match {
        case Some(value) => next = value
      }
    } else {
      desk.players.find(p => p.number == currentPlayerNumber) match {
        case Some(value) => next = value
      }
    }
    (current, next)
  }

  def initPlayer(): Unit = {
    desk.players.find(p => p.number == 0) match {
      case Some(pla) =>
        desk = desk.copy(players = desk.players.-(pla).+(pla.changeState(State.TURN)))
        for (player <- desk.players) {
          for (num <- 1 to 12) {
            desk = desk.copy(players = desk.players.-(player).+(player.addToBoard(desk.getRandomTile)))
          }

        }
    }
  }
}