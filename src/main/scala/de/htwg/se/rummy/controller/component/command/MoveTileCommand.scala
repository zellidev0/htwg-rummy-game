package de.htwg.se.rummy.controller.component.command


import de.htwg.se.rummy.controller.component.{AnswerState, Controller}
import de.htwg.se.rummy.controller.component.ControllerState.P_TURN
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{Desk, TileInterface}
import de.htwg.se.rummy.util.Command

import scala.collection.immutable.SortedSet

class MoveTileCommand(fromTile: TileInterface, toTile: TileInterface, controller: Controller) extends Command {
  var setWithTile: Option[TileInterface] = None


  override def doStep(): Unit = {
    setWithTile = Option(controller.desk.table.find(s => s.contains(fromTile)).get.head)
    if (setWithTile.get.equals(fromTile)) {
      setWithTile = None
    }
    controller.desk = controller.desk.moveTwoTilesOnDesk(fromTile, toTile)
    controller.switchAnswerState(AnswerState.MOVED_TILE)
    controller.switchControllerState(P_TURN)
  }


  override def undoStep(): Unit = {
    setWithTile match {
      case Some(x) => controller.desk = controller.desk.moveTwoTilesOnDesk(fromTile, x)
      case None =>
        controller.removeTileFromSet(fromTile)
        controller.desk = Desk(table = controller.desk.table + SortedSet[TileInterface](fromTile), players = controller.desk.players, bagOfTiles = controller.desk.bagOfTiles)
    }
    controller.switchAnswerState(AnswerState.UNDO_MOVED_TILE)
    controller.switchControllerState(P_TURN)
  }

  override def redoStep(): Unit = {
    controller.desk = controller.desk.moveTwoTilesOnDesk(fromTile, toTile)
    controller.switchAnswerState(AnswerState.MOVED_TILE)
    controller.switchControllerState(P_TURN)
  }
}
