class SwitchPlayerCommand(controller: Controller) extends Command {

  override def undoStep(): Unit = {
    controller.desk = controller.desk.switchToPreviousPlayer
    controller.switchState(AnswerState.NONE, P_TURN)
  }

  override def redoStep(): Unit =
    doStep()

  override def doStep(): Unit = {
    controller.desk = controller.desk.switchToNextPlayer
    controller.switchState(AnswerState.NONE, P_TURN)
  }
}
