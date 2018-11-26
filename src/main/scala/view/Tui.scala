package view

import controller.Controller
import util.Observer

import scala.util.matching.Regex

class Tui(controller: Controller) extends Observer {
  val AmountOfPlayersPattern: Regex = "([2-4])".r
  val PlayerNamePattern: Regex = "name ([A-Za-z])+".r

  controller.add(this)
  val LayDownTilePattern: Regex = "(l [1-13][RBYG][01])".r
  val MoveTilePattern: Regex = "(m [1-13][RBYG][01] t [1-13][RBYG][01])".r
  var playerOnTurn = false
  var mustInsertNames = false

  def processInputLine(input: String): Unit = {
    if (playerOnTurn) {
      handleOnTurnInout(input)
    } else if (mustInsertNames) {
      handleNameInput(input.substring(4).trim)
    } else {
      handleMenuInput(input)
    }
  }

  def handleNameInput(name: String): Unit = {
    name match {
      case "f" =>
        if (!controller.hasEnoughPlayers) {
          println("Not enough Players. Please insert another name")
          return
        }
        mustInsertNames = false
        println("You finished great. Now type in 's' and enter to start.")
      case PlayerNamePattern() =>
        if (controller.hasEnoughPlayers) {
          mustInsertNames = false
          println("The Maximum amount of players is set. Type 's' to start")
          return
        }
        if (controller.setPlayerName(name)) {
          println("Player " + controller.desk.players.size + " is named" + name.substring(4))
          println("Type in another players name and confirm with enter (Min 2 players, Max 4) or finish with 'f'")
        } else {
          println("Could not identify your input. Are you sure it was in the format 'name <name1>'?")
        }
      case _ => println("Could not identify your input. Are you sure it was in the format 'name <name1>'?")
    }
  }

  def handleOnTurnInout(input: String): Unit = {
    println("Type 't' to take a tile")
    println("Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table")
    println("Type 'm <valueA> <FirstLetterOfColorA> <numA> l <valueB> <FirstLetterOfColorB> <numB>"
      + "to put A in where B is")
    println("Type 'q' when you are finished with putting")
    input match {
      case "t" => controller.takeFromBagOfTiles(); playerOnTurn = false; controller.switchToNextPlayer();
      case LayDownTilePattern(c) => controller.layDownTile(c)
      case MoveTilePattern(c) => controller.moveTile(c)
    }
  }

  def handleMenuInput(input: String) = {
    input match {
      case "q" =>
      case "c" =>
        controller.createDesk(13)
        mustInsertNames = true
        println("Desk created. Please type in 'name <name1>' where name1 is the first players name. Confirm with enter")
      case "s" => playerOnTurn = true
        controller.initPlayersWithStones(12)
        println("Player 1 starts the game.")
        println("Type 't' to take a tile")
        println("Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table")
        println("Type 'm <valueA> <FirstLetterOfColorA> <numA> l <valueB> <FirstLetterOfColorB> <numB>"
          + "to put A in where B is")
        println("Type 'q' when you are finished with putting")
      case _ => println("Could not identify your input. Are you sure it was correct'?")
    }
  }


  override def update: Boolean = {
    println("test") //TODO
    true
  }

}
