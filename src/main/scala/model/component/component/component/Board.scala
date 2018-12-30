package model.component.component.component

import model.component.component.{BoardInterface, TileInterface}

import scala.collection.SortedSet

case class Board(tiles: SortedSet[TileInterface]) extends BoardInterface {

  override def isEmpty: Boolean = tiles.isEmpty
  override def amountOfTiles(): Int = tiles.size
  override def +(tile: TileInterface): BoardInterface = copy(tiles + tile)
  override def -(tile: TileInterface): BoardInterface = copy(tiles.filterNot(t => tile.identifier == t.identifier))
  override def contains(tile: TileInterface): Boolean = {
    var x = tiles.contains(tile)
    x
  }
  override def getTiles(): SortedSet[TileInterface] = tiles
}
