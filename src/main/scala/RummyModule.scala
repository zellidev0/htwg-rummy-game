import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controller.ControllerInterface
import controller.component.Controller
import model.DeskInterface
import model.component.Desk
import model.component.component.{PlayerInterface, TileInterface}
import net.codingwell.scalaguice.ScalaModule

import scala.collection.SortedSet

class RummyModule extends AbstractModule with ScalaModule {

  val defalutPlayer: Set[PlayerInterface] = Set[PlayerInterface]()
  val defalutBag: Set[TileInterface] = Set[TileInterface]()
  val defalutSet: Set[SortedSet[TileInterface]] = Set[SortedSet[TileInterface]]()

  def configure() = {
    bindConstant().annotatedWith(Names.named("Default"))
    bind[DeskInterface].to[Desk]
    bind[ControllerInterface].to[Controller]

  }

}