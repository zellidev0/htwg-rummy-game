class FinishedCommand(userPutTileDown: Int, controller: Controller) extends Command {

  override def undoStep(): Unit = {
    controller.userPutTileDown = userPutTileDown
    controller.switchState(AnswerState.P_FINISHED_UNDO, P_TURN)
  }
  override def redoStep(): Unit =
    doStep()

  override def doStep(): Unit = {
    controller.userPutTileDown = 0
    controller.switchState(AnswerState.P_FINISHED, NEXT_TYPE_N)
  }
}
