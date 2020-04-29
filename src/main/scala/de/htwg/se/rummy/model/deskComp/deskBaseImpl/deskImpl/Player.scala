package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

import com.google.inject.Inject
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{BoardInterface, PlayerInterface, TileInterface}

import scala.collection.immutable.SortedSet

case class Player @Inject()(name: String, board: BoardInterface, hasTurn: Boolean = false) extends PlayerInterface {

  override def won(): Boolean =
    board.isEmpty

  override def remove(tile: TileInterface): PlayerInterface =
    copy(board = board remove tile)

  override def add(tile: TileInterface): PlayerInterface =
    copy(board = board add tile)

  override def change(turn:Boolean): PlayerInterface =
    copy(hasTurn = turn)

  override def toString: String =
    s"Player $name, turn: ${hasTurn}, boardsize: ${board.amountOfTiles()}"

  override def has(tile: TileInterface): Boolean =
    board.contains(tile)

  override def tiles: SortedSet[TileInterface] =
    board.tiles
}
