package controller

import model._
import util.Command

class LayDownCommand(tile: String, controller: Controller) extends Command {
  override def undoStep: Unit = {
    controller.desk = controller.desk.takeUpTile(controller.currentP, controller.regexToTile(tile))
    controller.userPutTileDown -= 1
    controller.swState(ContState.UNDO_LAY_DOWN_TILE)
    controller.swState(ContState.P_TURN)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    controller.desk = controller.desk.putDownTile(controller.currentP, controller.regexToTile(tile))
    controller.userPutTileDown += 1
    controller.notifyObservers()
  }
}
