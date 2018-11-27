package view

import controller.Controller
import model.ControllerState
import util.Observer

import scala.util.matching.Regex

class Tui(controller: Controller) extends Observer {
  controller.add(this)

  val AmountOfPlayersPattern: Regex = "[2-4]".r
  val PlayerNamePattern: Regex = "name [A-Za-z]+".r
  val LayDownTilePattern: Regex = "l [1-13][RBYG][01]".r
  val MoveTilePattern: Regex = "m [1-13][RBYG][01] t [1-13][RBYG][01]".r

  def processInputLine(input: String): Unit = {
    controller.state match {
      case ControllerState.MENU => handleMenuInput(input)
      case ControllerState.INSERTING_NAMES => handleNameInput(input)
      case ControllerState.PLAYING => handleOnTurnInput(input)
    }
  }

  def handleNameInput(name: String): Unit = {
    name match {
      case "f" =>
        if (controller.hasMoreThan1Player && controller.hasLessThan4Players) {
          controller.switchControllerState(ControllerState.MENU)
          println("\tNEWS:\tYou're finished. Great. Now type in 's' and enter to start.")
        } else {
          println("\tNEWS:\tNot enough Players. Please insert another name")
        }
      case PlayerNamePattern() =>
        if (controller.hasLessThan4Players) {
          controller.setPlayerName(name.substring(4).trim)
          println("\tNEWS:\tPlayer " + controller.getAmountOfPlayers + " is named " + name.substring(4).trim + "\n" +
            "\tNEWS:\tType in another players name and confirm with enter (Min 2 players, Max 4) or finish with 'f'")
        } else {
          println("\tNEWS:\tThe Maximum amount of players is set. Type 'f' to finish inserting names")
        }
      case _ => println("\tNEWS:\tCould not identify your input. Are you sure it was in the format 'name <name1>'?")
    }
  }

  def handleOnTurnInput(input: String): Unit = {
    input match {
      case "n" => update
      case "t" => controller.takeFromBagOfTiles();
        controller.switchToNextPlayer()
        println("\tNEWS:\tYou took a tile, you are finished. The next player has to type 'n' to continue.");
      case LayDownTilePattern(c) => controller.layDownTile(c)
      case MoveTilePattern(c) => controller.moveTile(c)
      case "f" =>
        println("\tNEWS:\tYou are finished. The next player has to type 'n' to continue."); controller.switchToNextPlayer();
    }
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
          controller.switchControllerState(ControllerState.PLAYING)
          controller.initPlayersWithStones(12)
          update
        } else {
          println("\tNEWS:\tNot enough Players. Type <c> to create a desk and insert names")
        }
      case _ => println("\tNEWS:\tCould not identify your input. Are you sure it was correct'?")
    }
  }




  override def update: Boolean = {
    controller.state match {
      case ControllerState.PLAYING =>
        printWahtUserCanDo()
        printUserBoard()
        printTable()
      case ControllerState.INSERTING_NAMES =>

      case ControllerState.MENU =>
    }
    true
  }


  def printUserBoard(): Unit = {
    println(controller.currentPlayer.name + ", thats on your Board")
    for (_ <- controller.currentPlayer.board.tiles) {
      print("____ ")
    }
    println()
    for (tile <- controller.currentPlayer.board.tiles) {
      printf("|%2s| ", tile.value)
    }
    println()
    for (tile <- controller.currentPlayer.board.tiles) {
      printf("|%s | ", tile.color.toString.charAt(0))
    }
    println()
    for (tile <- controller.currentPlayer.board.tiles) {
      printf("|%s | ", tile.ident)
    }
    println()
    for (tile <- controller.currentPlayer.board.tiles) {
      print("\u203E\u203E\u203E\u203E ")
    }
    println("\n--------------------------------------------------------------------")


  }

  def printTable(): Unit = {
    println("The Tiles on the Desk")
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
    println("\n--------------------------------------------------------------------")
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
      controller.currentPlayer.name)
  }

}
