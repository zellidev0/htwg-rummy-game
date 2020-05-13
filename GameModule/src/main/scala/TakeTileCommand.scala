
import model.deskComp.deskBaseImpl.TileInterface

class TakeTileCommand(private val controller: Controller, private val randomTile: TileInterface) extends Command {

  override def doStep(): Unit =
    redoStep()

  override def undoStep(): Unit = {
    controller.desk = controller.desk.takeTileFromPlayerToBag(controller.getCurrentPlayer, randomTile)
    controller.switchState(AnswerState.UNDO_TAKE_TILE, P_TURN)
  }

  override def redoStep(): Unit = {
    controller.desk = controller.desk.takeTileFromBagToPlayer(controller.getCurrentPlayer, randomTile)
    controller.switchState(AnswerState.TAKE_TILE, NEXT_TYPE_N)
  }
}
