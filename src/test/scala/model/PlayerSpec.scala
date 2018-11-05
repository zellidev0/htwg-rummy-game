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
  }

}
