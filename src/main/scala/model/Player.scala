package model


case class Player(name: String, number: Int, board: Board) {
  var status: Status.Value = Status.WAIT

  override def toString: String = name

  def fromBoardToTable(tile: Tile): Player = Player(name, number, board.remove(tile))

  def fromBagToBoard(tile: Tile): Player = Player(name, number, board.add(tile))

  def changeState(newStatus: Status.Value): Player = {
    this.status = newStatus
    this.copy()
  }
}
