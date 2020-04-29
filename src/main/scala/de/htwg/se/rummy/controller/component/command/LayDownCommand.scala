package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.{AnswerState, Controller}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface
import de.htwg.se.rummy.util.Command

class LayDownCommand(tile: TileInterface, controller: Controller) extends Command {

  override def undoStep(): Unit = {
    controller.desk = controller.desk.takeUpTile(controller.getCurrentPlayer, tile)
    controller.userPutTileDown -= 1
    controller.switchState(AnswerState.UNDO_LAY_DOWN_TILE, P_TURN)
  }
  override def redoStep(): Unit = doStep()
  override def doStep(): Unit = {
    controller.desk = controller.desk.putDownTile(controller.getCurrentPlayer, tile)
    controller.userPutTileDown += 1
    controller.switchState(AnswerState.PUT_TILE_DOWN, P_TURN)
  }
}
