package model.component.component.component

import model.component.component.component.State.state
import model.component.component.{BoardInterface, PlayerInterface, TileInterface}

import scala.collection.SortedSet

case class Player(private val name: String, private val number: Int, private val board: BoardInterface, private val state: state = State.WAIT) extends PlayerInterface {

  override def won(): Boolean = board.isEmpty

  override def -(tile: TileInterface): PlayerInterface = copy(board = board - tile)

  override def +(tile: TileInterface): PlayerInterface = copy(board = board + tile)

  override def changeState(newState: State.Value): PlayerInterface = copy(state = newState)

  override def toString: String = "Player " + number + ": " + number + " with state: " + state.toString + " boardsize: " + board.amountOfTiles()

  override def hasTile(tile: TileInterface): Boolean = board.contains(tile)

  override def getNumber: Int = number
  override def getState: State.Value = state
  override def getName: String = name
  override def getTiles: SortedSet[TileInterface] = board.getTiles()
}
