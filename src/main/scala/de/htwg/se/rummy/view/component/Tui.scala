package de.htwg.se.rummy.view.component

import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.ControllerState
import de.htwg.se.rummy.view.UIInterface

class Tui(controller: ControllerInterface) extends UIInterface {

  controller.add(this)

  override def processInput(input: String): Unit = {
    if (input.equals("q")) {
      System.exit(0)
    }
    controller.controllerState match {
      case ControllerState.MENU => handleMenuInput(input)
      case ControllerState.INSERTING_NAMES => handleNameInput(input)
      case ControllerState.P_TURN => handleOnTurn(input)
      case ControllerState.P_FINISHED => handleOnTurnFinished(input)
    }
  }

  override def handleNameInput(name: String): Unit = {
    name match {
      case "f" => controller.nameInputFinished()
      case "z" => controller.undo()
      case "r" => controller.redo()
      case PlayerNamePattern() => controller.addPlayerAndInit(name.substring(4).trim, elements)
      case _ => printWrongArgument()
    }
  }

  override def handleOnTurnFinished(input: String): Unit = input match {
    case "n" => controller.switchToNextPlayer()
    case "s" => controller.storeFile()
    case _ => printWrongArgument()
  }

  override def handleOnTurn(input: String): Unit = {
    input match {
      case LayDownTilePattern(c) => controller.layDownTile(c.split(" ").apply(1));
      case MoveTilePattern(c) => controller.moveTile(c.split(" t ").apply(0).split(" ").apply(1), c.split(" t ").apply(1));
      case "f" => controller.userFinishedPlay()
      case "z" => controller.undo()
      case "r" => controller.redo()
      case _ => printWrongArgument()
    }
  }

  override def handleMenuInput(input: String): Unit = {
    input match {
      case "c" => controller.createDesk(elements + 1)
      case "l" => controller.loadFile
      case _ => printWrongArgument()
    }
  }

  private def printWrongArgument(): Unit = println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")

  override def update: Unit = {
    controller.controllerState match {
      case ControllerState.P_DOES_NOT_OWN_TILE => println("\tNEWS:\tYou dont have this tile on the board. Please select another one")
      case ControllerState.CREATED => println("\tNEWS:\tDesk created. Please type in 'name <name1>' where name1 is the first players name. Confirm with enter")
      case ControllerState.TABLE_NOT_CORRECT => println("\tNEWS:\tTable looks not correct, please move tiles to match the rules")
      case ControllerState.START => println("SSSSS  TTTTT    A    RRRR   TTTTT \nSS       T     A A   R   R    T   \nSSSSS    T    A   A  RRRR     T   \n   SS    T    AAAAA  RRR      T   \nSSSSS    T    A   A  R  RR    T   \n")
      case ControllerState.ENOUGH_PS => println("\tNEWS:\tThe Maximum amount of players is set. Type 'f' to finish inserting names")
      case ControllerState.P_FINISHED => println("\tNEWS:\tYou are finished. The next player has to type 'n' to continue,\nor type s to store the current game.")
      case ControllerState.P_TURN =>
        printf(
          "|---------------------------------------------------------------------------------------|\n" +
            "| %50s it's your turn. Do your stuff.     |\n" +
            "|---------------------------------------------------------------------------------------|\n" +
            "| Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |\n" +
            "| Type 'm <valueA> <FirstLetterOfColorA> <numA> t <valueB> <FirstLetterOfColorB> <numB> |\n" +
            "|  to put A in where B is                                                               |\n" +
            "| Type 'f' to finish (and take a tile automatically if you did nothing)                 |\n" +
            "|---------------------------------------------------------------------------------------|\n\n",
          controller.currentP.name)
        printUserBoard()
        printTable()
      case ControllerState.INSERTED_NAME => println("\tNEWS:\tPlayer " + controller.getAmountOfPlayers + " is added\n\tNEWS:\tType in another players name and confirm with enter (Min 2 players, Max 4) or finish with 'f'")
      case ControllerState.NOT_ENOUGH_PS => println("\tNEWS:\tNot enough Players. Type <c> to create a desk and insert names")
      case ControllerState.MENU => println("\tNEWS:\tYou're finished. Great. Now type in 's' and enter to start.")
      case ControllerState.P_WON =>
        printf("FFFFFF  I  NN   N  I  SSSSS  H   H  EEEEE  DDD\nF       I  N N  N  I  SS     H   H  E      D  D\nFFFFFF  I  N  N N  I  SSSSS  HHHHH  EEEEE  D   D\nF       I  N  N N  I     SS  H   H  E      D  D\nF       I  N   NN  I  SSSSS  H   H  EEEEE  DDD\n\n\n%s is the winner.\n", controller.currentP.name)
        System.exit(0)
      case ControllerState.PLAYER_REMOVED => println("\tNEWS:\tYou removed the player you inserted .")
      case ControllerState.UNDO_LAY_DOWN_TILE => println("\tNEWS:\tYou took the tile up.")
      case ControllerState.CANT_MOVE_THIS_TILE => println("\tNEWS:\tYou cant move this tile.")
      case ControllerState.LOAD_FILE => println("\tNEWS:\tYou loaded a previous game. You can start now.")
      case ControllerState.STORE_FILE => println("\tNEWS:\tYou stored a game. Go on.")
      case ControllerState.COULD_NOT_LOAD_FILE => println("\tNEWS:\tNo previous game found. A new desk was created.")
      case ControllerState.BAG_IS_EMPTY => println("\tNEWS:\tNo more tiles in the bag. You must lay a tile down")
      case _ =>
    }
  }

  private def printUserBoard(): Unit = {
    printf(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %20s thats on your board.                                             |\n" +
        "|---------------------------------------------------------------------------------------|\n",
      controller.currentP.name)
    for (_ <- controller.viewOfBoard) {
      print("____ ")
    }
    println()
    for (tile <- controller.viewOfBoard) {
      printf("|%2s| ", tile.value)
    }
    println()
    for (tile <- controller.viewOfBoard) {
      printf("|%s | ", tile.color.toString.charAt(0))
    }
    println()
    for (tile <- controller.viewOfBoard) {
      printf("|%s | ", tile.ident)
    }
    println()
    for (_ <- controller.viewOfBoard) {
      print("\u203E\u203E\u203E\u203E ")
    }
    println()


  }

  private def printTable(): Unit = {
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


}