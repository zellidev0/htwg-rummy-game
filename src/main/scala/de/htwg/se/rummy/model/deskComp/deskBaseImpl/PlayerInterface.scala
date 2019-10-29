package de.htwg.se.rummy.model.deskComp.deskBaseImpl

import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.State
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.State.state

import scala.collection.immutable.SortedSet


trait PlayerInterface {

  val name: String
  val number: Int
  val board: BoardInterface
  val state: state

  def won(): Boolean
  def -(tile: TileInterface): PlayerInterface
  def +(tile: TileInterface): PlayerInterface
  def changeState(newState: State.Value): PlayerInterface
  def hasTile(tile: TileInterface): Boolean
  def tiles: SortedSet[TileInterface]
}
