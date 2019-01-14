import com.google.inject.AbstractModule
import controller.ControllerInterface
import controller.component.Controller
import model.DeskInterface
import model.deskComp.Desk
import model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface
import net.codingwell.scalaguice.ScalaModule

import scala.collection.SortedSet

class RummyModule extends AbstractModule with ScalaModule {

  val defalutPlayer: Set[PlayerInterface] = Set[PlayerInterface]()
  val defalutBag: Set[TileInterface] = Set[TileInterface]()
  val defalutSet: Set[SortedSet[TileInterface]] = Set[SortedSet[TileInterface]]()

  def configure() = {
    bind[DeskInterface].to[Desk]
    bind[ControllerInterface].to[Controller]
    //    bind[TileInterface].to[Tile]
    //    bind[BoardInterface].to[Board]
    //    bind[PlayerInterface].to[Player]
    bind[FileIOInterface].to[model.fileIO.xml.FileIO]
    //    ScalaMultibinder.newSetBinder[Set[PlayerInterface]](binder).addBinding.to[Player]


  }

}