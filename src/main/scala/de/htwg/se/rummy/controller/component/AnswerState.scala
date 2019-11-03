package de.htwg.se.rummy.controller.component

object AnswerState extends Enumeration {
  val TAKE_TILE, UNDO_TAKE_TILE, BAG_IS_EMPTY, NONE, P_WON,
  TABLE_NOT_CORRECT, P_FINISHED, P_FINISHED_UNDO, CANT_MOVE_THIS_TILE,
  MOVED_TILE, UNDO_MOVED_TILE, P_DOES_NOT_OWN_TILE, UNDO_LAY_DOWN_TILE, PUT_TILE_DOWN
  , ADDED_PLAYER, PLAYER_REMOVED, CREATED_DESK, INSERTING_NAMES_FINISHED,
  NOT_ENOUGH_PLAYERS, STORED_FILE, LOADED_FILE, COULD_NOT_LOAD_FILE = Value

  //  def message(): Unit = {
  //    Value match {
  //      case AnswerState.P_DOES_NOT_OWN_TILE => "You dont have this tile on the board. Please select another one\n"
  //      case AnswerState.CREATED => "Desk created. Please type in 'name <name1>' where name1 is the first players name.\n"
  //      case AnswerState.TABLE_NOT_CORRECT => "Table looks not correct, please move tiles to match the rules\n"
  //      case AnswerState.START => "Start\n"
  //      case AnswerState.ENOUGH_PS => "The Maximum amount of players is set. Type 'f' to finish inserting names/n"
  //      case AnswerState.P_FINISHED => "You are finished. The next player has to type 'n' to continue,\nor type s to store the current game.\n"
  //      case AnswerState.P_TURN =>
  //        String.format(
  //          "|---------------------------------------------------------------------------------------|\n" +
  //            "| %50s it's your turn. Do your stuff.     |\n" +
  //            "|---------------------------------------------------------------------------------------|\n" +
  //            "| Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |\n" +
  //            "| Type 'm <valueA> <FirstLetterOfColorA> <numA> t <valueB> <FirstLetterOfColorB> <numB> |\n" +
  //            "|  to put A in where B is                                                               |\n" +
  //            "| Type 'f' to finish (and take a tile automatically if you did nothing)                 |\n" +
  //            "| Type 'z' to undo                                                                      |\n" +
  //            "| Type 'r' to redo                                                                      |\n" +
  //            "|---------------------------------------------------------------------------------------|\n\n",
  //          controller.getCurrentPlayer.name)
  //      case AnswerState.INSERTED_NAME => "Player " + controller.getAmountOfPlayers + " is added\n"
  //      case AnswerState.NOT_ENOUGH_PS => "Not enough Players. Type <c> to create a desk and insert names\n"
  //      case AnswerState.MENU => "You're finished. Great. Now type in 's' and enter to start.\n"
  //      case AnswerState.P_WON =>
  //        System.exit(0)
  //        String.format("%s is the winner.\n", controller.getCurrentPlayer.name)
  //      case AnswerState.PLAYER_REMOVED => "You removed the player you inserted .\n"
  //      case AnswerState.UNDO_LAY_DOWN_TILE => "You took the tile up.\n"
  //      case AnswerState.CANT_MOVE_THIS_TILE => "You can not move this tile.\n"
  //      case AnswerState.LOAD_FILE => "You loaded a previous game. You can start now.\n"
  //      case AnswerState.STORE_FILE => "You stored a game. Go on.\n"
  //      case AnswerState.COULD_NOT_LOAD_FILE => "No previous game found. A new desk was created.\n"
  //      case AnswerState.BAG_IS_EMPTY => "No more tiles in the bag. You must lay a tile down\n"
  //      case AnswerState.INSERTING_NAMES => "Type in 'name <the name of the player>' and confirm. (Min 2 players, Max 4) or finish with 'f'\n"
  //      case _ => ""
  //    }
  //  }

}
