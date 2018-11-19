package model

import scala.util.Random

case class Desk(players: Array[Player], bagOfTiles: Set[Tile], tileTable: Set[Tile]) {

  def layDownTileOnTable(player: Player, tile: Tile): Desk = {

    if (players.exists(p => player.number == p.number)) {
      players.update(players.indexWhere(p => player.number == p.number), player.fromBoardToTable(tile))
      Desk(players, bagOfTiles, tileTable + tile)
    } else {
      throw new IllegalArgumentException("wrong argument")
    }

  }

  def takeTileFromBag(player: Player): Desk = {
    val tile = getRandomTile()
    players.update(players.indexWhere(p => player.number == p.number), player.fromBagToBoard(tile))
    val bag = bagOfTiles - tile
    Desk(players, bag, tileTable)
  }

  private def getRandomTile(): Tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))

  def switchToNextPlayer(current: Player, next: Player): Desk = {

    val indexOfPlayer1 = players.indexWhere(p => p == current)
    val indexOfPlayer2 = players.indexWhere(p => p == next)
    val player1 = players.apply(indexOfPlayer1)
    val player2 = players.apply(indexOfPlayer2)

    val player1New = player1.changeState(State.WAIT)
    val player2New = player2.changeState(State.TURN)
    players.update(indexOfPlayer1, player1New)
    players.update(indexOfPlayer2, player2New)
    this.copy()
  }

}
