package de.htwg.se.rummy.view.component

import de.htwg.se.rummy.controller.component.{Controller, ControllerState}
import de.htwg.se.rummy.view.UIInterface

class Tui(controller: Controller) extends UIInterface {
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
      case "z" => controller.undo
      case "r" => controller.redo
      case PlayerNamePattern() => controller.addPlayerAndInit(name.substring(4).trim, elements)
      case _ => wrongInput()
    }
  }

  private def wrongInput() {
    println("Could not identify your input. Are you sure it was correct'?")
  }

  override def handleOnTurnFinished(input: String): Unit = input match {
    case "n" => controller.switchToNextPlayer()
    case "s" => controller.storeFile
    case _ => wrongInput()
  }

  override def handleOnTurn(input: String): Unit = {
    input match {
      case LayDownTilePattern(c) => controller.layDownTile(c.split(" ").apply(1));
      case MoveTilePattern(c) => controller.moveTile(c.split(" t ").apply(0).split(" ").apply(1), c.split(" t ").apply(1));
      case "f" => controller.userFinishedPlay()
      case "z" => controller.undo
      case "r" => controller.redo
      case _ => wrongInput()
    }
  }

  override def handleMenuInput(input: String): Unit = {
    input match {
      case "c" => controller.createDesk(elements + 1)
      case "l" => controller.loadFile
      case _ => wrongInput()
    }
  }

  override def update(): Unit = {
    println(controller.currentStateMessage())
    if (controller.controllerState == ControllerState.P_TURN) {
      println(controller.currentTableMessage())
      println(controller.currentBoardMessage())
    }
  }
}
