package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.util.Command

class FinishedCommand(userPutTileDown: Int, controller: Controller) extends Command {

  override def undoStep: Unit = {
    controller.userPutTileDown = userPutTileDown
    controller.swState(P_TURN)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    controller.userPutTileDown = 0
    controller.swState(P_FINISHED)
  }
}
