package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

import com.google.inject.Inject
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{BoardInterface, TileInterface}

import scala.collection.immutable.SortedSet

case class Board @Inject()(tiles: SortedSet[TileInterface]) extends BoardInterface {

  override def isEmpty: Boolean = tiles.isEmpty

  override def amountOfTiles(): Int = tiles.size

  override def +(tile: TileInterface): BoardInterface = copy(tiles + tile)

  override def -(tile: TileInterface): BoardInterface = if (!tiles.contains(tile)) copy() else copy(tiles.filterNot(t => tile.equals(t)))

  override def contains(tile: TileInterface): Boolean = {
    val x = tiles.contains(tile)
    x //TODO sometimes when laying down this return false even though it should clearly not
  }
}
