package controller.component.command

import controller.component.{ContState, Controller}
import util.Command
import util.UtilMethods.regexToTile

class LayDownCommand(tile: String, controller: Controller) extends Command {
  override def undoStep: Unit = {
    controller.desk = controller.desk.takeUpTile(controller.currentP, regexToTile(tile))
    controller.userPutTileDown -= 1
    controller.swState(ContState.UNDO_LAY_DOWN_TILE)
    controller.swState(ContState.P_TURN)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    controller.desk = controller.desk.putDownTile(controller.currentP, regexToTile(tile))
    controller.userPutTileDown += 1
    controller.notifyObservers()
  }
}
