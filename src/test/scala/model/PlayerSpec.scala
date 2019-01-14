package model

import model.deskComp.deskBaseImpl._
import model.deskComp.deskBaseImpl.deskImpl._
import org.scalatest._

import scala.collection.SortedSet

class PlayerSpec() extends WordSpec with Matchers {
  "A Player" when {
    "created" should {
      val player = Player("Name1", 1, Board(SortedSet[TileInterface]()))
      "have a name" in {
        player.name should be("Name1")
      }
      "have a nice String representation" in {
        player.toString should be("Player 1: 1 with state: WAIT boardsize: 0")
      }
      "have a player number" in {
        player.number should be(1)
      }
      "have the status Wait" in {
        player.state should be(State.WAIT)
      }
    }
    "takes a Tile" should {
      var player: PlayerInterface = Player("Name1", 1, Board(SortedSet[TileInterface]()))
      player.tiles.size should be(0)
      player = player + Tile(1, Color.RED, 0)
      "have one tile in board" in {
        player.tiles.size should be(1)
      }

    }
    "lays down a Tile" should {
      var player: PlayerInterface = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))))
      player.tiles.size should be(1)
      player = player.-(Tile(1, Color.RED, 0))
      "have one tile in board" in {
        player.tiles.size should be(0)
      }
    }
    "its status is changed" should {
      var player1: PlayerInterface = Player("Name1", 1, Board(SortedSet[TileInterface]()), State.TURN)
      player1 = player1.changeState(State.WAIT)
      "the new status be turn " in {
        player1.tiles.size should be(0)
      }
    }
    "its board is empty" should {
      val player1: PlayerInterface = Player("Name1", 1, Board(SortedSet[TileInterface]()), State.TURN)
      "the new status be turn " in {
        player1.won() should be(true)
      }
    }
    "has tile" should {
      val tile = Tile(1, Color.RED, 0)
      val player1 = Player("Name1", 1, Board(SortedSet[TileInterface](tile)), State.TURN)
      "have the tile" in {
        player1.hasTile(tile) should be(true)
      }
    }
  }
}
