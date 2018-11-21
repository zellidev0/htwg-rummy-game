package view

import controller.Controller
import util.Observer

class Tui(controller: Controller) extends Observer {

  val AmountOfPlayersPattern = "[2-4]".r
  val PlayerNamePattern = "name [A-Za-z]+".r

  def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "c" => controller.createDesk(13)
      case PlayerNamePattern(c) => controller.setPlayerName(c.substring(5))
    }
  }

  controller.add(this)


  override def update: Boolean = {
    true
  }
}
