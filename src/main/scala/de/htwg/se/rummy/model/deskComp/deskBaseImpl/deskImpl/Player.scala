package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.State.state

import scala.collection.immutable.SortedSet

case class Player(name: String, number: Int, board: Board, state: state = State.WAIT) {

 def won(): Boolean = board.isEmpty

 def -(tile: Tile): Player = copy(board = board - tile)

 def +(tile: Tile): Player = copy(board = board + tile)

 def changeState(newState: State.Value): Player = copy(state = newState)

 override def toString: String = "Player " + number + ": " + number + " with state: " + state.toString + " boardsize: " + board.amountOfTiles()

 def hasTile(tile: Tile): Boolean = board.contains(tile)

 def tiles: SortedSet[Tile] = board.tiles
}
