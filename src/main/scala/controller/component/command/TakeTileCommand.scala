package controller.component.command

import controller.component.ContState.{P_FINISHED, P_TURN}
import controller.component.Controller
import model.deskComp.deskBaseImpl.TileInterface
import util.Command

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
