package model

import model.State.state


case class Player(name: String, number: Int, board: Board, state: state = State.WAIT) {

  def won(): Boolean = board.isEmpty()


  override def toString: String = "Player " + number + ": " + number + " with state: " + state.toString + " boardsize: " + board.amountOfTiles()

  def -(tile: Tile): Player = copy(board = board - tile)

  def +(tile: Tile): Player = copy(board = board + tile)

  def changeState(newState: State.Value): Player = copy(state = newState)



}
