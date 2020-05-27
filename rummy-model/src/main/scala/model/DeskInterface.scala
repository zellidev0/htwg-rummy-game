package model

import model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}

import scala.collection.immutable.SortedSet

trait DeskInterface {

  val players: List[PlayerInterface]
  val bagOfTiles: Set[TileInterface]
  val table: Set[SortedSet[TileInterface]]

  def tryToMoveTwoTilesOnDesk(t1: TileInterface, t2: TileInterface):  Option[DeskInterface]
  def getTileFromBag: Option[TileInterface]
  def addPlayer(p: PlayerInterface): DeskInterface
  def switchToNextPlayer: DeskInterface
  def takeTileFromBagToPlayer(p: PlayerInterface, t: TileInterface): DeskInterface
  def putDownTile(p: PlayerInterface, t: TileInterface): DeskInterface
  def amountOfPlayers: Int
  def checkTable(): Boolean
  def getCurrentPlayer: PlayerInterface
  def lessThan4P: Boolean
  def correctAmountOfPlayers: Boolean
  def currentPlayerWon(): Boolean
  def boardView: SortedSet[TileInterface]
  def tableView: Set[SortedSet[TileInterface]]
  def getPlayerByName(playerName: String): Option[PlayerInterface]
}
