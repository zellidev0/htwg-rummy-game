package model.component.component.component

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
}
