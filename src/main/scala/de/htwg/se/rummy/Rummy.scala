package de.htwg.se.rummy

import com.google.inject.{Guice, Injector}
import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import de.htwg.se.rummy.view.component.{Gui, Tui}

import scala.collection.immutable.SortedSet

object Rummy {

  val injector: Injector = Guice.createInjector(new RummyModule)

  val desk: Desk = Desk(
    players = List[PlayerInterface](),
    bagOfTiles = Set[TileInterface](),
    table = Set[SortedSet[TileInterface]]())

  val controller: ControllerInterface = new Controller(desk)
  val tui = new Tui(controller)
  val gui = new Gui(controller)


  def main(args: Array[String]): Unit = {
    try {
      println("Type <c> to create a new desk or <l> to load a previous game")
      val input = if (args.length > 0) args(0) else "";
      if (!input.isEmpty) tui.processInput(input)
      else do {
        tui.processInput(readLine())
      } while (input != "q")
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
