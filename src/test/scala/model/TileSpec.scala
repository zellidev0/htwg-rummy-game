package model


import org.scalatest.{Matchers, WordSpec}

class TileSpec extends WordSpec with Matchers {

  "A tile" when {
    "created with val 1 and color red" should {
      val tile = Tile(1, Color.RED)
      "have value 1 and color red" in {
        tile.value should be(1)
        tile.color should be (Color.RED)
      }
    }
  }

}
