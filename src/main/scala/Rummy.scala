import controller.Controller
import model._
import view.Tui

object Rummy {


  val controller = new Controller(Desk(Set[Player](), Set(), Set()))
  val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {
    println("Type <c>")
    var input: String = ""
    if (args.length > 0) input = args(0)
    if (!input.isEmpty) tui.processInputLine(input)
    else do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "q")
  }
}
