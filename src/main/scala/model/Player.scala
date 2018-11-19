package model

import State.state


case class Player(name: String, number: Int, board: Board, state: state = State.WAIT) {

  override def toString: String = name

  def fromBoardToTable(tile: Tile): Player = copy(board = board.remove(tile))

  def fromBagToBoard(tile: Tile): Player = copy(board = board.add(tile))

  def changeState(newState: State.Value): Player = this.copy(state = newState)
}
