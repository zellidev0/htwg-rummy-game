package view

import controller.Controller
import model.ContState
import util.Observer

import scala.util.matching.Regex

class Tui(contr: Controller) extends Observer {
  contr.add(this)

  val AmountOfPlayersPattern: Regex = "[2-4]".r
  val PlayerNamePattern: Regex = "name [A-Za-z]+".r
  val LayDownTilePattern: Regex = "(l [1-9][RBGY][01]|l 1[0123][RBGY][01])".r
  val MoveTilePattern: Regex = "(m [1-9][RBGY][01] t [1-9][RBGY][01]|m 1[0123][RBGY][01] t [1-9][RBYG][01]|m 1[0-3][RBGY][01] t 1[0-3][RBGY][01]|m [1-9][RBGY][01] 1[0-3][RBYG][01])".r


  def processInputLine(input: String): Unit = {
    contr.stateM match {
      case ContState.MENU => handleMenuInput(input)
      case ContState.INSERTING_NAMES => handleNameInput(input)
      case ContState.P_TURN => handleOnTurn(input)
      case ContState.P_FINISHED => handleOnTurnFinished(input)
    }
  }

  def handleNameInput(name: String): Unit = {
    name match {
      case "f" => contr.nameInputFinished()
      case PlayerNamePattern() => contr.addPlayerAndInit(name.substring(4).trim, 12)
      case _ => printWrongArgument()
    }
  }

  def handleOnTurnFinished(input: String): Unit = input match {
    case "n" => contr.switchToNextPlayer();
    case _ => printWrongArgument()
  }

  def handleOnTurn(input: String): Unit = {
    input match {
      case LayDownTilePattern(c) => contr.layDownTile(c.split(" ").apply(1));
      case MoveTilePattern(c) => contr.moveTile(c.split(" t ").apply(0).split(" ").apply(1), c.split(" t ").apply(1));
      case "f" => contr.userFinishedPlay()
      case _ => printWrongArgument()
    }
  }

  def handleMenuInput(input: String): Unit = {
    input match {
      case "q" =>
      case "c" => contr.createDesk(13);
      case _ => printWrongArgument()
    }
  }

  override def update: Boolean = {
    contr.stateM match {
      case ContState.P_DOES_NOT_OWN_TILE => println("\tNEWS:\tYou dont have this tile on the board. Please select another one")
      case ContState.CREATED => println("\tNEWS:\tDesk created. Please type in 'name <name1>' where name1 is the first players name. Confirm with enter")
      case ContState.TABLE_NOT_CORRECT => println("\tNEWS:\tTable looks not correct, please move tiles to match the rules")
      case ContState.START => println("SSSSS  TTTTT    A    RRRR   TTTTT \nSS       T     A A   R   R    T   \nSSSSS    T    A   A  RRRR     T   \n   SS    T    AAAAA  RRR      T   \nSSSSS    T    A   A  R  RR    T   \n")
      case ContState.ENOUGH_PS => println("\tNEWS:\tThe Maximum amount of players is set. Type 'f' to finish inserting names")
      case ContState.P_FINISHED => printPlayerFinished()
      case ContState.P_TURN => printWhatUserCanDo(); printUserBoard(); printTable()
      case ContState.INSERTED_NAME => println("\tNEWS:\tPlayer " + contr.getAmountOfPlayers + " is added\n\tNEWS:\tType in another players name and confirm with enter (Min 2 players, Max 4) or finish with 'f'")
      case ContState.NOT_ENOUGH_PS => println("\tNEWS:\tNot enough Players. Type <c> to create a desk and insert names")
      case ContState.MENU => println("\tNEWS:\tYou're finished. Great. Now type in 's' and enter to start.")
      case ContState.P_WON => printPlayerWon()
      case _ =>
    }
    true
  }

  def printPlayerWon(): Unit = printf("FFFFFF  I  NN   N  I  SSSSS  H   H  EEEEE  DDD\nF       I  N N  N  I  SS     H   H  E      D  D\nFFFFFF  I  N  N N  I  SSSSS  HHHHH  EEEEE  D   D\nF       I  N  N N  I     SS  H   H  E      D  D\nF       I  N   NN  I  SSSSS  H   H  EEEEE  DDD\n%s is the winner.", contr.currentP)

  def printPlayerFinished(): Unit = println("\tNEWS:\tYou are finished. The next player has to type 'n' to continue.");

  def printUserBoard(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %20s thats on your board.                                             |\n" +
        "|---------------------------------------------------------------------------------------|\n",
      contr.currentP.name)
    for (_ <- contr.currentP.board.tiles) {
      print("____ ")
    }
    println()
    for (tile <- contr.currentP.board.tiles) {
      printf("|%2s| ", tile.value)
    }
    println()
    for (tile <- contr.currentP.board.tiles) {
      printf("|%s | ", tile.color.toString.charAt(0))
    }
    println()
    for (tile <- contr.currentP.board.tiles) {
      printf("|%s | ", tile.ident)
    }
    println()
    for (_ <- contr.currentP.board.tiles) {
      print("\u203E\u203E\u203E\u203E ")
    }
    println()


  }

  def printTable(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %20s thats on the desk.                                               |\n" +
        "|---------------------------------------------------------------------------------------|\n\n",
      contr.currentP.name)
    for (sortedSet <- contr.getTileSet) {
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

  def printWrongArgument(): Unit = println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")

  def printWhatUserCanDo(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %50s it's your turn. Do your stuff.     |\n" +
        "|---------------------------------------------------------------------------------------|\n" +
        "| Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |\n" +
        "| Type 'm <valueA> <FirstLetterOfColorA> <numA> l <valueB> <FirstLetterOfColorB> <numB> |\n" +
        "|  to put A in where B is                                                               |\n" +
        "| Type 'f' to finish (and take a tile automatically if you did nothing)                 |\n" +
        "|---------------------------------------------------------------------------------------|\n\n",
      contr.currentP.name)
  }
}
