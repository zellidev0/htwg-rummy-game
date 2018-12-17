package model

import model.Color.Color

import scala.collection.SortedSet
import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], sets: Set[SortedSet[Tile]]) {
  val minSize = 3

  def takeUpTile(p: Player, t: Tile): Desk = addToPlayer(p, t).removeFromTable(t)
  def removeFromTable(t: Tile): Desk = copy(sets = sets - sortedSet(t) + (sortedSet(t) - t))
  def putDownTile(p: Player, t: Tile): Desk = addToTable(t).removeFromPlayer(p, t)
  /*t*/
  def addToTable(t: Tile): Desk = copy(sets = sets + (SortedSet[Tile]() + t))
  def removeFromPlayer(p: Player, t: Tile): Desk = copy(players - p + (players.find(_ == p).get - t))

  def amountOfPlayers: Int = players.size
  /*t*/
  def amountOfTilesOnTable: Int = {
    var x = 0
    sets.foreach(sorted => x += sorted.size)
    x
  }
  /*t*/
  def checkTable(): Boolean = sets.forall(set => checkStreet(set) || checkPair(set))
  /*t*/
  def checkStreet(set: SortedSet[Tile]): Boolean = {
    if (set.isEmpty || set.size < minSize) return false
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
  }
  /*t*/
  def checkPair(set: SortedSet[Tile]): Boolean = {
    if (set.isEmpty || set.size < minSize || set.size > 4) {
      return false
    }
    var setOfValues = Set[Int]()
    var setOfColors = Set[Color]()
    set.foreach(t => setOfValues += t.value)
    set.foreach(t => setOfColors += t.color)
    if (setOfValues.size != 1 || setOfColors.size != set.size) return false
    true
  }
  /*t*/
  def previousP: Player = if (currentP.number - 1 == 0) players.find(_.number == 0).get else if (currentP.number - 1 < 0) players.find(_.number == players.size - 1).get else players.find(_.number == currentP.number - 1).get
  /*t*/
  def nextP: Player = if (currentP.number + 1 == players.size) players.find(_.number == 0).get else players.find(_.number == currentP.number + 1).get
  /*t*/
  def currentP: Player = players.find(_.state == State.TURN).get
  def moveTwoTilesOnDesk(t1: Tile, t2: Tile): Desk = if (setsContains(t1) && setsContains(t2)) copy(sets = (sets - sortedSet(t1) + (sortedSet(t1) - t1) - sortedSet(t2) + (sortedSet(t2) + t1)).filter(_.nonEmpty)) else this
  /*t*/
  def sortedSet(t: Tile): SortedSet[Tile] = sets.find(_.contains(t)).get
  /*t*/
  def setsContains(t: Tile): Boolean = sets.exists(_.contains(t))
  /*t*/
  def randomTileInBag: Tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))
  def switchToNextPlayer(curr: Player, next: Player): Desk = {

    val newPlayers = players - curr + players.find(_ == curr).get.changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).get.changeState(State.TURN))
  }
  /*t*/
  def addPlayer(p: Player): Desk = copy(players = players + p)
  /*t*/
  def removePlayer(p: Player): Desk = copy(players - players.find(_.number == p.number).get)
  def takeTileFromBagToPlayer(p: Player, t: Tile): Desk = addToPlayer(p, t).removeFromBag(t)
  def removeFromBag(t: Tile): Desk = copy(bagOfTiles = bagOfTiles - t)
  def addToPlayer(p: Player, t: Tile): Desk = copy(players - p + (players.find(_ == p).get + t))
  /*t*/
  def takeTileFromPlayerToBag(p: Player, t: Tile): Desk = removeFromPlayer(p, t).addToBag(t)
  def addToBag(t: Tile): Desk = copy(bagOfTiles = bagOfTiles + t)
  /*t*/
  def lessThan4P: Boolean = players.size < 4
  /*t*/
  def correctAmountOfPlayers: Boolean = moreThan1P && players.size <= 4
  /*t*/
  def moreThan1P: Boolean = players.size >= 2
  def currentPlayerWon(): Boolean = currentP.won() /*t*/


}
