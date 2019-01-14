package model

import model.component.component.component.{Color, Tile}
import org.scalatest.{Matchers, WordSpec}

class TileSpec extends WordSpec with Matchers {

  "A tile" when {
    "created with value and color" should {
      val tile = Tile(1, Color.RED, 1)
      "have value 1 and color red and num 1" in {
        tile.getValue should be(1)
        tile.getColor should be(Color.RED)
        tile.getIdent should be(1)
        tile.identifier should be("1R1")
      }
    }
    "compares with other tile" should {
      val tile = Tile(5, Color.RED, 1)
      "be 0 if same identifier" in {
        tile.compare(Tile(5, Color.RED, 1)) should be(0)
      }
      "be 1 if higher value" in {
        tile.compare(Tile(3, Color.RED, 1)) should be(1)
      }
      "be -1 if lower value" in {
        tile.compare(Tile(7, Color.RED, 1)) should be(-1)
      }
    }
  }
  "A string " when {
    "formatting" should {
      val tile = Tile(-1, Color.RED, -1)
      val tile0 = tile.stringToTile("1R0")
      val tile1 = tile.stringToTile("10R1")
      val tile2 = tile.stringToTile("2B0")
      val tile3 = tile.stringToTile("11B1")
      val tile4 = tile.stringToTile("3Y0")
      val tile5 = tile.stringToTile("12Y1")
      val tile6 = tile.stringToTile("4G0")
      val tile7 = tile.stringToTile("13G1")
      "get the tile" in {
        tile0 should be(Tile(1, Color.RED, 0))
        tile1 should be(Tile(10, Color.RED, 1))
        tile2 should be(Tile(2, Color.BLUE, 0))
        tile3 should be(Tile(11, Color.BLUE, 1))
        tile4 should be(Tile(3, Color.YELLOW, 0))
        tile5 should be(Tile(12, Color.YELLOW, 1))
        tile6 should be(Tile(4, Color.GREEN, 0))
        tile7 should be(Tile(13, Color.GREEN, 1))
      }
    }
  }
}
