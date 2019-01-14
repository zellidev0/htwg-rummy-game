package model.deskComp.deskBaseImpl

import model.deskComp.deskBaseImpl.deskImpl.State
import model.deskComp.deskBaseImpl.deskImpl.State.state

import scala.collection.SortedSet


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
