package model

import scala.collection.SortedSet
import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], sets: Set[SortedSet[Tile]]) {

  def getAmountOfPlayers: Int = players.size

  def size: Int = {
    var x = 0
    sets.foreach(sorted => x += sorted.size)
    x
  }


  def moveTile(current: Player, tile1: Tile, tile2: Tile): Desk = {
    if (tileSetContains(tile1) && tileSetContains(tile2)) {
      val setOfTile1 = setWithTile(tile1).getOrElse(throw new IllegalAccessError("Could not find tile 1"))
      val setOfTile2 = setWithTile(tile2).getOrElse(throw new IllegalAccessError("Could not find tile 2"))
      return copy(sets = removeEmptySets(sets - setOfTile1 + (setOfTile1 - tile1) - setOfTile2 + (setOfTile2 + tile1)))
    }
    this
  }

  def removeEmptySets(set: Set[SortedSet[Tile]]): Set[SortedSet[Tile]] = set.filter(f => f.nonEmpty)

  def tileSetContains(tile: Tile): Boolean = sets.exists(_.contains(tile))

  def setWithTile(tile: Tile): Option[SortedSet[Tile]] = sets.find(set => set.contains(tile))

  def layDownTileOnTable(player: Player, tile: Tile): Desk = {
    val foundPlayer = players.find(_ == player).getOrElse(throw new IllegalArgumentException("Could not find the player tat puts the tile on the table"))
    copy(players = players - foundPlayer + foundPlayer.takeFromBoard(tile), sets = sets + (SortedSet[Tile]() + tile))
  }

  def takeTileFromBag(player: Player): Desk = {
    val tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))
    copy(players = players - player + players.find(_ == player).getOrElse(throw new IllegalArgumentException("The Player does not exist!")).addToBoard(tile), bagOfTiles = bagOfTiles - tile)
  }


  def switchToNextPlayer(curr: Player, next: Player): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).getOrElse(throw new IllegalArgumentException("Could not set current player on WAIT!")).changeState(State.WAIT)
    copy(players = newPlayers - curr + newPlayers.find(_ == next).getOrElse(throw new IllegalArgumentException("Could not set next Player on TURN!")).changeState(State.TURN))
  }

  def addPlayers(player: Player): Desk = copy(players = players.+(player))

  def hasMoreThan1Player: Boolean = players.size >= 2

  def hasLessThan4Players: Boolean = players.size < 4

  def hasCorrectAmountOfPlayers: Boolean = players.size >= 2 && players.size <= 4

}
