package rummy.ui.impl

import model.deskComp.deskBaseImpl.TileInterface
import model.deskComp.deskBaseImpl.deskImpl.Tile
import rummy.controller.ControllerInterface
import rummy.ui.{UIConnector, UIInterface}
import rummy.util.{AnswerState, ControllerState, Observer}

import scala.collection.immutable.SortedSet

class Tui(var connector: UIConnector.type) extends UIInterface with Observer {
  connector.add(this)

  override def updated(controller: ControllerInterface): Unit =
    printOut(controller)

  override def processInput(input: String): Unit =
    connector.updateController(connector.controller.state match {
      case ControllerState.MENU => handleMenuInput(input)
      case ControllerState.INSERTING_NAMES => handleNameInput(input)
      case ControllerState.P_TURN => handleOnTurn(input)
      case ControllerState.NEXT_TYPE_N => handleNextPlayer(input)
    })

  private def handleMenuInput(input: String): ControllerInterface = input match {
    case "c" => connector.controller.createDesk(elements + 1)
    case "l" => connector.controller.loadFile()
    case _ => wrongInput()
  }

  private def handleNameInput(name: String): ControllerInterface = name match {
    case "f" => connector.controller.nameInputFinished()
    case "z" => connector.controller.undo()
    case "r" => connector.controller.redo()
    case PlayerNamePattern() => connector.controller.addPlayerAndInit(name.substring(4).trim, elements)
    case _ => wrongInput()
  }

  private def handleOnTurn(input: String): ControllerInterface = input match {
    case LayDownTilePattern(c) => connector.controller.layDownTile(Tile.stringToTile(c.split(" ").apply(1)).get);
    case MoveTilePattern(c) =>
      connector.controller.moveTile(Tile.stringToTile(c.split(" t ").apply(0).split(" ").apply(1)).get,
        Tile.stringToTile(c.split(" t ").apply(1)).get);
    case "f" => connector.controller.userFinishedPlay()
    case "z" => connector.controller.undo()
    case "r" => connector.controller.redo()
    case _ => wrongInput()
  }

  private def handleNextPlayer(input: String): ControllerInterface = input match {
    case "n" => connector.controller.switchToNextPlayer()
    case "s" => connector.controller.storeFile()
    case _ => wrongInput()
  }

  private def wrongInput(): ControllerInterface = {
    println("Could not identify your input. Are you sure it was correct'?")
    connector.controller
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
           | ${connector.controller.currentPlayerName} it's your turn. Do your stuff.${" " * (55 - connector.controller.currentPlayerName.length)}|
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
    println(answerState.toString)

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

  private def printCurrentBoardView(board: SortedSet[TileInterface]): Unit = {
    var s =
      s"""
         |---------------------------------------------------------------------------------------|
         | ${connector.controller.currentPlayerName} thats on your board.${" " * (65 - connector.controller.currentPlayerName.length)}|
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
