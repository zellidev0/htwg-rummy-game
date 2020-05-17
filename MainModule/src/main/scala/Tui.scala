import model.deskComp.deskBaseImpl.TileInterface
import model.deskComp.deskBaseImpl.deskImpl.Tile

import scala.collection.immutable.SortedSet

class Tui(var connector: UIConnector.type) extends UIInterface with Observer {
  connector.add(this)

  override def updated(controller: ControllerInterface): Unit =
    printOut(controller)
  override def processInput(input: String): Unit =
    connector.updateController(connector.contr.state match {
      case ControllerState.MENU            => handleMenuInput(input)
      case ControllerState.INSERTING_NAMES => handleNameInput(input)
      case ControllerState.P_TURN          => handleOnTurn(input)
      case ControllerState.NEXT_TYPE_N     => handleOnTurnFinished(input)
    })

  override def handleNameInput(name: String): ControllerInterface = name match {
    case "f"                 => connector.contr.nameInputFinished()
    case "z"                 => connector.contr.undo()
    case "r"                 => connector.contr.redo()
    case PlayerNamePattern() => connector.contr.addPlayerAndInit(name.substring(4).trim, elements)
    case _                   => wrongInput()
  }
  override def handleOnTurnFinished(input: String): ControllerInterface = input match {
    case "n" => connector.contr.switchToNextPlayer()
    case "s" => connector.contr.storeFile()
    case _   => wrongInput()
  }
  private def wrongInput(): ControllerInterface = {
    println("Could not identify your input. Are you sure it was correct'?")
    connector.contr
  }
  override def handleOnTurn(input: String): ControllerInterface = input match {
    case LayDownTilePattern(c) => connector.contr.layDownTile(Tile.stringToTile(c.split(" ").apply(1)).get);
    case MoveTilePattern(c) =>
      connector.contr.moveTile(Tile.stringToTile(c.split(" t ").apply(0).split(" ").apply(1)).get,
                               Tile.stringToTile(c.split(" t ").apply(1)).get);
    case "f" => connector.contr.userFinishedPlay()
    case "z" => connector.contr.undo()
    case "r" => connector.contr.redo()
    case _   => wrongInput()
  }
  override def handleMenuInput(input: String): ControllerInterface = input match {
    case "c" => connector.contr.createDesk(elements + 1)
    case "l" => connector.contr.loadFile()
    case _   => wrongInput()
  }

  private def printOut(controller: ControllerInterface): ControllerInterface = {
    printAnswerState(controller.answer)
    printCurrentStateView(controller.state)
    if (controller.state == ControllerState.P_TURN) {
      printCurrentTableView(controller.viewOfTable)
      printCurrentBoardView(controller.viewOfBoard)
    }
    controller
  }
  private def printCurrentStateView(currentState: ControllerState.Value): Unit =
    println(currentState match {
      case ControllerState.P_TURN =>
        s"""
           |---------------------------------------------------------------------------------------|
           | ${connector.contr.currentPlayerName} it's your turn. Do your stuff.${" " * (55 - connector.contr.currentPlayerName.length)}|
           |---------------------------------------------------------------------------------------|
           | Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |
           | Type 'm <valueA> <FirstLetterOfColorA> <numA> t <valueB> <FirstLetterOfColorB> <numB> |
           |     to put A in where B is                                                            |
           | Type 'f' to finish (and take a tile automatically if you did nothing)                 |
           | Type 'z' to undo                                                                      |
           | Type 'r' to redo                                                                      |
           |---------------------------------------------------------------------------------------|"""
      case ControllerState.KILL => System.exit(0)
      case ControllerState.INSERTING_NAMES =>
        "Type in 'name <the name of the player>' and confirm with enter. (Min 2 players, Max 4) or finish with 'f'"
      case ControllerState.NEXT_TYPE_N =>
        "The next player has to type 'n' to continue, or type s to store the current game."
      case _ => ""
    })
  private def printAnswerState(answerState: AnswerState.Value): Unit =
    println(answerState match {
      case AnswerState.CREATE_DESK              => "Create a desk"
      case AnswerState.P_FINISHED               => "You are finished."
      case AnswerState.TABLE_NOT_CORRECT        => "Table looks not correct, move tiles to match the rules"
      case AnswerState.P_WON                    => s"${connector.contr.currentPlayerName} is the winner."
      case AnswerState.UNDO_TAKE_TILE           => "The tile has been put back in the bag"
      case AnswerState.BAG_IS_EMPTY             => "No more tiles in the bag. You must lay a tile down"
      case AnswerState.CANT_MOVE_THIS_TILE      => "You can not move this tile."
      case AnswerState.UNDO_MOVED_TILE          => "You undid the move of a specific tile."
      case AnswerState.MOVED_TILE               => "Moved tile if allowed."
      case AnswerState.UNDO_LAY_DOWN_TILE       => "You undid the lay down you took the tile up."
      case AnswerState.ADDED_PLAYER             => "You added a player."
      case AnswerState.PUT_TILE_DOWN            => "You put down a tile"
      case AnswerState.REMOVED_PLAYER           => "You removed the player you inserted."
      case AnswerState.INSERTING_NAMES_FINISHED => "You finished inserting the names."
      case AnswerState.STORED_FILE              => "You stored the game in a file"
      case AnswerState.ENOUGH_PLAYER            => "The Maximum amount of players is set. Type 'f' to finish inserting names"
      case AnswerState.NOT_ENOUGH_PLAYERS       => "Not enough Players. Add some more."
      case AnswerState.COULD_NOT_LOAD_FILE      => "Could not load the file. Created a new game instead."
      case AnswerState.LOADED_FILE              => "You loaded a file"
      case AnswerState.CREATED_DESK             => "You started the game by creating a desk"
      case AnswerState.UNDO_MOVED_TILE_NOT_DONE => "Undo the move of the tile unnecessary. Nothing did happen."
      case AnswerState.P_FINISHED_UNDO          => "Its is again your turn. "
      case AnswerState.TOOK_TILE                => "Auto took a tile"
      case z                                    => "ERROR" + z.toString
    })
  private def printCurrentTableView(desk: Set[SortedSet[TileInterface]]): Unit = {
    var s =
    """
        |---------------------------------------------------------------------------------------|
        | That's on the desk.                                                                   |
        |---------------------------------------------------------------------------------------|""" + "\n"
    for (sortedSet <- desk) {
      for (_ <- sortedSet) {
        s = s + "____ "
      }
      s += "\n"
      for (tile <- sortedSet) {
        s += String.format("|%2s| ", tile.value.toString)
      }
      s += "\n"
      for (tile <- sortedSet) {
        s += String.format("|%s | ", tile.color.toString.charAt(0).toString)
      }
      s += "\n"
      for (tile <- sortedSet) {
        s += String.format("|%s | ", tile.ident.toString)
      }
      s += "\n"
      for (_ <- sortedSet) {
        s += String.format("\u203E\u203E\u203E\u203E ")
      }
      s += "\n"
    }
    println(s)
  }
  def printCurrentBoardView(board: SortedSet[TileInterface]): Unit = {
    var s =
    s"""
         |---------------------------------------------------------------------------------------|
         | ${connector.contr.currentPlayerName} thats on your board.${" " * (65 - connector.contr.currentPlayerName.length)}|
         |---------------------------------------------------------------------------------------|""" + "\n"
    for (_ <- board) {
      s += "____ "
    }
    s += "\n"
    for (tile <- board) {
      s += String.format("|%2s| ", tile.value.toString)
    }
    s += "\n"
    for (tile <- board) {
      s += String.format("|%s | ", tile.color.toString.charAt(0).toString)
    }
    s += "\n"
    for (tile <- board) {
      s += String.format("|%s | ", tile.ident.toString)
    }
    s += "\n"
    for (_ <- board) {
      s += String.format("\u203E\u203E\u203E\u203E ")
    }
    s += "\n"
    println(s)
  }

}
