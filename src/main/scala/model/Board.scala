package model

import scala.collection.SortedSet


/*
* Was ist besser: tiles etwas hinzufügen und dann zurückgeben
* oder tiles als var un neues Objet erzeugen, das einhänge und das zurückgeben
*/

case class Board(tiles: SortedSet[Tile]) {
  def isEmpty(): Boolean = tiles.isEmpty


  def amountOfTiles(): Int = tiles.size

  def +(tile: Tile): Board = Board(tiles + tile)

  def -(tile: Tile): Board = Board(tiles.filterNot(t => tile.identifier == t.identifier))

  def contains(tile: Tile): Boolean = tiles contains tile
}
