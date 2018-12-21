package util

import model.component.component.component.{Color, State, Tile}

object UtilMethods {

  def regexToTile(regex: String): Tile = {
    val color = regex.charAt(regex.length - 2) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    Tile(Integer.parseInt(regex.substring(0, regex.length - 2)), color, Integer.parseInt(regex.charAt(regex.length - 1).toString))
  }

  def stringToState(str: String): State.Value = {
    str match {
      case "WAIT" => State.WAIT
      case "WON" => State.WON
      case "TURN" => State.TURN
      case "LOST" => State.LOST
    }
  }

}
