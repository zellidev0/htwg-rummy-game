package model

import org.scalatest._

import scala.collection.SortedSet

class PlayerSpec() extends WordSpec with Matchers {
  "A Player" when {
    "created" should {
      val player = Player("Name1", 1, Board(SortedSet[Tile]()))
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
      var player = Player("Name1", 1, Board(SortedSet[Tile]()))
      player.board.amountOfTiles() should be(0)
      player = player.+(Tile(1, Color.RED, 0))
      "have one tile in board" in {
        player.board.amountOfTiles() should be(1)
      }

    }
    "lays down a Tile" should {
      var player = Player("Name1", 1, Board(SortedSet[Tile](Tile(1, Color.RED, 0))))
      player.board.amountOfTiles() should be(1)
      player = player.-(Tile(1, Color.RED, 0))
      "have one tile in board" in {
        player.board.amountOfTiles() should be(0)
      }
    }
    "its status is changed" should {
      var player1 = Player("Name1", 1, Board(SortedSet[Tile]()), State.TURN)
      player1 = player1.changeState(State.WAIT)
      "the new status be turn " in {
        player1.state should be(State.WAIT)
      }
    }
    "its board is empty" should {
      val player1 = Player("Name1", 1, Board(SortedSet[Tile]()), State.TURN)
      "the new status be turn " in {
        player1.won() should be(true)
      }
    }
    "has tile" should {
      val tile = Tile(1, Color.RED, 0)
      val player1 = Player("Name1", 1, Board(SortedSet[Tile](tile)), State.TURN)
      "have the tile" in {
        player1.hasTile(tile) should be(true)
      }
    }
  }
}
