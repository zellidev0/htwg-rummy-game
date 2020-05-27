package model.deskComp.deskBaseImpl

import model.DeskInterface

import scala.collection.immutable.SortedSet

case class Desk(players: List[PlayerInterface], bagOfTiles: Set[TileInterface], table: Set[SortedSet[TileInterface]])
    extends DeskInterface {
  val minSize = 3

  override def tryToMoveTwoTilesOnDesk(t1: TileInterface, t2: TileInterface): Option[Desk] =
    if (tableContains(t1) && tableContains(t2)) Some(moveTwoTilesOnDesk(t1, t2)) else None

  override def getTileFromBag: Option[TileInterface] =
    bagOfTiles.headOption

  override def addPlayer(p: PlayerInterface): Desk =
    copy(players = players :+ p)

  override def switchToNextPlayer: DeskInterface =
    copy((players :+ players.head).drop(1))

  override def getCurrentPlayer: PlayerInterface =
    players.head

  override def takeTileFromBagToPlayer(p: PlayerInterface, t: TileInterface): Desk =
    addToPlayer(p, t).removeFromBag(t)

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

  override def getPlayerByName(name: String): Option[PlayerInterface] =
    players.find(_.name == name)

  override def putDownTile(p: PlayerInterface, t: TileInterface): Desk =
    addToTable(t).removeTileFromPlayer(t, p)

  override def amountOfPlayers: Int =
    players.size

  override def checkTable(): Boolean =
    true //todo change back in production to => table.forall(set => checkStreet(set) || checkPair(set))

  private[model] def checkStreet(set: SortedSet[TileInterface]): Boolean =
    set.size >= minSize && allTilesHaveSameColor(set) && allTilesHaveValidStreetValues(set)

  private[model] def addToPlayer(p: PlayerInterface, t: TileInterface): Desk =
    replacePlayer(oldPlayer = p, newPlayer = p add t)

  private[model] def replacePlayer(oldPlayer: PlayerInterface, newPlayer: PlayerInterface): Desk =
    copy(players = players.filterNot(_ == oldPlayer) :+ newPlayer)

  private[model] def allTilesHaveSameColor(set: Set[TileInterface]): Boolean =
    set.nonEmpty && set.forall(tile => tile.color == set.head.color) && !allTilesHaveDifferentColor(set)

  private[model] def allTilesHaveDifferentColor(set: Set[TileInterface]): Boolean =
    set.map(tile => tile.color).size == set.size

  private[model] def allTilesHaveValidStreetValues(set: SortedSet[TileInterface]): Boolean =
    set.map(tile => tile.value).size == set.size &&
    set.map(tile => tile.value).last - set.map(tile => tile.value).head == set.size - 1

  private[model] def checkPair(set: SortedSet[TileInterface]): Boolean =
    correctPairSize(set) && allTilesHaveSameValues(set) && allTilesHaveDifferentColor(set)

  private[model] def correctPairSize(set: Set[TileInterface]): Boolean =
    set.size == minSize || set.size == 4

  private[model] def allTilesHaveSameValues(set: Set[TileInterface]): Boolean =
    set.nonEmpty && set.forall(tile => tile.value == set.head.value) && !allTilesHaveDifferentValues(set)

  private[model] def allTilesHaveDifferentValues(set: Set[TileInterface]): Boolean =
    set.map(tile => tile.value).size == set.size

  private def moveTwoTilesOnDesk(t1: TileInterface, t2: TileInterface): Desk =
    copy(
      table = table
        .map(sortedSet => if (sortedSet.contains(t1)) sortedSet - t1 else sortedSet)
        .map(sortedSet => if (sortedSet.contains(t2)) sortedSet + t1 else sortedSet)
        .filter(sortedSet => sortedSet.nonEmpty))

  private[model] def removeTileFromPlayer(t: TileInterface, p: PlayerInterface): Desk =
    replacePlayer(oldPlayer = p, newPlayer = getPlayerByName(p.name).get remove t)

  private[model] def moreThan1P: Boolean =
    players.size >= 2

  private[model] def addToTable(t: TileInterface): Desk =
    copy(table = table + (SortedSet[TileInterface]() + t))

  private[model] def removeFromBag(t: TileInterface): Desk =
    copy(bagOfTiles = bagOfTiles - t)

  private[model] def tableContains(t: TileInterface): Boolean =
    table.exists(sortedSet => sortedSet.exists(t1 => t1 == t)) // leave like that, otherwise it fails

}
