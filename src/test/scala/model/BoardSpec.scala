package model

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {


  "A Board" when {
    "created" should {
      val set = Set[Tile]()
      val board = Board(set)
      "have 0 tiles" in {
        board.tiles.isEmpty should be(true)
      }
    }
    "created with 1" should {
      val tile = Tile(1, Color.RED, 0)
      val set = Set[Tile](tile)
      val board = Board(set)
      "have 1 tiles" in {
        board.contains(tile) should be(true)
        board.tiles.isEmpty should be(false)
        board.tiles.size should be(1)
      }
    }
    "gets added a tile" should {
      val set = Set[Tile]()
      var board = Board(set)
      val tile = Tile(1, Color.RED, 0)
      board = board.add(tile)
      "have 1 tiles" in {
        var opt = board.tiles.find(t => tile.identifier == t.identifier)
        if (opt.isDefined) {
          board.size() should be(1)
          board.contains(tile) should be(true)
          (opt.get.identifier == tile.identifier) should be(true)
        } else {
          1 should be(0)
        }
      }
    }
    "gets removed a tile" should {
      val tile = Tile(1, Color.RED, 0)
      val set = Set[Tile](tile)
      var board = Board(set)
      board.size() should be(1)
      board.contains(tile) should be(true)

      board = board.remove(tile)
      "have 0 tiles" in {
        var opt = board.tiles.find(t => tile.identifier == t.identifier)
        if (opt.isEmpty) {
          board.contains(tile) should be(false)
          board.size() should be(0)
        } else {
          1 should be(0)
        }
      }
    }
  }
}
