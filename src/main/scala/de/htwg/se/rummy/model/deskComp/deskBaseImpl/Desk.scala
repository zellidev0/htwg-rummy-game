package de.htwg.se.rummy.model.deskComp.deskBaseImpl

import de.htwg.se.rummy.model.DeskInterface
import scala.collection.immutable.SortedSet

case class Desk(players: List[PlayerInterface], bagOfTiles: Set[TileInterface], table: Set[SortedSet[TileInterface]])
  extends DeskInterface {
  val minSize = 3

  override def getPreviousPlayer: PlayerInterface = {
    val index = players.indexOf(getCurrentPlayer)
    if ((index - 1) < 0) players.last else players(index - 1)
  }

  override def getNextPlayer: PlayerInterface =
    players size match {
      case num if Range(2, 5) contains num => players(((players indexOf getCurrentPlayer) + 1) % num)
      case _ => throw new RuntimeException("Less than 2 or more than 4 player in the game")
    }

  override def getCurrentPlayer: PlayerInterface =
    players.find(p => p.hasTurn).get

  override def getPlayerByName(name: String): Option[PlayerInterface] =
    players.find(_.name == name)

  override def moveTwoTilesOnDesk(t1: TileInterface, t2: TileInterface): Desk = {
    if (tableContains(t1) && tableContains(t2)) {
      val setOfT1 = table.find(_.contains(t1)).get
      val setOfT2 = table.find(_.contains(t2)).get
      if (!setOfT1.equals(setOfT2)) {
        val setOfT1WithoutT1 = setOfT1 - t1
        val setOfT2WithT1 = setOfT2 + t1
        var tableWithoutAny = table - setOfT1
        tableWithoutAny = tableWithoutAny - setOfT2
        tableWithoutAny = tableWithoutAny + setOfT1WithoutT1
        tableWithoutAny = tableWithoutAny + setOfT2WithT1
        return copy(table = tableWithoutAny.filter(elements => elements.nonEmpty))
      }
    }
    this
  }

  override def tableContains(t: TileInterface): Boolean =
    table.exists(sortedSet => sortedSet.exists(t1 => t1 == t))// leave like that, otherwise it fails

  override def getTileFromBag: TileInterface =
    bagOfTiles.head

  override def addPlayer(p: PlayerInterface): Desk =
    copy(players = players :+ p)

  override def switchToNextPlayer: Desk =
    replacePlayer(getCurrentPlayer, getCurrentPlayer change (turn = false))
      .replacePlayer(getNextPlayer, getNextPlayer change (turn = true))

  override def switchToPreviousPlayer: Desk =
    replacePlayer(getCurrentPlayer, getCurrentPlayer change (turn = false))
      .replacePlayer(getPreviousPlayer, getPreviousPlayer change (turn = true))

  override def removePlayer(p: PlayerInterface): Desk =
    copy(players = players.filterNot(pl => pl == p))

  override def takeTileFromBagToPlayer(p: PlayerInterface, t: TileInterface): Desk =
    addToPlayer(p, t).removeFromBag(t)

  override def takeTileFromPlayerToBag(p: PlayerInterface, t: TileInterface): Desk =
    removeTileFromPlayer(t, p).addToBag(t)

  override def lessThan4P: Boolean =
    players.size < 4

  override def correctAmountOfPlayers: Boolean =
    moreThan1P && players.size <= 4

  override def currentPlayerWon(): Boolean =
    getCurrentPlayer.won()

  override def boardView: SortedSet[TileInterface] =
    getCurrentPlayer.tiles

  override def tableView: Set[SortedSet[TileInterface]] =
    table

  override def takeUpTile(p: PlayerInterface, t: TileInterface): Desk =
    removeFromTable(t).addToPlayer(p, t)

  override def removeFromTable(t: TileInterface): Desk =
    copy(table = table - table.find(_.contains(t)).get + (table.find(_.contains(t)).get - t))

  override def putDownTile(p: PlayerInterface, t: TileInterface): Desk =
    addToTable(t).removeTileFromPlayer(t, p)

  override def amountOfPlayers: Int =
    players.size

  override def checkTable(): Boolean =
    true //table.forall(set => checkStreet(set) || checkPair(set))


  private[model] def addToTable(t: TileInterface): Desk =
    copy(table = table + (SortedSet[TileInterface]() + t))

  private[model] def addToPlayer(p: PlayerInterface, t: TileInterface): Desk = {
    replacePlayer(oldPlayer = p, newPlayer = getPlayerByName(p.name).get add t)
  }

  private[model] def removeTileFromPlayer(t: TileInterface, p: PlayerInterface): Desk =
    replacePlayer(oldPlayer = p, newPlayer = getPlayerByName(p.name).get remove t)

  private[model] def checkStreet(set: SortedSet[TileInterface]): Boolean =
    set.size >= minSize && allTilesHaveSameColor(set) && allTilesHaveValidStreetValues(set)

  private[model] def checkPair(set: SortedSet[TileInterface]): Boolean =
    correctPairSize(set) && allTilesHaveSameValues(set) && allTilesHaveDifferentColor(set)

  private[model] def replacePlayer(oldPlayer: PlayerInterface, newPlayer: PlayerInterface): Desk =
    copy(players = players.filterNot(_ == oldPlayer) :+ newPlayer)

  private[model] def correctPairSize(set: Set[TileInterface]): Boolean =
    set.size == minSize || set.size == 4

  private[model] def moreThan1P: Boolean =
    players.size >= 2

  private[model] def addToBag(t: TileInterface): Desk =
    copy(bagOfTiles = bagOfTiles + t)

  private[model] def removeFromBag(t: TileInterface): Desk =
    copy(bagOfTiles = bagOfTiles - t)

  private[model] def allTilesHaveSameColor(set: Set[TileInterface]): Boolean =
    set.nonEmpty && set.forall(tile => tile.color == set.head.color) && !allTilesHaveDifferentColor(set)

  private[model] def allTilesHaveDifferentColor(set: Set[TileInterface]): Boolean =
    set.map(tile => tile.color).size == set.size

  private[model] def allTilesHaveSameValues(set: Set[TileInterface]): Boolean =
    set.nonEmpty && set.forall(tile => tile.value == set.head.value) && !allTilesHaveDifferentValues(set)

  private[model] def allTilesHaveDifferentValues(set: Set[TileInterface]): Boolean =
    set.map(tile => tile.value).size == set.size

  private[model] def allTilesHaveValidStreetValues(set: SortedSet[TileInterface]): Boolean =
    set.map(tile => tile.value).size == set.size &&
      set.map(tile => tile.value).last - set.map(tile => tile.value).head == set.size - 1


}
