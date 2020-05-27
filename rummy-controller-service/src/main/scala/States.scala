

object AnswerState extends Enumeration {
  type AnswerState = Value

  val CREATE_DESK: AnswerState.Value = Value("Created a desk")
  val P_FINISHED: AnswerState.Value = Value("You are finished.")
  val TABLE_NOT_CORRECT: AnswerState.Value = Value("Table looks not correct, move tiles to match the rules")
  val P_WON: AnswerState.Value = Value("One Player is the winner.")
  val UNDO_TAKE_TILE: AnswerState.Value = Value("The tile has been put back in the bag")
  val BAG_IS_EMPTY: AnswerState.Value = Value("No more tiles in the bag. You must lay a tile down")
  val CANT_MOVE_THIS_TILE: AnswerState.Value = Value("You can not move this tile.")
  val UNDO_MOVED_TILE: AnswerState.Value = Value("You undid the move of a specific tile.")
  val MOVED_TILE: AnswerState.Value = Value("Moved tile if allowed.")
  val UNDO_LAY_DOWN_TILE: AnswerState.Value = Value("You undid the lay down you took the tile up.")
  val ADDED_PLAYER: AnswerState.Value = Value("You added a player.")
  val PUT_TILE_DOWN: AnswerState.Value = Value("You put down a tile")
  val REMOVED_PLAYER: AnswerState.Value = Value("You removed the player you inserted.")
  val INSERTING_NAMES_FINISHED: AnswerState.Value = Value("You finished inserting the names.")
  val STORED_FILE: AnswerState.Value = Value("You stored the game in a file")
  val ENOUGH_PLAYER: AnswerState.Value = Value("The Maximum amount of players is set. Type 'f' to finish inserting names")
  val NOT_ENOUGH_PLAYERS: AnswerState.Value = Value("Not enough Players. Add some more.")
  val COULD_NOT_LOAD_FILE: AnswerState.Value = Value("Could not load the file. Created a new game instead.")
  val LOADED_FILE: AnswerState.Value = Value("You created a file")
  val CREATED_DESK: AnswerState.Value = Value("You started the game by creating a desk")
  val UNDO_MOVED_TILE_NOT_DONE: AnswerState.Value = Value("Undo the move of the tile unnecessary. Nothing did happen.")
  val P_FINISHED_UNDO: AnswerState.Value = Value("Its is again your turn. ")
  val TOOK_TILE: AnswerState.Value = Value("Auto took a tile")
  val UNDO: AnswerState.Value = Value("Undid the last operation")
  val REDO: AnswerState.Value = Value("Undid the undo operation")
  val SWITCHED_TO_NEXT: AnswerState.Value = Value("Switched to next")
  val COULD_NOT_PARSE: AnswerState.Value = Value("Could not parse data sent")


  def from(state: String): AnswerState.Value = {
    state match {
      case "CREATE_DESK" => CREATE_DESK
      case "P_FINISHED" => P_FINISHED
      case "TABLE_NOT_CORRECT" => TABLE_NOT_CORRECT
      case "P_WON" => P_WON
      case "UNDO_TAKE_TILE" => UNDO_TAKE_TILE
      case "BAG_IS_EMPTY" => BAG_IS_EMPTY
      case "CANT_MOVE_THIS_TILE" => CANT_MOVE_THIS_TILE
      case "UNDO_MOVED_TILE" => UNDO_MOVED_TILE
      case "MOVED_TILE" => MOVED_TILE
      case "UNDO_LAY_DOWN_TILE" => UNDO_LAY_DOWN_TILE
      case "ADDED_PLAYER" => ADDED_PLAYER
      case "PUT_TILE_DOWN" => PUT_TILE_DOWN
      case "REMOVED_PLAYER" => REMOVED_PLAYER
      case "INSERTING_NAMES_FINISHED" => INSERTING_NAMES_FINISHED
      case "STORED_FILE" => STORED_FILE
      case "ENOUGH_PLAYER" => ENOUGH_PLAYER
      case "NOT_ENOUGH_PLAYERS" => NOT_ENOUGH_PLAYERS
      case "COULD_NOT_LOAD_FILE" => COULD_NOT_LOAD_FILE
      case "LOADED_FILE" => LOADED_FILE
      case "CREATED_DESK" => CREATED_DESK
      case "UNDO_MOVED_TILE_NOT_DONE" => UNDO_MOVED_TILE_NOT_DONE
      case "P_FINISHED_UNDO" => P_FINISHED_UNDO
      case "TOOK_TILE" => TOOK_TILE
      case "UNDO" => UNDO
      case "REDO" => REDO
      case "SWITCHED_TO_NEXT" => SWITCHED_TO_NEXT
      case "COULD_NOT_PARSE" => COULD_NOT_PARSE
    }
  }
}

object ControllerState extends Enumeration {
  val MENU, INSERTING_NAMES, P_TURN, NEXT_TYPE_N, KILL = Value


  def from(state: String): ControllerState.Value = {
    state match {
      case "MENU" => MENU
      case "INSERTING_NAMES" => INSERTING_NAMES
      case "P_TURN" => P_TURN
      case "NEXT_TYPE_N" => NEXT_TYPE_N
      case "KILL" => KILL
    }
  }
}
