package controller

import model.ContState.{P_FINISHED, P_TURN}
import util.Command

class FinishedCommand(controller: Controller) extends Command {

  override def undoStep: Unit = controller.swState(P_TURN)
  override def redoStep: Unit = doStep
  override def doStep: Unit = controller.swState(P_FINISHED)
}
