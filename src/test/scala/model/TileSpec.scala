package model


import org.scalatest.{Matchers, WordSpec}

class TileSpec extends WordSpec with Matchers {

  "A tile" when {
    "created with value and color" should {
      val tile = Tile(1, Color.RED, 1)
      "have value 1 and color red and num 1" in {
        tile.value should be(1)
        tile.color should be(Color.RED)
        tile.ident should be(1)
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
}
