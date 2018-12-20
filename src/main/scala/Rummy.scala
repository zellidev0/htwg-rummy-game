import com.google.inject.{Guice, Injector}
import controller.ControllerInterface
import view.component.Tui

object Rummy {

  val injector: Injector = Guice.createInjector(new RummyModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  controller.init()


  def main(args: Array[String]): Unit = {
    println("Type <c>")
    var input: String = ""

    do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "q")
  }
}
