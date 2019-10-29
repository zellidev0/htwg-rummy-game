package de.htwg.se.rummy

import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.Desk
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Player, Tile}
import de.htwg.se.rummy.view.component.{Gui, Tui}

import scala.collection.immutable.SortedSet

object Rummy {

  val defaultPlayer: Set[Player] = Set[Player]()
  val defaultBag: Set[Tile] = Set[Tile]()
  val defaultSet: Set[SortedSet[Tile]] = Set[SortedSet[Tile]]()

  val desk = Desk(defaultPlayer, defaultBag, defaultSet)
  val controller: Controller = new Controller(desk)
  val tui = new Tui(controller)
  val gui = new Gui(controller)


  def main(args: Array[String]): Unit = {
    try {
      println("Type <c> to create a new desk or <l> to load a previous game")
      var input: String = ""
      if (args.length > 0) input = args(0)
      if (!input.isEmpty) tui.processInput(input)
      else do {
        input = readLine()
        tui.processInput(input)
      } while (input != "q")
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
