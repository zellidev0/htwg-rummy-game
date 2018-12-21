package controller.component.command

import controller.component.Controller
import model.component.Desk
import model.component.component.TileInterface
import util.Command
import util.UtilMethods.regexToTile

import scala.collection.SortedSet

class MoveTileCommand(tile1: String, tile2: String, controller: Controller) extends Command {
  var setWithTile: Option[TileInterface] = None


  override def doStep: Unit = {
    setWithTile = Option(controller.desk.sets.find(s => s.contains(regexToTile(tile1))).get.head)
    if (setWithTile.get.identifier == tile1) {
      setWithTile = None
    }
    controller.desk = controller.desk.moveTwoTilesOnDesk(regexToTile(tile1), regexToTile(tile2))
    controller.notifyObservers()
  }


  override def undoStep: Unit = {
    setWithTile match {
      case Some(x) => controller.desk = controller.desk.moveTwoTilesOnDesk(regexToTile(tile1), x)
      case None =>
        controller.removeTileFromSet(regexToTile(tile1))
        controller.desk = Desk(sets = controller.desk.sets + SortedSet[TileInterface](regexToTile(tile1)), players = controller.desk.players, bagOfTiles = controller.desk.bagOfTiles)
    }
    controller.notifyObservers()
  }

  override def redoStep: Unit = {
    controller.desk = controller.desk.moveTwoTilesOnDesk(regexToTile(tile1), regexToTile(tile2))
    controller.notifyObservers()
  }
}
