package model

import model.Color.Color

import scala.collection.SortedSet
import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], sets: Set[SortedSet[Tile]]) {

  def currentPlayerWon(): Boolean = currentP.won() /*t*/

  def checkTable(): Boolean = sets.forall(set => checkStreet(set) || checkPair(set)) /*t*/

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
    set.foreach(t => setOfValues += t.value)
    set.foreach(t => setOfColors += t.color)
    if (setOfValues.size != 1 || setOfColors.size != set.size) return false
    true
  } /*t*/

  def amountOfPlayers: Int = players.size /*t*/

  def amountOfTilesOnTable: Int = {
    var x = 0
    sets.foreach(sorted => x += sorted.size)
    x
  } /*t*/

  def nextP: Player = if (currentP.number + 1 == players.size) players.find(_.number == 0).get else players.find(_.number == currentP.number + 1).get /*t*/

  def currentP: Player = players.find(_.state == State.TURN).get /*t*/

  def moveTwoTilesOnDesk(current: Player, tile1: Tile, tile2: Tile): Desk = if (setContains(tile1) && setContains(tile2)) copy(sets = removeEmptySets(sets - setWithTile(tile1) + (setWithTile(tile1) - tile1) - setWithTile(tile2) + (setWithTile(tile2) + tile1))) else this

  private[model] def removeEmptySets(set: Set[SortedSet[Tile]]): Set[SortedSet[Tile]] = set.filter(f => f.nonEmpty) /*t*/

  private[model] def setContains(tile: Tile): Boolean = sets.exists(_.contains(tile)) /*t*/

  private[model] def setWithTile(tile: Tile): SortedSet[Tile] = sets.find(set => set.contains(tile)).get /*t*/

  def putDownTile(p: Player, tile: Tile): Desk = copy(players - players.find(_ == p).get + (players.find(_ == p).get - tile), sets = sets + (SortedSet[Tile]() + tile)) /*t*/

  def takeTile(player: Player): Desk = {
    val tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))
    copy(players = players - player + (players.find(_ == player).get + tile), bagOfTiles = bagOfTiles - tile)
  } /*t*/

  def switchToNextPlayer(curr: Player, next: Player): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).get.changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).get.changeState(State.TURN))
  } /*t*/

  def addPlayer(p: Player): Desk = copy(players = players + p) /*t*/

  def hasMoreThan1Player: Boolean = players.size >= 2 /*t*/

  def hasLessThan4Players: Boolean = players.size < 4 /*t*/

  def hasCorrectAmountOfPlayers: Boolean = players.size >= 2 && players.size <= 4 /*t*/

}
