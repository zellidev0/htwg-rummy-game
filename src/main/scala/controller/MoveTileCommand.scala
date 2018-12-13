package controller

import model._
import util.Command

import scala.collection.SortedSet

class MoveTileCommand(tile1: String, tile2: String, controller: Controller) extends Command {
  var setWithTile: Option[Tile] = None


  override def doStep: Unit = {
    setWithTile = Option(controller.desk.sets.find(s => s.contains(controller.regexToTile(tile1))).get.head)
    if (setWithTile.get.identifier == tile1) {
      setWithTile = None
    }
    controller.desk = controller.desk.moveTwoTilesOnDesk(controller.regexToTile(tile1), controller.regexToTile(tile2))
    controller.notifyObservers()
  }


  override def undoStep: Unit = {
    setWithTile match {
      case Some(x) => controller.desk = controller.desk.moveTwoTilesOnDesk(controller.regexToTile(tile1), x)
      case None =>


        controller.removeTileFromSet(controller.regexToTile(tile1))
        controller.desk = controller.desk.copy(sets = controller.desk.sets + SortedSet[Tile](controller.regexToTile(tile1)))
    }
    controller.notifyObservers()
  }

  override def redoStep: Unit = {
    controller.desk = controller.desk.moveTwoTilesOnDesk(controller.regexToTile(tile1), controller.regexToTile(tile2))
    controller.notifyObservers()
  }
}
