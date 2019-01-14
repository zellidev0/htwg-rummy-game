package de.htwg.se.rummy

import com.google.inject.{Guice, Injector}
import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}
import de.htwg.se.rummy.view.component.Tui

import scala.collection.SortedSet

object Rummy {

  val injector: Injector = Guice.createInjector(new RummyModule)
  //  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val defalutPlayer: Set[PlayerInterface] = Set[PlayerInterface]()
  val defalutBag: Set[TileInterface] = Set[TileInterface]()
  val defalutSet: Set[SortedSet[TileInterface]] = Set[SortedSet[TileInterface]]()

  val desk = deskBaseImpl.Desk(defalutPlayer, defalutBag, defalutSet)
  val controller: ControllerInterface = new Controller(desk)
  val tui = new Tui(controller)
  //    val gui = new Gui(controller)


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
