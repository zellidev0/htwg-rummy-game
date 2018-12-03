package view

import controller.Controller
import model.ContState
import util.Observer

import scala.util.matching.Regex

class Tui(controller: Controller) extends Observer {
  controller.add(this)

  val AmountOfPlayersPattern: Regex = "[2-4]".r
  val PlayerNamePattern: Regex = "name [A-Za-z]+".r
  val LayDownTilePattern: Regex = "(l [1-9][RBGY][01]|l 1[0123][RBGY][01])".r
  val MoveTilePattern: Regex = "(m [1-9][RBGY][01] t [1-9][RBGY][01]|m 1[0123][RBGY][01] t [1-9][RBYG][01]|m 1[0-3][RBGY][01] t 1[0-3][RBGY][01]|m [1-9][RBGY][01] 1[0-3][RBYG][01])".r


  def processInputLine(input: String): Unit = {
    controller.state match {
      case ContState.MENU => handleMenuInput(input)
      case ContState.INSERTING_NAMES => handleNameInput(input)
      case ContState.PLAYER_TURN => handleOnTurn(input)
      case ContState.PLAYER_FINISHED => handleOnTurnFinished(input)
    }
  }

  def handleNameInput(name: String): Unit = {
    name match {
      case "f" => controller.nameInputFinished()
      case PlayerNamePattern() => controller.addPlayerAndInit(name.substring(4).trim, 12)
      case _ => printWrongArgument()
    }
  }

  def printWrongArgument(): Unit = println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")

  def handleOnTurnFinished(input: String): Unit = input match {
    case "n" => controller.switchToNextPlayer();
    case _ => printWrongArgument()
  }

  def handleOnTurn(input: String): Unit = {
    input match {
      case LayDownTilePattern(c) => controller.layDownTile(c.split(" ").apply(1));
      case MoveTilePattern(c) => controller.moveTile(c.split(" t ").apply(0).split(" ").apply(1), c.split(" t ").apply(1));
      case "f" => controller.userFinishedPlay()
      case _ => printWrongArgument()
    }
  }

  def handleMenuInput(input: String): Unit = {
    input match {
      case "q" =>
      case "c" => controller.createDesk(13);
      case _ => printWrongArgument()
    }
  }

  override def update: Boolean = {
    controller.state match {
      case ContState.CREATED =>
        println("\tNEWS:\tDesk created. Please type in 'name <name1>' where name1 is the first players name. Confirm with enter")
        controller.swState(ContState.INSERTING_NAMES)
      case ContState.TABLE_NOT_CORRECT =>
        println("\tNEWS:\tTable looks not correct, please move tiles to match the rules")
        controller.swState(ContState.PLAYER_TURN)
      case ContState.START =>
        println("" +
          "SSSSS  TTTTT    A    RRRR   TTTTT \n" +
          "SS       T     A A   R   R    T   \n" +
          "SSSSS    T    A   A  RRRR     T   \n" +
          "   SS    T    AAAAA  RRR      T   \n" +
          "SSSSS    T    A   A  R  RR    T   \n\n\n\n")
        controller.swState(ContState.PLAYER_TURN)
      case ContState.ENOUGH_PLAYERS =>
        println("\tNEWS:\tThe Maximum amount of players is set. Type 'f' to finish inserting names")
        controller.swState(ContState.INSERTING_NAMES)
      case ContState.INSERTING_NAMES =>
      case ContState.PLAYER_FINISHED =>
        printPlayerFinished()
      case ContState.PLAYER_TURN =>
        printWahtUserCanDo()
        printUserBoard()
        printTable()
      case ContState.INSERTED_NAME =>
        println("\tNEWS:\tPlayer " + controller.getAmountOfPlayers + " is added\n" +
          "\tNEWS:\tType in another players name and confirm with enter (Min 2 players, Max 4) or finish with 'f'")
        controller.swState(ContState.INSERTING_NAMES)
      case ContState.NOT_ENOUGH_PLAYERS =>
        println("\tNEWS:\tNot enough Players. Type <c> to create a desk and insert names")
        controller.swState(ContState.INSERTING_NAMES)
      case ContState.MENU => println("\tNEWS:\tYou're finished. Great. Now type in 's' and enter to start.")
      case ContState.PLAYER_WON => printPlayerWon()
        controller.swState(ContState.MENU)

    }
    true
  }

  def printPlayerWon() = {
    println("FFFFFF  I  NN   N  I  SSSSS  H   H  EEEEE  DDD                                          " +
      "F       I  N N  N  I  SS     H   H  E      D  D                                         " +
      "FFFFFF  I  N  N N  I  SSSSS  HHHHH  EEEEE  D   D                                        " +
      "F       I  N  N N  I     SS  H   H  E      D  D                                         " +
      "F       I  N   NN  I  SSSSS  H   H  EEEEE  DDD                                          \n\n" +
      controller.currentP + "is the winner.")
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

  def printPlayerFinished() = println("\tNEWS:\tYou are finished. The next player has to type 'n' to continue.");

  def printWahtUserCanDo(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %50s it's your turn. Do your stuff.     |\n" +
        "|---------------------------------------------------------------------------------------|\n" +
        "| Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |\n" +
        "| Type 'm <valueA> <FirstLetterOfColorA> <numA> l <valueB> <FirstLetterOfColorB> <numB> |\n" +
        "|  to put A in where B is                                                               |\n" +
        "| Type 'f' to finish (and take a tile automatically if you did nothing)                 |\n" +
        "|---------------------------------------------------------------------------------------|\n\n",
      controller.currentP.name)
  }
}
