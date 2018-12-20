package controller

import controller.ContState.{P_FINISHED, P_TURN}
import util.Command

class FinishedCommand(userputTileDown: Int, controller: Controller) extends Command {

  override def undoStep: Unit = {
    controller.userPutTileDown = userputTileDown
    controller.swState(P_TURN)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    controller.userPutTileDown = 0
    controller.swState(P_FINISHED)
  }
}
