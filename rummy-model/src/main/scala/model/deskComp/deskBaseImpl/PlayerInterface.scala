package model.deskComp.deskBaseImpl

import scala.collection.immutable.SortedSet

trait PlayerInterface {

  val name: String
  val board: BoardInterface

  def won(): Boolean
  def remove(tile: TileInterface): PlayerInterface
  def add(tile: TileInterface): PlayerInterface
  def has(tile: TileInterface): Boolean
  def tiles: SortedSet[TileInterface]
}
