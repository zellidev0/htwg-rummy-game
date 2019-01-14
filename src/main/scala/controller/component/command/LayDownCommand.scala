package controller.component.command

import controller.component.{ContState, Controller}
import model.deskComp.deskBaseImpl.deskImpl.{Color, Tile}
import util.Command

class LayDownCommand(tile: String, controller: Controller) extends Command {
  override def undoStep: Unit = {
    val t = Tile(-1, Color.RED, -1)
    controller.desk = controller.desk.takeUpTile(controller.currentP, t.stringToTile(tile))
    controller.userPutTileDown -= 1
    controller.swState(ContState.UNDO_LAY_DOWN_TILE)
    controller.swState(ContState.P_TURN)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    val t = Tile(-1, Color.RED, -1)
    controller.desk = controller.desk.putDownTile(controller.currentP, t.stringToTile(tile))
    controller.userPutTileDown += 1
    controller.notifyObservers()
  }
}
