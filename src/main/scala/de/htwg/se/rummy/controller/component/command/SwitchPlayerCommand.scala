package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.{AnswerState, Controller}
import de.htwg.se.rummy.util.Command


class SwitchPlayerCommand(controller: Controller) extends Command {

  override def undoStep(): Unit = {
    controller.desk = controller.desk.switchToNextPlayer(controller.getCurrentPlayer, controller.getPreviousPlayer)
    controller.switchState(AnswerState.NONE, P_TURN)
  }

  override def redoStep(): Unit = doStep()

  override def doStep(): Unit = {
    controller.desk = controller.desk.switchToNextPlayer(controller.getCurrentPlayer, controller.getNextPlayer)
    controller.switchState(AnswerState.NONE, P_TURN)
  }
}
