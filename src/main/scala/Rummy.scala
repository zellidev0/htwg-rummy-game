import com.google.inject.{Guice, Injector}
import controller.ControllerInterface
import view.component.Tui

object Rummy {

  val injector: Injector = Guice.createInjector(new RummyModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  //  val gui = new Gui(controller)


  def main(args: Array[String]): Unit = {
    println("Type <c> to create a new desk or <l> to load a previous game")
    var input: String = ""
    if (args.length > 0) input = args(0)
    if (!input.isEmpty) tui.processInput(input)
    else do {
      input = readLine()
      tui.processInput(input)
    } while (input != "q")

  }
}
