package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.ContState._
import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface
import de.htwg.se.rummy.util.Command


class TakeTileCommand(controller: Controller) extends Command {
  private var randomTile: TileInterface = _


  override def doStep: Unit = {
    randomTile = controller.desk.randomTileInBag
    controller.desk = controller.desk.takeTileFromBagToPlayer(controller.currentP, randomTile)
    controller.swState(P_FINISHED)
  }


  override def undoStep: Unit = {
    controller.desk = controller.desk.takeTileFromPlayerToBag(controller.currentP, randomTile)
    controller.swState(P_TURN)
  }

  override def redoStep: Unit = {
    controller.desk = controller.desk.takeTileFromBagToPlayer(controller.currentP, randomTile)
    controller.swState(P_FINISHED)
  }
}
