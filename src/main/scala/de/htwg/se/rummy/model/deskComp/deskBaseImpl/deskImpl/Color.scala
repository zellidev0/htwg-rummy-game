package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

object Color extends Enumeration {
  val RED, BLUE, YELLOW, GREEN = Value

  def colorFromString(string: String): Color.Value = {
    string match {
      case "GREEN" => Color.GREEN
      case "RED" => Color.RED
      case "YELLOW" => Color.YELLOW
      case "BLUE" => Color.BLUE
    }
  }

  def beginningChar(color:Color.Value): String = {
    color.toString.charAt(0).toString
  }

}
