package de.htwg.se.rummy.view.component

import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.{AnswerState, ControllerState}
import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.Tile
import de.htwg.se.rummy.view.UIInterface

import scala.collection.immutable.SortedSet

class Tui(controller: ControllerInterface) extends UIInterface {

  controller.add(this)

  override def processInput(input: String): Unit = {
    if (input.equals("q")) {
      System.exit(0)
    }
    controller.currentControllerState match {
      case MENU => handleMenuInput(input)
      case INSERTING_NAMES => handleNameInput(input)
      case P_TURN => handleOnTurn(input)
      case NEXT_TYPE_N => handleOnTurnFinished(input)
    }
  }

  override def handleNameInput(name: String): Unit = {
    name match {
      case "f" => controller.nameInputFinished()
      case "z" => controller.undo()
      case "r" => controller.redo()
      case PlayerNamePattern() => controller.addPlayerAndInit(name.substring(4).trim, elements)
      case _ => wrongInput()
    }
  }

  private def wrongInput() {
    println("Could not identify your input. Are you sure it was correct'?")
  }

  override def handleOnTurnFinished(input: String): Unit = input match {
    case "n" => controller.switchToNextPlayer()
    case "s" => controller.storeFile()
    case _ => wrongInput()
  }

  override def handleOnTurn(input: String): Unit = {
    input match {
      case LayDownTilePattern(c) =>
        controller.layDownTile(Tile.stringToTile(c.split(" ").apply(1)).get);
      case MoveTilePattern(c) =>
        controller.moveTile(Tile.stringToTile(c.split(" t ").apply(0).split(" ").apply(1)).get,
          Tile.stringToTile(c.split(" t ").apply(1)).get);
      case "f" => controller.userFinishedPlay()
      case "z" => controller.undo()
      case "r" => controller.redo()
      case _ => wrongInput()
    }
  }

  override def handleMenuInput(input: String): Unit = {
    input match {
      case "c" => controller.createDesk(elements + 1)
      case "l" => controller.loadFile()
      case _ => wrongInput()
    }
  }


  override def update(): Unit = {
    printAnswerState(controller.currentAnswerState)
    printCurrentStateView(controller.currentControllerState)
    if (controller.currentControllerState == ControllerState.P_TURN) {
      printCurrentTableView(controller.viewOfTable)
      printCurrentBoardView(controller.viewOfBoard)
    }
  }

  def printCurrentStateView(currentState: ControllerState.Value): Unit = {
    println(currentState match {
      case ControllerState.P_TURN =>
        s"""
           |---------------------------------------------------------------------------------------|
           | ${controller.getCurrentPlayer.name} it's your turn. Do your stuff.${" " * (55 - controller.getCurrentPlayer.name.length)}|
           |---------------------------------------------------------------------------------------|
           | Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |
           | Type 'm <valueA> <FirstLetterOfColorA> <numA> t <valueB> <FirstLetterOfColorB> <numB> |
           |     to put A in where B is                                                            |
           | Type 'f' to finish (and take a tile automatically if you did nothing)                 |
           | Type 'z' to undo                                                                      |
           | Type 'r' to redo                                                                      |
           |---------------------------------------------------------------------------------------|"""
      case ControllerState.KILL =>
        System.exit(0)
      case ControllerState.INSERTING_NAMES =>
        "Type in 'name <the name of the player>' and confirm with enter. (Min 2 players, Max 4) or finish with 'f'"
      case ControllerState.NEXT_TYPE_N =>
        "The next player has to type 'n' to continue, or type s to store the current game."
      case _ => ""
    })
  }

  def printAnswerState(answerState: AnswerState.Value): Unit = {
    println(answerState match {
      case AnswerState.P_FINISHED =>
        "You are finished."
      case AnswerState.TABLE_NOT_CORRECT =>
        "Table looks not correct, please move tiles to match the rules or undo until it matches"
      case AnswerState.P_WON =>
        s"${controller.getCurrentPlayer.name} is the winner."
      case AnswerState.UNDO_TAKE_TILE =>
        "The tile has been put back in the bag"
      case AnswerState.BAG_IS_EMPTY =>
        "No more tiles in the bag. You must lay a tile down"
      case AnswerState.CANT_MOVE_THIS_TILE =>
        "You can not move this tile."
      case AnswerState.UNDO_MOVED_TILE =>
        "You undid the move of a specific tile."
      case AnswerState.MOVED_TILE =>
        "You did move a tile to another when possible."
      case AnswerState.UNDO_LAY_DOWN_TILE =>
        "You undid the lay down you took the tile up."
      case AnswerState.ADDED_PLAYER =>
        "You added a player."
      case AnswerState.PUT_TILE_DOWN =>
        "You put down a tile"
      case AnswerState.REMOVED_PLAYER =>
        "You removed the player you inserted."
      case AnswerState.P_DOES_NOT_OWN_TILE =>
        "You dont have this tile on the board. Please select another one"
      case AnswerState.INSERTING_NAMES_FINISHED =>
        "You finished inserting the names."
      case AnswerState.STORED_FILE =>
        "You stored the game in a file"
      case AnswerState.ENOUGH_PLAYER =>
        "The Maximum amount of players is set. Type 'f' to finish inserting names"
      case AnswerState.NOT_ENOUGH_PLAYERS =>
        "Not enough Players. Add some more."
      case AnswerState.COULD_NOT_LOAD_FILE =>
        "Could not load the file. Created a new game instead."
      case AnswerState.LOADED_FILE =>
        "You loaded a file"
      case AnswerState.CREATED_DESK =>
        "You started the game by creating a desk"
      case AnswerState.UNDO_MOVED_TILE_NOT_DONE =>
        "Undo the move of the tile unnecessary. Nothing did happen."
      case AnswerState.P_FINISHED_UNDO =>
        "Its is again your turn. "
      case _ => ""
    })
  }


  def printCurrentTableView(desk: Set[SortedSet[TileInterface]]): Unit = {

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
         | ${controller.getCurrentPlayer.name} thats on your board.${" " * (65 - controller.getCurrentPlayer.name.length)}|
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
