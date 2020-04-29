package de.htwg.se.rummy.model

import de.htwg.se.rummy.model.deskComp.deskBaseImpl._
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, Tile}
import org.scalatest._

import scala.collection.immutable.SortedSet

class PlayerSpec() extends WordSpec with Matchers {
  "A Player" when {
      val player = Player("John",Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))))
    "created with one tile on board" should {
      "have a name" in {
        player.name should be("John")
      }
      "have a nice String representation" in {
        player.toString should be("Player John, turn: false, boardsize: 1")
      }
      "have the turn false" in {
        player.hasTurn should be(false)
      }
    }
    "takes a Tile" should {
      val tile = Tile(2, Color.RED, 0)
      val newPlayer = player add tile
      "have a tile more on board" in {
        newPlayer.has(tile) should be(true)
      }
    }
    "lays down a Tile" should {
      val tile = Tile(1, Color.RED, 0)
      val newPlayer = player remove tile
      "have one tile less on board" in {
        newPlayer.has(tile) should be(false)
      }
    }
    "its turn status is changed" should {
      val newPlayer = player.change(true)
      "have turn " in {
        newPlayer.hasTurn should be(true)
      }
    }
  }
}
