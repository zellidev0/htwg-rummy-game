package controller

import controller.ContState.P_TURN
import util.Command

class SwitchPlayerCommand(controller: Controller) extends Command {
  override def undoStep: Unit = {
    controller.desk = controller.desk.switchToNextPlayer(controller.currentP, controller.previousP)
    controller.swState(P_TURN)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    controller.desk = controller.desk.switchToNextPlayer(controller.currentP, controller.nextP)
    controller.swState(P_TURN)
  }
}
