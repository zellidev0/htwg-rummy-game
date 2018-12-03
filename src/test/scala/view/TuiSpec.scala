package view

import controller.Controller
import org.scalatest.{Matchers, WordSpec}


class TuiSpec extends WordSpec with Matchers {

  "A Tui" when {
    "created" should {
      var desk = Desk()
      var controller = new Controller()
      "have value 1 and color red and num 1" in {

      }
    }
    "compares with other tile" should {

    }
  }
}
