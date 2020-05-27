package model.deskComp.deskBaseImpl.deskImpl

import model.deskComp.deskBaseImpl.{BoardInterface, TileInterface}

import scala.collection.immutable.SortedSet

case class Board (tiles: SortedSet[TileInterface]) extends BoardInterface {

  override def isEmpty: Boolean =
    tiles.isEmpty

  override def amountOfTiles(): Int =
    tiles.size

  override def add(tile: TileInterface): BoardInterface =
    copy(tiles + tile)

  override def remove(tile: TileInterface): BoardInterface =
    copy(tiles filterNot (_ == tile))

  //TODO sometimes when laying down this return false even though it should clearly not
  override def contains(tile: TileInterface): Boolean =
    tiles contains tile

}
