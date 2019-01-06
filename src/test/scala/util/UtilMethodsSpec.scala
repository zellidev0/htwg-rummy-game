package util

import model.component.component.component.{Color, Tile}
import org.scalatest.{Matchers, WordSpec}

class UtilMethodsSpec extends WordSpec with Matchers {

  "A string " when {
    "formatting" should {
      val tile0 = UtilMethods.regexToTile("1R0")
      val tile1 = UtilMethods.regexToTile("10R1")
      val tile2 = UtilMethods.regexToTile("2B0")
      val tile3 = UtilMethods.regexToTile("11B1")
      val tile4 = UtilMethods.regexToTile("3Y0")
      val tile5 = UtilMethods.regexToTile("12Y1")
      val tile6 = UtilMethods.regexToTile("4G0")
      val tile7 = UtilMethods.regexToTile("13G1")
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
