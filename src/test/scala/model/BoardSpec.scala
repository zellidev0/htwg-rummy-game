package model

import org.scalatest.{Matchers, WordSpec}

import scala.collection.SortedSet

class BoardSpec extends WordSpec with Matchers {


  "A Board" when {
    "created" should {
      "have 0 tiles" in {
        Board(SortedSet[Tile]()).tiles.isEmpty should be(true)
      }
    }
    "created with 1" should {
      val tile = Tile(1, Color.RED, 0)
      val set = SortedSet[Tile](tile)
      val board = Board(set)
      "have 1 tile" in {
        board.contains(tile) should be(true)
        board.tiles.isEmpty should be(false)
        board.tiles.size should be(1)
      }
    }
    "gets added a tile" should {
      var board = Board(SortedSet[Tile]())
      val tile = Tile(1, Color.RED, 0)
      board = board + tile
      "have 1 more tile" in {
        val opt = board.tiles.find(t => tile.identifier == t.identifier)
        if (opt.isDefined) {
          board.amountOfTiles() should be(1)
          board.contains(tile) should be(true)
          (opt.get.identifier == tile.identifier) should be(true)
        } else {
          1 should be(0)
        }
      }
    }
    "gets removed a tile" should {
      val tile = Tile(1, Color.RED, 0)
      var board = Board(SortedSet[Tile](tile))
      board.amountOfTiles() should be(1)
      board.contains(tile) should be(true)
      board = board.-(tile)
      "have 0 tiles" in {
        var opt = board.tiles.find(t => tile.identifier == t.identifier)
        if (opt.isEmpty) {
          board.contains(tile) should be(false)
          board.amountOfTiles() should be(0)
        } else {
          1 should be(0)
        }
      }
    }
  }
}
