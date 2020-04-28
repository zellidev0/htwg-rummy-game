package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

import com.google.inject.Inject
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.State.state
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{BoardInterface, PlayerInterface, TileInterface}

import scala.collection.immutable.SortedSet

case class Player @Inject()(name: String, number: Int, board: BoardInterface, state: state = State.WAIT) extends PlayerInterface {

  override def won(): Boolean = board.isEmpty

  override def removeTile(tile: TileInterface): PlayerInterface = copy(board = board - tile)

  override def addTile(tile: TileInterface): PlayerInterface = copy(board = board + tile)

  override def changeState(newState: State.Value): PlayerInterface = copy(state = newState)

  override def toString: String = "Player " + number + ": " + number + " with state: " + state.toString + " boardsize: " + board.amountOfTiles()

  override def hasTile(tile: TileInterface): Boolean = board.contains(tile)

  override def tiles: SortedSet[TileInterface] = board.tiles
}
