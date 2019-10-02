package de.htwg.se.rummy.controller

import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.State
import org.scalatest.{Matchers, WordSpec}

class ControllerStateSpec extends WordSpec with Matchers {


  "A state " when {
    "formatting" should {
      val state0 = State.stringToState("WAIT")
      val state1 = State.stringToState("WON")
      val state2 = State.stringToState("TURN")
      "get the tile" in {
        state0 should be(State.WAIT)
        state1 should be(State.WON)
        state2 should be(State.TURN)
      }
    }
  }

}
