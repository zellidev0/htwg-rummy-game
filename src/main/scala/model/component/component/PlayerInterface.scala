package model.component.component

import model.component.component.component.State

import scala.collection.SortedSet

trait PlayerInterface {

  def won(): Boolean
  def -(tile: TileInterface): PlayerInterface
  def +(tile: TileInterface): PlayerInterface
  def changeState(newState: State.Value): PlayerInterface
  override def toString: String
  def hasTile(tile: TileInterface): Boolean
  def getNumber: Int
  def getState: State.Value
  def getName: String
  def getTiles: SortedSet[TileInterface]
}
