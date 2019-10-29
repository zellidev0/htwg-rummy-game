package de.htwg.se.rummy.model.deskComp.deskBaseImpl

import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Color, State}
import scala.collection.immutable.SortedSet
import scala.util.Random

case class Desk(players: Set[PlayerInterface], bagOfTiles: Set[TileInterface], table: Set[SortedSet[TileInterface]]) extends DeskInterface {
  val minSize = 3

  override def getPreviousPlayer: PlayerInterface = if (getCurrentPlayer.number - 1 < 0) players.find(_.number == players.size - 1).get else players.find(_.number == getCurrentPlayer.number - 1).get

  override def getCurrentPlayer: PlayerInterface = players.find(_.state == State.TURN).get

  override def getNextPlayer: PlayerInterface = if (getCurrentPlayer.number + 1 == players.size) players.find(_.number == 0).get else players.find(_.number == getCurrentPlayer.number + 1).get

  override def moveTwoTilesOnDesk(t1: TileInterface, t2: TileInterface): Desk = if (tableContains(t1) && tableContains(t2)) copy(table = (table - table.find(_.contains(t1)).get + (table.find(_.contains(t1)).get - t1) - table.find(_.contains(t2)).get + (table.find(_.contains(t2)).get + t1)).filter(_.nonEmpty)) else this

  override def tableContains(t: TileInterface): Boolean = table.exists(_.contains(t))

  override def getRandomTileInBag: TileInterface = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))

  override def addPlayer(p: PlayerInterface): Desk = copy(players = players + p)

  override def switchToNextPlayer(curr: PlayerInterface, next: PlayerInterface): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).get.changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).get.changeState(State.TURN))
  }

  override def removePlayer(p: PlayerInterface): Desk = copy(players - players.find(_.number == p.number).get)

  override def takeTileFromBagToPlayer(p: PlayerInterface, t: TileInterface): Desk = addToPlayer(p, t).removeFromBag(t)

  override def takeTileFromPlayerToBag(p: PlayerInterface, t: TileInterface): Desk = removeFromPlayer(p, t).addToBag(t)

  override def lessThan4P: Boolean = players.size < 4

  override def correctAmountOfPlayers: Boolean = moreThan1P && players.size <= 4

  override def currentPlayerWon(): Boolean = getCurrentPlayer.won()

  override def viewOfCurrentPlayersBoard: SortedSet[TileInterface] = getCurrentPlayer.tiles

  override def viewOfTable: Set[SortedSet[TileInterface]] = table

  override def takeUpTile(p: PlayerInterface, t: TileInterface): Desk = removeFromTable(t).addToPlayer(p, t)

  override def removeFromTable(t: TileInterface): Desk = copy(table = table - table.find(_.contains(t)).get + (table.find(_.contains(t)).get - t))

  override def putDownTile(p: PlayerInterface, t: TileInterface): Desk = addToTable(t).removeFromPlayer(p, t)

  override def amountOfPlayers: Int = players.size

  override def checkTable(): Boolean = table.forall(set => checkStreet(set) || checkPair(set))

  private[model] def addToTable(t: TileInterface): Desk = copy(table = table + (SortedSet[TileInterface]() + t))

  private[model] def addToPlayer(p: PlayerInterface, t: TileInterface): Desk = copy(players - p + (players.find(_ == p).get + t))

  private[model] def removeFromPlayer(p: PlayerInterface, t: TileInterface): Desk = copy(players = players - p + (players.find(_ == p).get - t))

  private[model] def checkStreet(set: SortedSet[TileInterface]): Boolean = {
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

  private[model] def checkPair(set: SortedSet[TileInterface]): Boolean = {
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

  private[model] def addToBag(t: TileInterface): Desk = copy(bagOfTiles = bagOfTiles + t)

  private[model] def removeFromBag(t: TileInterface): Desk = copy(bagOfTiles = bagOfTiles - t)
}
