package model

import org.scalatest._

class PlayerSpec() extends WordSpec with Matchers {
  "A Player" when {
    "created" should {
      val set = Set[Tile]()
      val board = Board(set)
      val player = Player("Name1", 1, board)
      "have a name" in {
        player.name should be("Name1")
      }
      "have a nice String representation" in {
        player.toString should be("Name1")
      }
      "have a player number" in {
        player.number should be(1)
      }
      "have the status Wait" in {
        player.status should be(Status.WAIT)
      }
    }
    "takes a Tile" should {
      val set = Set[Tile]()
      val board = Board(set)
      var player = Player("Name1", 1, board)
      player.board.size() should be(0)
      player = player.fromBagToBoard(Tile(1, Color.RED, 0))
      "have one tile in board" in {
        player.board.size() should be(1)
      }

    }
    "lays down a Tile" should {
      val set = Set[Tile](Tile(1, Color.RED, 0))
      val board = Board(set)
      var player = Player("Name1", 1, board)
      player.board.size() should be(1)
      player = player.fromBoardToTable(Tile(1, Color.RED, 0))
      "have one tile in board" in {
        player.board.size() should be(0)
      }
    }
  }
}
