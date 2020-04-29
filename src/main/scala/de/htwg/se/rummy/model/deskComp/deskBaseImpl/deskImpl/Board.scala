package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

import com.google.inject.Inject
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{BoardInterface, TileInterface}

import scala.collection.immutable.SortedSet

case class Board @Inject()(tiles: SortedSet[TileInterface]) extends BoardInterface {

  override def isEmpty: Boolean =
    tiles.isEmpty

  override def amountOfTiles(): Int =
    tiles.size

  override def add(tile: TileInterface): BoardInterface =
    copy(tiles + tile)

  override def remove(tile: TileInterface): BoardInterface =
    copy(tiles filterNot(_ == tile))

  override def contains(tile: TileInterface): Boolean =
     tiles contains tile

  //TODO sometimes when laying down this return false even though it should clearly not

}
