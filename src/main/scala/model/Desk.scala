package model

import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], tileTable: Set[Tile]) {

  def layDownTileOnTable(player: Player, tile: Tile): Desk = {
    var playerInSetOptional = players.find(p => p == player)
    playerInSetOptional match {
      case Some(value) => copy(players = players.-(value).+(value.takeFromBoard(tile)), tileTable = tileTable + tile)
      case None => throw new IllegalArgumentException("wrong argument")
    }
  }

  def takeTileFromBag(player: Player): Desk = {
    val tile = getRandomTile
    var playerInSetOptional = players.find(p => p == player)
    playerInSetOptional match {
      case Some(value) => {
        val newBagOfTiles = bagOfTiles - tile
        val newPlayer = value.addToBoard(tile)
        val newPlayerArray = players.-(value)
        val newPlayerarray2 = newPlayerArray.+(newPlayer)
        copy(players = newPlayerarray2, bagOfTiles = newBagOfTiles)
      }
      case None => throw new IllegalArgumentException("wrong argument")
    }
  }

  private def getRandomTile: Tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))

  def switchToNextPlayer(current: Player, next: Player): Desk = {


    var currentFoundnd = players.find(p => p == current)
    var newPlayers = Set[Player]()
    currentFoundnd match {
      case Some(value) =>
        var newPlayer = value.changeState(State.WAIT)
        var x = players.-(value)
        var y = x.+(newPlayer)
        newPlayers = y
      case None => throw new IllegalArgumentException("wrong argument")
    }
    var nextFoundnd = newPlayers.find(p => p == next)

    nextFoundnd match {
      case Some(value) =>
        var newPlayer = value.changeState(State.TURN)
        var x = newPlayers.-(value)
        var y = x.+(newPlayer)
        newPlayers = y
      case None => throw new IllegalArgumentException("wrong argument")
    }
    copy(players = newPlayers)
  }

  def addPlayers(player: Player): Desk = copy(players = players.+(player))

  def hasEnoughPlayers: Boolean = players.size >= 2 && players.size <= 4

  def hasStarted: Boolean = {
    for (player <- players) {
      if (player.board.amountOfTiles() != 0) {
        true
      }
    }
    false
  }
}
