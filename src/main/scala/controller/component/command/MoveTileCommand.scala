package controller.component.command

import controller.component.Controller
import model.deskComp.Desk
import model.deskComp.deskBaseImpl.TileInterface
import model.deskComp.deskBaseImpl.deskImpl.{Color, Tile}
import util.Command

import scala.collection.SortedSet

class MoveTileCommand(tile1: String, tile2: String, controller: Controller) extends Command {
  var setWithTile: Option[TileInterface] = None


  override def doStep: Unit = {
    val t = Tile(-1, Color.RED, -1)
    setWithTile = Option(controller.desk.sets.find(s => s.contains(t.stringToTile(tile1))).get.head)
    if (setWithTile.get.identifier == tile1) {
      setWithTile = None
    }
    controller.desk = controller.desk.moveTwoTilesOnDesk(t.stringToTile(tile1), t.stringToTile(tile2))
    controller.notifyObservers()
  }


  override def undoStep: Unit = {
    val t = Tile(-1, Color.RED, -1)
    setWithTile match {
      case Some(x) => controller.desk = controller.desk.moveTwoTilesOnDesk(t.stringToTile(tile1), x)
      case None =>
        controller.removeTileFromSet(t.stringToTile(tile1))
        controller.desk = Desk(sets = controller.desk.sets + SortedSet[TileInterface](t.stringToTile(tile1)), players = controller.desk.players, bagOfTiles = controller.desk.bagOfTiles)
    }
    controller.notifyObservers()
  }

  override def redoStep: Unit = {
    val t = Tile(-1, Color.RED, -1)
    controller.desk = controller.desk.moveTwoTilesOnDesk(t.stringToTile(tile1), t.stringToTile(tile2))
    controller.notifyObservers()
  }
}
