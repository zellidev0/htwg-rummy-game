import scala.util.matching.Regex

trait UIInterface extends Observer {
  val PlayerNamePattern: Regex  = "name [A-Za-z]+".r
  val LayDownTilePattern: Regex = "(l [1-9][RBGY][01]|l 1[0123][RBGY][01])".r
  val MoveTilePattern: Regex =
    "(m [1-9][RBGY][01] t [1-9][RBGY][01]|m 1[0123][RBGY][01] t [1-9][RBYG][01]|m 1[0-3][RBGY][01] t 1[0-3][RBGY][01]|m [1-9][RBGY][01] t 1[0-3][RBYG][01])".r
  val elements = 12

  def processInput(input: String): Unit

  def handleNameInput(name: String): Unit

  def handleOnTurnFinished(input: String): Unit

  def handleOnTurn(input: String): Unit

  def handleMenuInput(input: String): Unit
}
