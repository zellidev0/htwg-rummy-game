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

    players.apply(players.indexWhere(p => p == current)).status = Status.WAIT
    players.apply(players.indexWhere(p => p == next)).status = Status.TURN
    this.copy(players)
  }

}
