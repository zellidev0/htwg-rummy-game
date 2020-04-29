package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.{AnswerState, Controller}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface
import de.htwg.se.rummy.util.Command


class TakeTileCommand(controller: Controller) extends Command {
  private var randomTile: TileInterface = _

  override def doStep(): Unit = {
    randomTile = controller.desk.getTileFromBag
    redoStep()
  }


  override def undoStep(): Unit = {
    controller.desk = controller.desk.takeTileFromPlayerToBag(controller.getCurrentPlayer, randomTile)
    controller.switchState(AnswerState.UNDO_TAKE_TILE, P_TURN)
  }

  override def redoStep(): Unit = {
    controller.desk = controller.desk.takeTileFromBagToPlayer(controller.getCurrentPlayer, randomTile)
    controller.switchState(AnswerState.TAKE_TILE, NEXT_TYPE_N)
  }
}
