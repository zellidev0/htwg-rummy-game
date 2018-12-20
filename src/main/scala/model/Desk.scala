package model

import scala.collection.SortedSet
import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], sets: Set[SortedSet[Tile]]) {
  val minSize = 3

  /*takeUpTile fully tested*/
  def takeUpTile(p: Player, t: Tile): Desk = removeFromTable(t).addToPlayer(p, t)

  /*removeFromTable fully tested*/
  def removeFromTable(t: Tile): Desk = copy(sets = sets - sortedSet(t) + (sortedSet(t) - t))

  /*putDownTile fully tested*/
  def putDownTile(p: Player, t: Tile): Desk = addToTable(t).removeFromPlayer(p, t)

  /*addToTable fully tested*/
  def addToTable(t: Tile): Desk = copy(sets = sets + (SortedSet[Tile]() + t))
  /*amountOfPlayers fully tested*/
  def amountOfPlayers: Int = players.size
  /*amountOfTilesOnTable fully tested*/
  def amountOfTilesOnTable: Int = {
    var x = 0
    sets.foreach(sorted => x += sorted.size)
    x
  }
  /*checkTable fully tested*/
  def checkTable(): Boolean = sets.forall(set => checkStreet(set) || checkPair(set))
  /*checkStreet fully tested*/
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
  /*checkPair fully tested*/
  def checkPair(set: SortedSet[Tile]): Boolean = {
    if (set.isEmpty || set.size < minSize || set.size > 4) {
      return false
    }
    var setOfValues = Set[Int]()
    var setOfColors = Set[Color.Value]()
    set.foreach(t => setOfValues += t.value)
    set.foreach(t => setOfColors += t.color)
    if (setOfValues.size != 1 || setOfColors.size != set.size) return false
    true
  }
  /*previousP fully tested*/
  def previousP: Player = if (currentP.number - 1 < 0) players.find(_.number == players.size - 1).get else players.find(_.number == currentP.number - 1).get
  /*currentP fully tested*/
  def currentP: Player = players.find(_.state == State.TURN).get
  /*nextP fully tested*/
  def nextP: Player = if (currentP.number + 1 == players.size) players.find(_.number == 0).get else players.find(_.number == currentP.number + 1).get
  /*moveTwoTilesOnDesk fully tested*/
  def moveTwoTilesOnDesk(t1: Tile, t2: Tile): Desk = if (setsContains(t1) && setsContains(t2)) copy(sets = (sets - sortedSet(t1) + (sortedSet(t1) - t1) - sortedSet(t2) + (sortedSet(t2) + t1)).filter(_.nonEmpty)) else this
  /*sortedSet fully tested*/
  def sortedSet(t: Tile): SortedSet[Tile] = sets.find(_.contains(t)).get
  /*setsContains fully tested*/
  def setsContains(t: Tile): Boolean = sets.exists(_.contains(t))
  /*randomTileInBag fully tested*/
  def randomTileInBag: Tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))
  /*addPlayer fully tested*/
  def addPlayer(p: Player): Desk = copy(players = players + p)


  def switchToNextPlayer(curr: Player, next: Player): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).get.changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).get.changeState(State.TURN))
  }
  /*removePlayer fully tested*/
  def removePlayer(p: Player): Desk = copy(players - players.find(_.number == p.number).get)
  /*takeTileFromBagToPlayer fully tested*/
  def takeTileFromBagToPlayer(p: Player, t: Tile): Desk = addToPlayer(p, t).removeFromBag(t)
  /*removeFromBag fully tested*/
  def removeFromBag(t: Tile): Desk = copy(bagOfTiles = bagOfTiles - t)
  /*addToPlayer fully tested*/
  def addToPlayer(p: Player, t: Tile): Desk = copy(players - p + (players.find(_ == p).get + t))
  /*takeTileFromPlayerToBag fully tested*/
  def takeTileFromPlayerToBag(p: Player, t: Tile): Desk = removeFromPlayer(p, t).addToBag(t)
  /*removeFromPlayer fully tested*/
  def removeFromPlayer(p: Player, t: Tile): Desk = copy(players = players - p + (players.find(_ == p).get - t))
  /*addToBag fully tested*/
  def addToBag(t: Tile): Desk = copy(bagOfTiles = bagOfTiles + t)

  /*lessThan4P fully tested*/
  def lessThan4P: Boolean = players.size < 4

  /*correctAmountOfPlayers fully tested*/
  def correctAmountOfPlayers: Boolean = moreThan1P && players.size <= 4

  /*moreThan1P fully tested*/
  def moreThan1P: Boolean = players.size >= 2

  /*currentPlayerWon fully tested*/
  def currentPlayerWon(): Boolean = currentP.won()


}
