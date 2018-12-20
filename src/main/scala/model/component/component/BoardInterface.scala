package model.component.component

import scala.collection.SortedSet


trait BoardInterface {

  def isEmpty: Boolean

  def amountOfTiles(): Int

  def +(tile: TileInterface): BoardInterface

  def -(tile: TileInterface): BoardInterface

  def contains(tile: TileInterface): Boolean

  def getTiles(): SortedSet[TileInterface]

}
