package model.deskComp.deskBaseImpl.deskImpl

object State extends Enumeration {
  type state = Value
  var WON, WAIT, TURN = Value
  def stringToState(str: String): State.Value = {
    str match {
      case "WAIT" => State.WAIT
      case "WON" => State.WON
      case "TURN" => State.TURN
    }
  }
}
