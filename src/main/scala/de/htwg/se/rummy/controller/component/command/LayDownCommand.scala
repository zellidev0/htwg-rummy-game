package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Color, Tile}
import de.htwg.se.rummy.util.Command

class LayDownCommand(tile: String, controller: Controller) extends Command {
  override def undoStep: Unit = {
    val t = Tile(-1, Color.RED, -1)
    controller.desk = controller.desk.takeUpTile(controller.currentP, t.stringToTile(tile))
    controller.userPutTileDown -= 1
    controller.swState(UNDO_LAY_DOWN_TILE)
    controller.swState(P_TURN)
  }
  override def redoStep: Unit = doStep
  override def doStep: Unit = {
    val t = Tile(-1, Color.RED, -1)
    controller.desk = controller.desk.putDownTile(controller.currentP, t.stringToTile(tile))
    controller.userPutTileDown += 1
    controller.swState(P_TURN)
  }
}
