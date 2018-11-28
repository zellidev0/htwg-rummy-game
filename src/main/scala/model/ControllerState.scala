package model


object ControllerState extends Enumeration {
  type state = Value
  val MENU, INSERTING_NAMES, TAKE_OR_PLAY, PLAY, PLAYER_FINISHED = Value
}
