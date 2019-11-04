package de.htwg.se.rummy.controller.component.command


import de.htwg.se.rummy.controller.component.{AnswerState, Controller}
import de.htwg.se.rummy.controller.component.ControllerState.P_TURN
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{Desk, TileInterface}
import de.htwg.se.rummy.util.Command

import scala.collection.immutable.SortedSet

class MoveTileCommand(fromTile: TileInterface, toTile: TileInterface, controller: Controller) extends Command {
  private val desk = controller.desk.table
  private var moved = false


  override def doStep(): Unit = {
    val setWithFromTile = controller.desk.table.find(s => s.contains(fromTile)).get
    val setWithToTile = controller.desk.table.find(s => s.contains(toTile)).get
    if (setWithFromTile.equals(setWithToTile)) {
      controller.switchState(AnswerState.CANT_MOVE_THIS_TILE, P_TURN)
    } else {
      moved = true
      controller.desk = controller.desk.moveTwoTilesOnDesk(fromTile, toTile)
      controller.switchState(AnswerState.MOVED_TILE, P_TURN)
    }
  }


  override def undoStep(): Unit = {
    if (!moved) {
      controller.switchState(AnswerState.UNDO_MOVED_TILE_NOT_DONE, P_TURN)
    } else {
      controller.desk = Desk(table = desk, players = controller.desk.players, bagOfTiles = controller.desk.bagOfTiles)
      controller.switchState(AnswerState.UNDO_MOVED_TILE, P_TURN)
    }
  }

  override def redoStep(): Unit = {
    doStep()
  }
}
