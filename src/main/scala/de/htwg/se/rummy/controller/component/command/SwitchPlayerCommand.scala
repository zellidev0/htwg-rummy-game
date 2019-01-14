package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.ContState._
import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.util.Command


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
