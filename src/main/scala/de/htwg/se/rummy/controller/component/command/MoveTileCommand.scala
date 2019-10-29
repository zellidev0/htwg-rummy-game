package de.htwg.se.rummy.controller.component.command


import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.controller.component.ControllerState.P_TURN
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.Desk
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.Tile
import de.htwg.se.rummy.util.Command

import scala.collection.immutable.SortedSet

class MoveTileCommand(tile1: String, tile2: String, controller: Controller) extends Command {
  var setWithTile: Option[Tile] = None


  override def doStep: Unit = {
    setWithTile = Option(controller.desk.table.find(s => s.contains(Tile.stringToTile(tile1))).get.head)
    if (setWithTile.get.equals(Tile.stringToTile(tile1))) {
      setWithTile = None
    }
    controller.desk = controller.desk.moveTwoTilesOnDesk(Tile.stringToTile(tile1), Tile.stringToTile(tile2))
    controller.swState(P_TURN)
  }


  override def undoStep(): Unit = {
    setWithTile match {
      case Some(x) => controller.desk = controller.desk.moveTwoTilesOnDesk(Tile.stringToTile(tile1), x)
      case None =>
        controller.removeTileFromSet(Tile.stringToTile(tile1))
        controller.desk = Desk(table = controller.desk.table + SortedSet[Tile](Tile.stringToTile(tile1)), players = controller.desk.players, bagOfTiles = controller.desk.bagOfTiles)
    }
    controller.swState(P_TURN)
  }

  override def redoStep(): Unit = {
    controller.desk = controller.desk.moveTwoTilesOnDesk(Tile.stringToTile(tile1), Tile.stringToTile(tile2))
    controller.swState(P_TURN)
  }
}
