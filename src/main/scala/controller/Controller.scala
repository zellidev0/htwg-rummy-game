package controller

import model._
import util.Observable

import scala.collection.SortedSet

class Controller(var desk: Desk) extends Observable {

  def moveTile(toMoveString: String): Unit = {
    //assert(toMoveString.matches("(m [1-13][RBYG][01] t [1-13][RBYG][01])"))

    val tiles = toMoveString.split(" t ")
    val tile1 = getTileFromRegex(tiles.apply(0).split(" ").apply(1))
    val tile2 = getTileFromRegex(tiles.apply(1))
    desk = desk.moveTile(getCurrentPlayer, tile1, tile2)
  }


  def layDownTile(toInsert: String): Unit = {
    assert(toInsert.matches("(l [1-13][RBYG][01])"))

    val tile = getTileFromRegex(toInsert.split(" ").apply(1))

    if (getCurrentPlayer.board.contains(tile)) {
      desk = desk.layDownTileOnTable(getCurrentPlayer, tile)
    }
  }

  def getCurrentPlayer: Player = {
    desk.players.find(p => p.state == State.TURN) match {
      case Some(value) => value
      case None => throw new IllegalAccessException("Could not find the current player")
    }
  }

  def getTileFromRegex(regexString: String): Tile = {
    val value = Integer.parseInt(regexString.charAt(0).toString)
    val color = regexString.charAt(1) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    val num = Integer.parseInt(regexString.charAt(2).toString)
    Tile(value, color, num)
  }

  def takeFromBagOfTiles() = desk = desk.takeTileFromBag(getCurrentPlayer)

  def setPlayerName(newName: String): Boolean = {
    if (!newName.matches("([A-Za-z]+)")) return false
    if (!desk.hasNotMorePlayersThanAllowed) return false
    if (desk.players.nonEmpty) {
      desk = desk.copy(players = desk.players.+(Player(newName, desk.players.size, Board(Set[Tile]()), State.WAIT)))
      return true
    }
    desk = desk.copy(players = desk.players.+(Player(newName, desk.players.size, Board(Set[Tile]()), State.TURN)))
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

  def switchToNextPlayer(): Unit = desk = desk.switchToNextPlayer(getCurrentPlayer, getNextPlayer)


  def getNextPlayer: Player = {
    val amountOfPlayers = desk.players.size
    val currentPlayerNumber = getCurrentPlayer.number
    var next: Player = null

    if (currentPlayerNumber + 1 == amountOfPlayers) {
      desk.players.find(p => p.number == 0) match {
        case Some(value) => next = value
        case None => throw new IllegalAccessError("Could not get next player!")
      }
    } else {
      desk.players.find(p => p.number == currentPlayerNumber + 1) match {
        case Some(value) => next = value
        case None => throw new IllegalAccessError("Could not get next player!")
      }
    }
    next
  }

  def initPlayersWithStones(amountOfStones: Int): Unit = {

    for (player <- desk.players) {
      for (num <- 1 to amountOfStones) {
        desk = desk.copy(players = desk.players.-(player).+(player.addToBoard(desk.getRandomTile)))
      }


    }
  }

  def hasEnoughPlayers: Boolean = desk.hasEnoughPlayers

  def hasMorePlayersThanAllowed: Boolean = desk.hasNotMorePlayersThanAllowed

}