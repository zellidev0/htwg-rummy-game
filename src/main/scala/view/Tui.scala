package view

import controller.Controller
import model.ControllerState
import util.Observer

import scala.util.matching.Regex

class Tui(controller: Controller) extends Observer {
  controller.add(this)

  val AmountOfPlayersPattern: Regex = "[2-4]".r
  val PlayerNamePattern: Regex = "name [A-Za-z]+".r
  val LayDownTilePattern: Regex = "l [1-9][RBGY][01]|l 1[0123][RBGY][01]".r
  val MoveTilePattern: Regex = "m [1-9][RBGY][01] t [1-9][RBGY][01]|m 1[0123][RBGY][01] t [1-9][RBYG][01]|m 1[0-3][RBGY][01] t 1[0-3][RBGY][01]|m [1-9][RBGY][01] 1[0-3][RBYG][01]".r

  def processInputLine(input: String): Unit = {
    controller.state match {
      case ControllerState.MENU => handleMenuInput(input)
      case ControllerState.INSERTING_NAMES => handleNameInput(input)
      case ControllerState.TAKE_OR_PLAY => handleOnTurnTakeOrPlay(input)
      case ControllerState.PLAY => handleOnTurnPlay(input)
      case ControllerState.PLAYER_FINISHED => handleOnTurnFinished(input)
    }
  }

  def handleNameInput(name: String): Unit = {
    name match {
      case "f" =>
        if (controller.hasCorrectAmountOfPlayers) {
          controller.switchControllerState(ControllerState.MENU)
          println("\tNEWS:\tYou're finished. Great. Now type in 's' and enter to start.")
        } else {
          println("\tNEWS:\tNot enough Players. Please insert another name")
        }
      case PlayerNamePattern() =>
        if (controller.hasLessThan4Players) {
          controller.addPlayerAndInit(name.substring(4).trim)
          println("\tNEWS:\tPlayer " + controller.getAmountOfPlayers + " is named " + name.substring(4).trim + "\n" +
            "\tNEWS:\tType in another players name and confirm with enter (Min 2 players, Max 4) or finish with 'f'")
        } else {
          println("\tNEWS:\tThe Maximum amount of players is set. Type 'f' to finish inserting names")
        }
      case _ => println("\tNEWS:\tCould not identify your input. Are you sure it was in the format 'name <name1>'?")
    }
  }

  def handleOnTurnTakeOrPlay(input: String): Unit = {
    input match {
      case "t" => controller.takeATile()
        controller.switchControllerState(ControllerState.PLAYER_FINISHED)
        println("\tNEWS:\tYou took a tile, you are finished. The next player has to type 'n' to continue.");
      case "p" =>
        println("\tNEWS:\tYou decided to play");
        controller.switchControllerState(ControllerState.PLAY)
        update
      case _ => println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")

    }
  }

  def handleOnTurnPlay(input: String): Unit = {
    input match {
      case LayDownTilePattern(c) => controller.layDownTile(c.split(" ").apply(1)); update
      case MoveTilePattern(c) => controller.moveTile(c.split(" t ").apply(0).split(" ").apply(1), c.split(" t ").apply(1)); update
      case "f" =>
        println("\tNEWS:\tYou are finished. The next player has to type 'n' to continue."); controller.switchControllerState(ControllerState.PLAYER_FINISHED)
      case _ => println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")
    }
  }

  def handleOnTurnFinished(input: String): Unit = input match {
    case "n" => controller.switchToNextPlayer()
      controller.switchControllerState(ControllerState.TAKE_OR_PLAY)
      update
    case _ => println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")
  }

  def handleMenuInput(input: String): Unit = {
    input match {
      case "q" =>
      case "c" =>
        controller.createDesk(13)
        controller.switchControllerState(ControllerState.INSERTING_NAMES)
        println("\tNEWS:\tDesk created. Please type in 'name <name1>' where name1 is the first players name. Confirm with enter")
      case "s" =>
        if (controller.hasCorrectAmountOfPlayers) {
          println("" +
            "SSSSS  TTTTT    A    RRRR   TTTTT \n" +
            "SS       T     A A   R   R    T   \n" +
            "SSSSS    T    A   A  RRRR     T   \n" +
            "   SS    T    AAAAA  RRR      T   \n" +
            "SSSSS    T    A   A  R  RR    T   \n\n\n\n")
          controller.switchControllerState(ControllerState.TAKE_OR_PLAY)
          update
        } else {
          println("\tNEWS:\tNot enough Players. Type <c> to create a desk and insert names")
        }
      case _ => println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")
    }
  }


  def printHandleOnTurnTakeOrPlay = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %15s it's your turn. Decide! Down there you see your Board and the Table   |\n" +
        "|---------------------------------------------------------------------------------------|\n" +
        "| Type 't' to take a tile                                                               |\n" +
        "| Type 'p' to play and move tiles, lay down etc.                                        |\n" +
        "|---------------------------------------------------------------------------------------|\n\n",
      controller.currentP.name)
  }

  override def update: Boolean = {
    controller.state match {
      case ControllerState.TAKE_OR_PLAY =>
        printHandleOnTurnTakeOrPlay
        printUserBoard()
        printTable()
      case ControllerState.PLAYER_FINISHED =>

      case ControllerState.PLAY =>
        printWahtUserCanDo()
        printUserBoard()
        printTable()
      case ControllerState.INSERTING_NAMES =>

      case ControllerState.MENU =>
    }
    true
  }


  def printUserBoard(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %20s thats on your board.                                             |\n" +
        "|---------------------------------------------------------------------------------------|\n",
      controller.currentP.name)
    for (_ <- controller.currentP.board.tiles) {
      print("____ ")
    }
    println()
    for (tile <- controller.currentP.board.tiles) {
      printf("|%2s| ", tile.value)
    }
    println()
    for (tile <- controller.currentP.board.tiles) {
      printf("|%s | ", tile.color.toString.charAt(0))
    }
    println()
    for (tile <- controller.currentP.board.tiles) {
      printf("|%s | ", tile.ident)
    }
    println()
    for (tile <- controller.currentP.board.tiles) {
      print("\u203E\u203E\u203E\u203E ")
    }
    println()


  }

  def printTable(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %20s thats on the desk.                                               |\n" +
        "|---------------------------------------------------------------------------------------|\n\n",
      controller.currentP.name)
    for (sortedSet <- controller.getTileSet) {
      for (_ <- sortedSet) {
        print("____ ")
      }
      println()
      for (tile <- sortedSet) {
        printf("|%2s| ", tile.value)
      }
      println()
      for (tile <- sortedSet) {
        printf("|%s | ", tile.color.toString.charAt(0))
      }
      println()
      for (tile <- sortedSet) {
        printf("|%s | ", tile.ident)
      }
      println()
      for (tile <- sortedSet) {
        print("\u203E\u203E\u203E\u203E ")
      }
      println()
    }
  }

  def printWahtUserCanDo(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %50s it's your turn. Do your stuff.     |\n" +
        "|---------------------------------------------------------------------------------------|\n" +
        "| Type 't' to take a tile                                                               |\n" +
        "| Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |\n" +
        "| Type 'm <valueA> <FirstLetterOfColorA> <numA> l <valueB> <FirstLetterOfColorB> <numB> |\n" +
        "|  to put A in where B is                                                               |\n" +
        "| Type 'f' when you are finished with putting                                           |\n" +
        "|---------------------------------------------------------------------------------------|\n\n",
      controller.currentP.name)
  }

}
