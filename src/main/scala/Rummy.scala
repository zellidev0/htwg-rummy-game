import controller.component.Controller
import model.component.Desk
import model.component.component.PlayerInterface
import view.component.Tui

object Rummy {


  val controller = new Controller(Desk(Set[PlayerInterface](), Set(), Set()))
  val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {
    println("Type <c> to create a new desk or <l> to load a previous game")
    var input: String = ""
    if (args.length > 0) input = args(0)
    if (!input.isEmpty) tui.processInputLine(input)
    else do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "q")
  }
}
