package model

import scala.collection.SortedSet



case class Board(tiles: SortedSet[Tile]) {
  def isEmpty: Boolean = tiles.isEmpty

  def amountOfTiles(): Int = tiles.size

  def +(tile: Tile): Board = Board(tiles + tile)

  def -(tile: Tile): Board = Board(tiles.filterNot(t => tile.identifier == t.identifier))

  def contains(tile: Tile): Boolean = tiles contains tile
}
