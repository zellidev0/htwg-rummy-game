package rummy

import model.DeskInterface
import model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import rummy.controller.impl
import rummy.ui.UIConnector
import rummy.ui.impl.{Tui, Wui}
import rummy.util.{AnswerState, ControllerState}

import scala.collection.immutable.SortedSet

object Rummy {

  val desk: DeskInterface = Desk(
    players = List[PlayerInterface](),
    bagOfTiles = Set[TileInterface](),
    table = Set[SortedSet[TileInterface]]())

  UIConnector.updateController(impl.Controller(desk, AnswerState.CREATE_DESK, ControllerState.MENU))

  //  val gui  = new Gui(UIConnector)
  val akka = Wui()
  var tui = new Tui(UIConnector)

  def main(args: Array[String]): Unit = {
    println("Type <c> to create a new desk or <l> to load a previous game")
    while (true) {
      val line = scala.io.StdIn.readLine();
      line match {
        case "" =>
        case "q" => return
        case x => tui.processInput(x)
      }
    }
  }

}
