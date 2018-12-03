package model

import scala.collection.SortedSet
import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], sets: Set[SortedSet[Tile]]) {

  def amountOfPlayers: Int = players.size

  def amountOfTilesOnTable: Int = {
    var x = 0
    sets.foreach(sorted => x += sorted.size)
    x
  }

  def nextP: Player = {
    if (currentP.number + 1 == players.size) {
      players.find(_.number == 0).getOrElse(throw new IllegalAccessError("Could not get next player!"))
    } else {
      players.find(_.number == currentP.number + 1).getOrElse(throw new IllegalAccessError("Could not get next player!"))
    }
  }

  def currentP: Player = players.find(_.state == State.TURN).getOrElse(throw new IllegalAccessException("Could not find the current player"))

  def moveTwoTilesOnDesk(current: Player, tile1: Tile, tile2: Tile): Desk = {
    if (tileSetContains(tile1) && tileSetContains(tile2)) {
      val setOfTile1 = setWithTile(tile1).getOrElse(throw new IllegalAccessError("Could not find tile 1"))
      val setOfTile2 = setWithTile(tile2).getOrElse(throw new IllegalAccessError("Could not find tile 2"))
      return copy(sets = removeEmptySets(sets - setOfTile1 + (setOfTile1 - tile1) - setOfTile2 + (setOfTile2 + tile1)))
    }
    this
  }

  private[model] def removeEmptySets(set: Set[SortedSet[Tile]]): Set[SortedSet[Tile]] = set.filter(f => f.nonEmpty)

  private[model] def tileSetContains(tile: Tile): Boolean = sets.exists(_.contains(tile))

  private[model] def setWithTile(tile: Tile): Option[SortedSet[Tile]] = sets.find(set => set.contains(tile))

  def putDownTile(player: Player, tile: Tile): Desk = {
    val foundPlayer = players.find(_ == player).getOrElse(throw new IllegalArgumentException("Could not find the player tat puts the tile on the table"))
    copy(players = players - foundPlayer + foundPlayer.-(tile), sets = sets + (SortedSet[Tile]() + tile))
  }

  def takeTile(player: Player): Desk = {
    val tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))
    copy(players = players - player + (players.find(_ == player).getOrElse(throw new IllegalArgumentException("The Player does not exist!")) + tile), bagOfTiles = bagOfTiles - tile)
  }


  def switchToNextPlayer(curr: Player, next: Player): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).getOrElse(throw new IllegalArgumentException("Could not set current player on WAIT!")).changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).getOrElse(throw new IllegalArgumentException("Could not set next Player on TURN!")).changeState(State.TURN))
  }

  def addPlayer(p: Player): Desk = copy(players = players + p)

  def hasMoreThan1Player: Boolean = players.size >= 2

  def hasLessThan4Players: Boolean = players.size < 4

  def hasCorrectAmountOfPlayers: Boolean = players.size >= 2 && players.size <= 4

  def setsOnDeskAreCorrect: Boolean = !sets.exists(set => set.size < 3)

  def findPlayer(playerNumber: Int): Option[Player] = players.find(_.number == playerNumber)
}
