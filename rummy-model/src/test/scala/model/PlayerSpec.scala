package model

import model.deskComp.deskBaseImpl.TileInterface
import model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, Tile}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.SortedSet

class PlayerSpec() extends WordSpec with Matchers {
  "A Player" when {
      val player = Player("John",Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))))
    "created with one tile on board" should {
      "have a name" in {
        player.name should be("John")
      }
      "have a nice String representation" in {
        player.toString should be("Player John, boardsize: 1")
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
  }
}
