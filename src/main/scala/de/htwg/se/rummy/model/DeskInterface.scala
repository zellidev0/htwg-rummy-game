package de.htwg.se.rummy.model

import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}

import scala.collection.SortedSet

trait DeskInterface {

  val players: Set[PlayerInterface]
  val bagOfTiles: Set[TileInterface]
  val board: Set[SortedSet[TileInterface]]

  def takeUpTile(p: PlayerInterface, t: TileInterface): Desk
  def removeFromTable(t: TileInterface): Desk
  def putDownTile(p: PlayerInterface, t: TileInterface): Desk
  def amountOfPlayers: Int
  def checkTable(): Boolean
  def getPreviousPlayer: PlayerInterface
  def getCurrentPlayer: PlayerInterface
  def getNextPlayer: PlayerInterface
  def moveTwoTilesOnDesk(t1: TileInterface, t2: TileInterface): Desk
  def boardContains(t: TileInterface): Boolean
  def getRandomTileInBag: TileInterface
  def addPlayer(p: PlayerInterface): Desk
  def switchToNextPlayer(curr: PlayerInterface, next: PlayerInterface): Desk
  def removePlayer(p: PlayerInterface): Desk
  def takeTileFromBagToPlayer(p: PlayerInterface, t: TileInterface): Desk
  def takeTileFromPlayerToBag(p: PlayerInterface, t: TileInterface): Desk
  def lessThan4P: Boolean
  def correctAmountOfPlayers: Boolean
  def currentPlayerWon(): Boolean
  def viewOfBoard: SortedSet[TileInterface]
  def viewOfSet: Set[SortedSet[TileInterface]]

}
