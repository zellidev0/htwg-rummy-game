package model

import model.Color.Color

import scala.collection.SortedSet
import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], sets: Set[SortedSet[Tile]]) {


  def currentPlayerWon(): Boolean = currentP.won() /*t*/

  def checkTable(): Boolean = {
    for (set <- sets) {
      if (set.size < 3) return false
      if (!checkStreet(set) && !checkPair(set)) {
        return false
      }
    }
    true
  } /*t*/

  def checkStreet(set: SortedSet[Tile]): Boolean = {
    if (set.isEmpty || set.size < 3) return false
    val x = set.toArray
    var first = x.apply(0)
    for (i <- x.indices) {
      if (i != x.length - 1) {
        if (first.value != x.apply(i + 1).value - 1 || first.color != x.apply(i + 1).color) return false
        first = x.apply(i + 1)
      } else {
        if (first.value != x.apply(i - 1).value + 1 || first.color != x.apply(i - 1).color) return false
      }
    }
    true
  } /*t*/

  def checkPair(set: SortedSet[Tile]): Boolean = {
    if (set.isEmpty || set.size < 3 || set.size > 4) {
      return false
    }
    var setOfValues = Set[Int]()
    var setOfColors = Set[Color]()
    set.foreach(t => setOfValues = setOfValues.+(t.value))
    set.foreach(t => setOfColors = setOfColors.+(t.color))
    if (setOfValues.size != 1 || setOfColors.size != set.size) return false
    true
  } /*t*/

  def amountOfPlayers: Int = players.size /*t*/

  def amountOfTilesOnTable: Int = {
    var x = 0
    sets.foreach(sorted => x += sorted.size)
    x
  } /*t*/

  def nextP: Player = {
    if (currentP.number + 1 == players.size) {
      players.find(_.number == 0).getOrElse(throw new IllegalAccessError("Could not get next player!"))
    } else {
      players.find(_.number == currentP.number + 1).getOrElse(throw new IllegalAccessError("Could not get next player!"))
    }
  } /*t*/

  def currentP: Player = players.find(_.state == State.TURN).getOrElse(throw new IllegalAccessException("Could not find the current player")) /*t*/

  def moveTwoTilesOnDesk(current: Player, tile1: Tile, tile2: Tile): Desk = {
    if (tileSetContains(tile1) && tileSetContains(tile2)) {
      val setOfTile1 = setWithTile(tile1).getOrElse(throw new IllegalAccessError("Could not find tile 1"))
      val setOfTile2 = setWithTile(tile2).getOrElse(throw new IllegalAccessError("Could not find tile 2"))
      return copy(sets = removeEmptySets(sets - setOfTile1 + (setOfTile1 - tile1) - setOfTile2 + (setOfTile2 + tile1)))
    }
    this
  }

  private[model] def removeEmptySets(set: Set[SortedSet[Tile]]): Set[SortedSet[Tile]] = set.filter(f => f.nonEmpty)

  private[model] def tileSetContains(tile: Tile): Boolean = sets.exists(_.contains(tile)) /*t*/

  private[model] def setWithTile(tile: Tile): Option[SortedSet[Tile]] = sets.find(set => set.contains(tile)) /*t*/

  def putDownTile(player: Player, tile: Tile): Desk = {
    val foundPlayer = players.find(_ == player).getOrElse(throw new IllegalArgumentException("Could not find the player tat puts the tile on the table"))
    copy(players = players - foundPlayer + (foundPlayer - tile), sets = sets + (SortedSet[Tile]() + tile))
  } /*t*/

  def takeTile(player: Player): Desk = {
    val tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))
    copy(players = players - player + (players.find(_ == player).getOrElse(throw new IllegalArgumentException("The Player does not exist!")) + tile), bagOfTiles = bagOfTiles - tile)
  } /*t*/

  def switchToNextPlayer(curr: Player, next: Player): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).getOrElse(throw new IllegalArgumentException("Could not set current player on WAIT!")).changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).getOrElse(throw new IllegalArgumentException("Could not set next Player on TURN!")).changeState(State.TURN))
  } /*t*/

  def addPlayer(p: Player): Desk = copy(players = players + p) /*t*/

  def hasMoreThan1Player: Boolean = players.size >= 2 /*t*/

  def hasLessThan4Players: Boolean = players.size < 4 /*t*/

  def hasCorrectAmountOfPlayers: Boolean = players.size >= 2 && players.size <= 4

  def setsOnDeskAreCorrect: Boolean = !sets.exists(set => set.size < 3)

  def findPlayer(playerNumber: Int): Option[Player] = players.find(_.number == playerNumber)
}
