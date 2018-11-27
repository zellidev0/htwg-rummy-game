package model

import model.State.state


case class Player(name: String, number: Int, board: Board, state: state = State.WAIT) {

  override def toString: String = "Player " + number + ": " + number + " with state: " + state.toString + " boardsize: " + board.amountOfTiles()

  def takeFromBoard(tile: Tile): Player = copy(board = board.remove(tile))

  def addToBoard(tile: Tile): Player = copy(board = board.add(tile))

  def changeState(newState: State.Value): Player = this.copy(state = newState)
}
