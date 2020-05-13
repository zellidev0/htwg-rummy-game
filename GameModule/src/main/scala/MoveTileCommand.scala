

class MoveTileCommand(fromTile: TileInterface, toTile: TileInterface, controller: Controller) extends Command {
  private var savedDesk: Option[DeskInterface] = None

  override def doStep(): Unit = {
    val desk = controller.desk.tryToMoveTwoTilesOnDesk(fromTile, toTile)
    savedDesk = if (controller.desk != desk) Some(desk) else None
    controller.desk = desk
    controller.switchState(AnswerState.MOVED_TILE, P_TURN)
  }

  override def undoStep(): Unit =
    controller.switchState(savedDesk match {
      case Some(x) =>
        controller.desk = x
        AnswerState.UNDO_MOVED_TILE
      case None =>
        AnswerState.UNDO_MOVED_TILE_NOT_DONE
    }, P_TURN)

  override def redoStep(): Unit = doStep()

}
