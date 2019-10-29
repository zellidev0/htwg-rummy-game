package de.htwg.se.rummy.model.deskComp.deskBaseImpl

import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Color, Player, State, Tile}

import scala.collection.immutable.SortedSet
import scala.util.Random

case class Desk( players: Set[Player], bagOfTiles: Set[Tile], table: Set[SortedSet[Tile]]) {
  val minSize = 3

   def getPreviousPlayer: Player = if (getCurrentPlayer.number - 1 < 0) players.find(_.number == players.size - 1).get else players.find(_.number == getCurrentPlayer.number - 1).get

   def getCurrentPlayer: Player = players.find(_.state == State.TURN).get

   def getNextPlayer: Player = if (getCurrentPlayer.number + 1 == players.size) players.find(_.number == 0).get else players.find(_.number == getCurrentPlayer.number + 1).get

   def moveTwoTilesOnDesk(t1: Tile, t2: Tile): Desk = if (contains(t1) && contains(t2)) copy(table = (table - table.find(_.contains(t1)).get + (table.find(_.contains(t1)).get - t1) - table.find(_.contains(t2)).get + (table.find(_.contains(t2)).get + t1)).filter(_.nonEmpty)) else this

   def contains(t: Tile): Boolean = table.exists(_.contains(t))

   def getRandomTileInBag: Tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))

   def addPlayer(p: Player): Desk = copy(players = players + p)

   def switchToNextPlayer(curr: Player, next: Player): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).get.changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).get.changeState(State.TURN))
  }

   def removePlayer(p: Player): Desk = copy(players - players.find(_.number == p.number).get)

   def takeTileFromBagToPlayer(p: Player, t: Tile): Desk = addToPlayer(p, t).removeFromBag(t)

   def takeTileFromPlayerToBag(p: Player, t: Tile): Desk = removeFromPlayer(p, t).addToBag(t)

   def lessThan4P: Boolean = players.size < 4

   def correctAmountOfPlayers: Boolean = moreThan1P && players.size <= 4

   def currentPlayerWon(): Boolean = getCurrentPlayer.won()

   def viewOfCurrentPlayersBoard: SortedSet[Tile] = getCurrentPlayer.tiles

   def viewOfTable: Set[SortedSet[Tile]] = table

   def takeUpTile(p: Player, t: Tile): Desk = removeFromTable(t).addToPlayer(p, t)

   def removeFromTable(t: Tile): Desk = copy(table = table - table.find(_.contains(t)).get + (table.find(_.contains(t)).get - t))

   def putDownTile(p: Player, t: Tile): Desk = addToTable(t).removeFromPlayer(p, t)

   def amountOfPlayers: Int = players.size

   def checkTable(): Boolean = table.forall(set => checkStreet(set) || checkPair(set))

  private[model] def addToTable(t: Tile): Desk = copy(table = table + (SortedSet[Tile]() + t))

  private[model] def addToPlayer(p: Player, t: Tile): Desk = copy(players - p + (players.find(_ == p).get + t))

  private[model] def removeFromPlayer(p: Player, t: Tile): Desk = copy(players = players - p + (players.find(_ == p).get - t))

  private[model] def checkStreet(set: SortedSet[Tile]): Boolean = {
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

  private[model] def checkPair(set: SortedSet[Tile]): Boolean = {
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


  private[model] def moreThan1P: Boolean = players.size >= 2

  private[model] def addToBag(t: Tile): Desk = copy(bagOfTiles = bagOfTiles + t)

  private[model] def removeFromBag(t: Tile): Desk = copy(bagOfTiles = bagOfTiles - t)
}
