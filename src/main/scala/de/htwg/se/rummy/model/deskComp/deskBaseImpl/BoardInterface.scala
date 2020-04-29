package de.htwg.se.rummy.model.deskComp.deskBaseImpl

import scala.collection.immutable.SortedSet


trait BoardInterface {
  val tiles: SortedSet[TileInterface]

  def isEmpty: Boolean
  def amountOfTiles(): Int
  def add(tile: TileInterface): BoardInterface
  def remove(tile: TileInterface): BoardInterface
  def contains(tile: TileInterface): Boolean

}
