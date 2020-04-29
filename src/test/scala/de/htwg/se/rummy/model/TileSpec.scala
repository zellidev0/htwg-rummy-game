package de.htwg.se.rummy.model

import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Color, Tile}
import org.scalatest.{Matchers, WordSpec}

class TileSpec extends WordSpec with Matchers {

  "A tile" when {
    val tile = Tile(5, Color.RED, 0)
    "created with value 1 and color red" should {
      "have value 1" in {
        tile.value should be(5)
      }
      "have color red" in {
        tile.color should be(Color.RED)
      }
      "have ident 0" in {
        tile.ident should be(0)
      }
    }
    "compared to other tile" should {
      "be 0 if same value" in {
        tile.compare(Tile(5, Color.BLUE, 1)) should be(0)
      }
      "be greater 0 if higher value" in {
        tile.compare(Tile(6, Color.BLUE, 1)) should be < 0
      }
      "be less 0 if lower value" in {
        tile.compare(Tile(4, Color.BLUE, 1)) should be > 0
      }
    }
    "toString" should {
      "be '5RO'" in {
        tile.toString should be("5R0")
      }
    }
  }
  "A string" when {
    "formatted" should {
      val tileCorrect0 = Tile.stringToTile("1R0")
      val tileCorrect1 = Tile.stringToTile("10R1")
      val tileCorrect2 = Tile.stringToTile("2B0")
      val tileCorrect3 = Tile.stringToTile("11B1")
      val tileCorrect4 = Tile.stringToTile("3Y0")
      val tileCorrect5 = Tile.stringToTile("12Y1")
      val tileCorrect6 = Tile.stringToTile("4G0")
      val tileCorrect7 = Tile.stringToTile("13G1")
      val tileWrong7 = Tile.stringToTile("xG1")
      val tileWrong8 = Tile.stringToTile("4g1")
      val tileWrong9 = Tile.stringToTile("5G2")
      "give a correct instance" in {
        tileCorrect0.get should be(Tile(1, Color.RED, 0))
        tileCorrect1.get should be(Tile(10, Color.RED, 1))
        tileCorrect2.get should be(Tile(2, Color.BLUE, 0))
        tileCorrect3.get should be(Tile(11, Color.BLUE, 1))
        tileCorrect4.get should be(Tile(3, Color.YELLOW, 0))
        tileCorrect5.get should be(Tile(12, Color.YELLOW, 1))
        tileCorrect6.get should be(Tile(4, Color.GREEN, 0))
        tileCorrect7.get should be(Tile(13, Color.GREEN, 1))
      }
      "give a None" in {
        tileWrong7 should be(None)
        tileWrong8 should be(None)
        tileWrong9 should be(None)
      }
    }
    "parsing a value" should {
      val valueCorrect1 = "1R0"
      val valueCorrect2 = "5R0"
      val valueCorrect3 = "13R0"
      val valueWrong1 = "0R0"
      val valueWrong2 = "-1R0"
      val valueWrong3 = "14R0"
      val valueWrong4 = "xR0"
      "give a correct instance" in {
        Tile.parseValue(valueCorrect1).get should be(1)
        Tile.parseValue(valueCorrect2).get should be(5)
        Tile.parseValue(valueCorrect3).get should be(13)
      }
      "give a None" in {
        Tile.parseValue(valueWrong1) should be(None)
        Tile.parseValue(valueWrong2) should be(None)
        Tile.parseValue(valueWrong3) should be(None)
        Tile.parseValue(valueWrong4) should be(None)
      }
    }
    "parsing a color" should {
      val valueCorrect1 = "1R0"
      val valueCorrect2 = "5G0"
      val valueWrong1 = "0X0"
      val valueWrong2 = "-150"
      val valueWrong3 = "14P0"
      "give a correct instance" in {
        Tile.parseColor(valueCorrect1).get should be(Color.RED)
        Tile.parseColor(valueCorrect2).get should be(Color.GREEN)
      }
      "give a None" in {
        Tile.parseColor(valueWrong1) should be(None)
        Tile.parseColor(valueWrong2) should be(None)
        Tile.parseColor(valueWrong3) should be(None)
      }
    }
    "parsing a identifier" should {
      val valueCorrect1 = "1R0"
      val valueCorrect2 = "5G1"
      val valueWrong1 = "0X-4"
      val valueWrong2 = "12R2"
      val valueWrong3 = "14PX"
      "give a correct instance" in {
        Tile.parseIdent(valueCorrect1).get should be(0)
        Tile.parseIdent(valueCorrect2).get should be(1)
      }
      "give a None" in {
        Tile.parseIdent(valueWrong1) should be(None)
        Tile.parseIdent(valueWrong2) should be(None)
        Tile.parseIdent(valueWrong3) should be(None)
      }
    }
  }
}
