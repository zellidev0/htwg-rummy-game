package model

import scala.collection.SortedSet
import scala.util.Random

case class Desk(players: Set[Player], bagOfTiles: Set[Tile], tileSets: Set[SortedSet[Tile]]) {

  def size: Int = {
    var x = 0
    for (sets <- tileSets) {
      x += sets.size
    }
    x
  }


  def moveTile(tile1: Tile, tile2: Tile): Desk = {
    if (tileSetContains(tile1) && tileSetContains(tile2)) {

      var tileSetWithou1 = SortedSet[Tile]()
      var all = tileSets

      getTileSetWhereTileIsIn(tile1) match {
        case Some(value) => tileSetWithou1 = value.-(tile1)
          all = tileSets.-(value).+(tileSetWithou1)
      }
      getTileSetWhereTileIsIn(tile2) match {
        case Some(value) => val x = value.+(tile1)
          all = all.-(value).+(x)
      }
      copy(tileSets = all)
    }
    this
  }


  def tileSetContains(tile: Tile): Boolean = {
    for (set <- tileSets) {
      if (set.contains(tile)) {
        return true
      }
    }
    false
  }

  def getTileSetWhereTileIsIn(tile: Tile): Option[SortedSet[Tile]] = {
    for (set <- tileSets) {
      if (set.contains(tile)) {
        return Option(set)
      }
    }
    None
  }

  def layDownTileOnTable(player: Player, tile: Tile): Desk = {
    players.find(p => p == player) match {
      case Some(value) => copy(players = players.-(value).+(value.takeFromBoard(tile)),
        tileSets = tileSets.+(SortedSet[Tile]().+(tile)))
      case None => throw new IllegalArgumentException("Could not find the player tat puts the tile on the table")
    }
  }

  def takeTileFromBag(player: Player): Desk = {
    val tile = getRandomTile
    players.find(p => p == player) match {
      case Some(value) => copy(players = players.-(value).+(value.addToBoard(tile)), bagOfTiles = bagOfTiles - tile)
      case None => throw new IllegalArgumentException("The Player does not exist!")
    }
  }


  def getRandomTile: Tile = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))



  def switchToNextPlayer(current: Player, next: Player): Desk = {

    var newPlayers = Set[Player]()
    players.find(p => p == current) match {
      case Some(value) => newPlayers = players.-(value).+(value.changeState(State.WAIT))
      case None => throw new IllegalArgumentException("Could not set current player on WAIT!")
    }
    newPlayers.find(p => p == next) match {
      case Some(value) => newPlayers = newPlayers.-(value).+(value.changeState(State.TURN))
      case None => throw new IllegalArgumentException("Could not set next Player on TURN!")
    }
    copy(players = newPlayers)
  }

  def addPlayers(player: Player): Desk = copy(players = players.+(player))

  def hasCorrectAmountOfPlayers: Boolean = players.size >= 2 && players.size + 1 <= 4

}
