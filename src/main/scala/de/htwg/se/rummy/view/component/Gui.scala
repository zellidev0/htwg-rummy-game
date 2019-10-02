package de.htwg.se.rummy.view.component

import java.awt.Dimension

import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.ControllerState
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface
import de.htwg.se.rummy.view.UIInterface

import scala.swing.event.ButtonClicked
import scala.swing.{Action, Button, Frame, GridPanel, Menu, MenuBar, MenuItem, ScrollPane, TextArea, TextField}

class Gui(contr: ControllerInterface) extends Frame with UIInterface {
  contr.add(this)
  val newsTestView: TextArea = new TextArea("News come in here\n") {
    editable = false
  }

  title = "Rummy"
  size = new Dimension(1000, 1000)
  menuBar = new MenuBar {
    contents += new Menu("Menu") {
      contents += new MenuItem(Action("Quit game") {
        System.exit(0)
      })
      contents += new MenuItem(Action("Save game") {
        contr.storeFile
      })
    }
  }
  val scrollContainerNews: ScrollPane = new ScrollPane() {
    contents = newsTestView
  }
  var oneIsSelected: Option[TileInterface] = None
  var buttonPanel = new GridPanel(1, 6)
  var deskPanel = new ScrollPane()
  var userBoard: ScrollPane = new ScrollPane() {
    contents = new GridPanel(2, 12)

  }
  contents = new GridPanel(4, 1) {
    hGap = 3
    vGap = 3
    contents += scrollContainerNews
    contents += deskPanel
    contents += buttonPanel
    contents += userBoard

  }
  visible = true
  handleMenuInput()


  override def processInput(input: String): Unit = {
    contr.controllerState match {
      case ControllerState.MENU => handleMenuInput()
      case ControllerState.INSERTING_NAMES => handleNameInput()
      case ControllerState.P_TURN => handleOnTurn()
      case ControllerState.P_FINISHED => handleOnTurnFinished()
    }
  }
  override def handleNameInput(name: String = ""): Unit = {
    val nameInputField = new TextField("Insert Name here")
    buttonPanel = new GridPanel(1, 5) {
      contents += new Button {
        text = "Confirm"
        reactions += {
          case ButtonClicked(_) => contr.addPlayerAndInit(nameInputField.text, elements)
        }
      }
      contents += new Button {
        text = "Finished"
        reactions += {
          case ButtonClicked(_) => contr.nameInputFinished()
        }
      }
      contents += new Button {
        text = "Undo"
        reactions += {
          case ButtonClicked(_) => contr.undo()
        }
      }
      contents += new Button {
        text = "Redo"
        reactions += {
          case ButtonClicked(_) => contr.redo()
        }
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += new GridPanel(2, 1) {
        contents += nameInputField
        contents += buttonPanel
      }
      contents += userBoard
    }
  }


  override def handleOnTurnFinished(input: String = ""): Unit = {
    buttonPanel = new GridPanel(1, 5) {
      contents += new Button() {
        text = "next Player"
        reactions += {
          case ButtonClicked(_) => contr.switchToNextPlayer()
        }
      }
    }
    userBoard = new ScrollPane() {
      contents = new GridPanel(2, 12)
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
  }

  override def handleOnTurn(input: String = ""): Unit = {
    buttonPanel = new GridPanel(1, 5) {
      contents += new Button() {
        text = "Redo"
        reactions += {
          case ButtonClicked(_) => contr.redo()
        }
      }
      contents += new Button() {
        text = "Undo"
        reactions += {
          case ButtonClicked(_) => contr.undo()
        }
      }
      contents += new Button() {
        text = "Finished"
        reactions += {
          case ButtonClicked(_) => contr.userFinishedPlay()
        }
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
  }


  override def handleMenuInput(in: String = ""): Unit = {
    buttonPanel.contents += new Button {
      text = "Create new Desk"
      reactions += {
        case ButtonClicked(_) => contr.createDesk(13)
      }
    }
    buttonPanel.contents += new Button {
      text = "Load stored game"
      reactions += {
        case ButtonClicked(_) => contr.loadFile
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
  }


  override def update: Unit = {
    contr.controllerState match {
      case ControllerState.P_DOES_NOT_OWN_TILE => newsTestView.text += "NEWS:You dont have this tile on the board. Please select another one\n"
      case ControllerState.CREATED => newsTestView.text += "NEWS:Desk created. Please type in a name and confirm with the button\n"; handleNameInput()
      case ControllerState.TABLE_NOT_CORRECT => newsTestView.text += "NEWS:Table looks not correct, please move tiles to match the rules\n"
      case ControllerState.START => newsTestView.text += "Start. Player 1 begins\n"
      case ControllerState.ENOUGH_PS => newsTestView.text += "NEWS:The Maximum amount of players is set.Please click the finish button\n"
      case ControllerState.P_FINISHED => newsTestView.text += "NEWS:You are finished. The next player has to click the button\n"; handleOnTurnFinished()
      case ControllerState.P_TURN => newsTestView.text += "NEWS:It's " + contr.currentP.name + "'s turn\n"
        handleOnTurn()
        printUserBoard()
        printTable()
      case ControllerState.INSERTED_NAME => newsTestView.text += "NEWS:Player " + contr.getAmountOfPlayers + " is added\n"
      case ControllerState.NOT_ENOUGH_PS => newsTestView.text += "NEWS:Not enough Players Please insert another name\n"
      case ControllerState.MENU => newsTestView.text += "NEWS:You're finished. Great.\n"
      case ControllerState.P_WON => printf("The winner is %s\n", contr.currentP); System.exit(0)
      case ControllerState.PLAYER_REMOVED => newsTestView.text += "NEWS:You removed the last inserted player.\n"
      case ControllerState.UNDO_LAY_DOWN_TILE => newsTestView.text += "NEWS:You took the tile up.\n"
      case ControllerState.CANT_MOVE_THIS_TILE => newsTestView.text += "NEWS:You cant move this tile.\n"
      case ControllerState.LOAD_FILE => newsTestView.text += "NEWS:You loaded a previous game. You can start now.\n"
      case ControllerState.STORE_FILE => newsTestView.text += "NEWS:You stored a game. Go on.\n"
      case ControllerState.COULD_NOT_LOAD_FILE => newsTestView.text += "NEWS:No previous game found. A new desk was created.\n"
      case ControllerState.BAG_IS_EMPTY => newsTestView.text += "NEWS:No more tiles in the bag. You must lay a tile down\n"
      case _ =>
    }
  }
  def printUserBoard(): Unit = {
    userBoard = new ScrollPane() {
      contents = new GridPanel(2, 12) {
        for (tile <- contr.viewOfBoard) {
          contents += tileAsViewForBoard(tile)
        }
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
  }
  def tileAsViewForBoard(tile: TileInterface): GridPanel = {
    new GridPanel(3, 1) {
      val value = new TextField(tile.value.toString)
      value.editable = false
      contents += value
      val color = new TextField(tile.color.toString)
      color.editable = false
      contents += color
      contents += new Button() {
        text = "Select"
        reactions += {
          case ButtonClicked(_) => contr.layDownTile(tile.identifier)
        }
      }
    }
  }
  def printTable(): Unit = {
    deskPanel = new ScrollPane() {
      contents = new GridPanel(20, 1) {
        for (sortedSet <- contr.getTileSet) {
          contents += new GridPanel(1, 13) {
            for (tile <- sortedSet)
              contents += tileAsViewForDesk(tile)
          }
        }
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
  }
  def tileAsViewForDesk(tile: TileInterface): GridPanel = {
    new GridPanel(3, 1) {
      val value = new TextField(tile.value.toString)
      value.editable = false
      contents += value
      val color = new TextField(tile.color.toString)
      color.editable = false
      contents += color
      contents += new Button() {
        text = "Select"
        reactions += {
          case ButtonClicked(_) =>
            if (oneIsSelected.isDefined) {
              if (oneIsSelected.equals(tile)) {
                oneIsSelected = None
              } else {
                contr.moveTile(oneIsSelected.get.identifier, tile.identifier)
                oneIsSelected = None
              }
            } else {
              oneIsSelected = Option(tile)
            }
        }
      }
    }
  }
}