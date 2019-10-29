package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl


import scala.collection.immutable.SortedSet

case class Board(tiles: SortedSet[Tile]) {

  def isEmpty: Boolean = tiles.isEmpty

  def amountOfTiles(): Int = tiles.size

  def +(tile: Tile): Board = copy(tiles + tile)

  def -(tile: Tile): Board = copy(tiles - tile)

  def contains(tile: Tile): Boolean = tiles.contains(tile)

}
