package de.htwg.se.rummy.model.deskComp.deskBaseImpl

import scala.collection.immutable.SortedSet


trait PlayerInterface {

  val name: String
  val board: BoardInterface
  val hasTurn: Boolean

  def won(): Boolean
  def remove(tile: TileInterface): PlayerInterface
  def add(tile: TileInterface): PlayerInterface
  def change(turn: Boolean): PlayerInterface
  def has(tile: TileInterface): Boolean
  def tiles: SortedSet[TileInterface]
}
