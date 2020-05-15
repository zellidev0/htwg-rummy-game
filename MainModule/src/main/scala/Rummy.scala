import model.DeskInterface
import model.deskComp.deskBaseImpl.{ Desk, PlayerInterface, TileInterface }

import scala.collection.immutable.SortedSet

object Rummy {

  val desk: DeskInterface =
    Desk(players = List[PlayerInterface](), bagOfTiles = Set[TileInterface](), table = Set[SortedSet[TileInterface]]())

  var controller: ControllerInterface = Controller(desk, AnswerState.CREATE_DESK, ControllerState.MENU)
  var tui                             = new Tui(controller)
//  val gui                             = new Gui(controller)

  def main(args: Array[String]): Unit = {
    println("Type <c> to create a new desk or <l> to load a previous game")
    while (true) {
      val line = scala.io.StdIn.readLine();
      line match {
        case ""  =>
        case "q" => return
        case x   => tui = new Tui(tui.processInput(x));
      }
    }
  }

}
