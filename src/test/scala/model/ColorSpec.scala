package model

import model.component.component.component.Color
import org.scalatest.{Matchers, WordSpec}

class ColorSpec extends WordSpec with Matchers {


  "A color " when {
    "formatting" should {
      val state0 = Color.colorFromString("BLUE")
      val state1 = Color.colorFromString("RED")
      val state3 = Color.colorFromString("GREEN")
      val state4 = Color.colorFromString("YELLOW")
      "get the tile" in {
        state0 should be(Color.BLUE)
        state1 should be(Color.RED)
        state3 should be(Color.GREEN)
        state4 should be(Color.YELLOW)
      }
    }
  }


}

