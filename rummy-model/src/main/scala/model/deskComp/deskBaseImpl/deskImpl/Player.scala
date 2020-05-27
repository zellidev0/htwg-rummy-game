package model.deskComp.deskBaseImpl.deskImpl

import model.deskComp.deskBaseImpl.{BoardInterface, PlayerInterface, TileInterface}

import scala.collection.immutable.SortedSet

case class Player (name: String, board: BoardInterface) extends PlayerInterface {

  override def won(): Boolean =
    board.isEmpty

  override def remove(tile: TileInterface): PlayerInterface =
    copy(board = board remove tile)

  override def add(tile: TileInterface): PlayerInterface =
    copy(board = board add tile)

  override def has(tile: TileInterface): Boolean =
    board.contains(tile)

  override def tiles: SortedSet[TileInterface] =
    board.tiles

  override def toString: String =
    s"Player $name, boardsize: ${board.amountOfTiles()}"
}
