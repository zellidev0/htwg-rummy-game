package view

import util.Observer

trait TuiInterface extends Observer {

  def processInputLine(input: String): Unit
  def update: Unit
}
