package model

import model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Tile}
import model.deskComp.deskBaseImpl.{BoardInterface, TileInterface}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.SortedSet

class BoardSpec extends WordSpec with Matchers {


  "A Board" when {
    "created" should {
      val set = SortedSet[TileInterface](Tile(1, Color.RED, 0))
      val board = Board(tiles = set)
      "have 0 tiles" in {
        board.isEmpty should be(false)
      }
    }
    "created with 1" should {
      val tile = Tile(1, Color.RED, 0)
      val set = SortedSet[TileInterface](tile)
      val board = Board(set)
      "have 1 tile" in {
        board.contains(tile) should be(true)
        board.isEmpty should be(false)
        board.amountOfTiles() should be(1)
      }
    }
    "gets added a tile" should {
      var board: BoardInterface = Board(SortedSet[TileInterface]())
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
      var board: BoardInterface = Board(SortedSet[TileInterface](tile))
      board.amountOfTiles() should be(1)
      board.contains(tile) should be(true)
      board = board - tile
      "have 0 tiles" in {
        val opt = board.tiles.find(t => tile.identifier == t.identifier)
        if (opt.isEmpty) {
          board.contains(tile) should be(false)
          board.amountOfTiles() should be(0)
        } else {
          1 should be(0)
        }
      }
    }
    "checked if empty" should {
      val tile = Tile(1, Color.RED, 0)
      val board: BoardInterface = Board(SortedSet[TileInterface]())
      "should be" in {
        board.isEmpty should be(true)
        board.amountOfTiles() should be(0)
      }
    }
  }
}
