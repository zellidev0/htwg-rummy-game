package model


object ControllerState extends Enumeration {
  type state = Value
  val MENU, INSERTING_NAMES, PLAYING = Value
}
