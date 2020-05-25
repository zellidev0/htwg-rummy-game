object AnswerState extends Enumeration {
  type AnswerState = Value

  val CREATE_DESK: AnswerState.Value              = Value("Created a desk")
  val P_FINISHED: AnswerState.Value               = Value("You are finished.")
  val TABLE_NOT_CORRECT: AnswerState.Value        = Value("Table looks not correct, move tiles to match the rules")
  val P_WON: AnswerState.Value                    = Value("One Player is the winner.")
  val UNDO_TAKE_TILE: AnswerState.Value           = Value("The tile has been put back in the bag")
  val BAG_IS_EMPTY: AnswerState.Value             = Value("No more tiles in the bag. You must lay a tile down")
  val CANT_MOVE_THIS_TILE: AnswerState.Value      = Value("You can not move this tile.")
  val UNDO_MOVED_TILE: AnswerState.Value          = Value("You undid the move of a specific tile.")
  val MOVED_TILE: AnswerState.Value               = Value("Moved tile if allowed.")
  val UNDO_LAY_DOWN_TILE: AnswerState.Value       = Value("You undid the lay down you took the tile up.")
  val ADDED_PLAYER: AnswerState.Value             = Value("You added a player.")
  val PUT_TILE_DOWN: AnswerState.Value            = Value("You put down a tile")
  val REMOVED_PLAYER: AnswerState.Value           = Value("You removed the player you inserted.")
  val INSERTING_NAMES_FINISHED: AnswerState.Value = Value("You finished inserting the names.")
  val STORED_FILE: AnswerState.Value              = Value("You stored the game in a file")
  val ENOUGH_PLAYER: AnswerState.Value = Value(
    "The Maximum amount of players is set. Type 'f' to finish inserting names")
  val NOT_ENOUGH_PLAYERS: AnswerState.Value       = Value("Not enough Players. Add some more.")
  val COULD_NOT_LOAD_FILE: AnswerState.Value      = Value("Could not load the file. Created a new game instead.")
  val LOADED_FILE: AnswerState.Value              = Value("You created a file")
  val CREATED_DESK: AnswerState.Value             = Value("You started the game by creating a desk")
  val UNDO_MOVED_TILE_NOT_DONE: AnswerState.Value = Value("Undo the move of the tile unnecessary. Nothing did happen.")
  val P_FINISHED_UNDO: AnswerState.Value          = Value("Its is again your turn. ")
  val TOOK_TILE: AnswerState.Value                = Value("Auto took a tile")
  val UNDO: AnswerState.Value                     = Value("Undid the last operation")
  val REDO: AnswerState.Value                     = Value("Undid the undo operation")
  val SWITCHED_TO_NEXT: AnswerState.Value         = Value("Switched to next")
}

object ControllerState extends Enumeration {
  val MENU, INSERTING_NAMES, P_TURN, NEXT_TYPE_N, KILL = Value
}
