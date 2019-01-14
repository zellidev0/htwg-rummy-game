package de.htwg.se.rummy.model.deskComp.deskBaseImpl

import scala.collection.SortedSet


trait BoardInterface {
  val tiles: SortedSet[TileInterface]

  def isEmpty: Boolean

  def amountOfTiles(): Int

  def +(tile: TileInterface): BoardInterface

  def -(tile: TileInterface): BoardInterface

  def contains(tile: TileInterface): Boolean

}
