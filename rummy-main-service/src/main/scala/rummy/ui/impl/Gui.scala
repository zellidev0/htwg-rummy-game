package rummy.ui.impl

import model.deskComp.deskBaseImpl.TileInterface
import model.deskComp.deskBaseImpl.deskImpl.Tile
import rummy.controller.ControllerInterface
import rummy.ui.{UIConnector, UIInterface}
import rummy.util.{ControllerState, Observer}

import scala.swing.event.ButtonClicked
import scala.swing.{Action, Button, Frame, GridPanel, Menu, MenuBar, MenuItem, ScrollPane, TextArea, TextField}

class Gui(connector: UIConnector.type) extends Frame with UIInterface with Observer {
  connector.add(this)

  val newsTestView: TextArea = new TextArea() {
    editable = false
  }

  title = "Rummy"
  bounds = new swing.Rectangle(800, 800)
  menuBar = new MenuBar {
    contents += new Menu("Menu") {
      contents += new MenuItem(Action("Quit game") {
        System.exit(0)
      })
      contents += new MenuItem(Action("Save game") {
        connector.controller.storeFile()
      })
    }
  }
  val scrollContainerNews: ScrollPane = new ScrollPane() {
    contents = newsTestView
  }
  var oneIsSelected: Option[TileInterface] = None
  var buttonPanel                          = new GridPanel(1, 6)
  var deskPanel                            = new ScrollPane()
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

  override def processInput(input: String): Unit =
    connector.controller.state match {
      case ControllerState.MENU            => handleMenuInput()
      case ControllerState.INSERTING_NAMES => handleNameInput()
      case ControllerState.P_TURN          => handleOnTurn()
      case ControllerState.NEXT_TYPE_N     => handleOnTurnFinished()
    }

  private def handleNameInput(name: String = ""): ControllerInterface = {
    val nameInputField = new TextField("Insert Name here")
    buttonPanel = new GridPanel(1, 5) {
      contents += new Button {
        text = "Confirm"
        reactions += {
          case ButtonClicked(_) =>
            connector.updateController(connector.controller.addPlayerAndInit(nameInputField.text, elements))
        }
      }
      contents += new Button {
        text = "Finished"
        reactions += {
          case ButtonClicked(_) => connector.updateController(connector.controller.nameInputFinished())
        }
      }
      contents += new Button {
        text = "Undo"
        reactions += {
          case ButtonClicked(_) => connector.updateController(connector.controller.undo())
        }
      }
      contents += new Button {
        text = "Redo"
        reactions += {
          case ButtonClicked(_) => connector.updateController(connector.controller.redo())
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
    connector.controller
  }

  private def handleOnTurnFinished(input: String = ""): ControllerInterface = {
    buttonPanel = new GridPanel(1, 5) {
      contents += new Button() {
        text = "next Player"
        reactions += {
          case ButtonClicked(_) => connector.updateController(connector.controller.switchToNextPlayer())
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
    connector.controller
  }

  private def handleOnTurn(input: String = ""): ControllerInterface = {
    buttonPanel = new GridPanel(1, 5) {
      contents += new Button() {
        text = "Redo"
        reactions += {
          case ButtonClicked(_) => connector.updateController(connector.controller.redo())
        }
      }
      contents += new Button() {
        text = "Undo"
        reactions += {
          case ButtonClicked(_) => connector.updateController(connector.controller.undo())
        }
      }
      contents += new Button() {
        text = "Finished"
        reactions += {
          case ButtonClicked(_) => connector.updateController(connector.controller.userFinishedPlay())
        }
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
    connector.controller
  }

  private def handleMenuInput(in: String = ""): ControllerInterface = {
    buttonPanel.contents += new Button {
      text = "Create new Desk"
      reactions += {
        case ButtonClicked(_) => connector.updateController(connector.controller.createDesk(13))
      }
    }
    buttonPanel.contents += new Button {
      text = "Load stored game"
      reactions += {
        case ButtonClicked(_) => connector.updateController(connector.controller.loadFile())
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
    connector.controller
  }

  override def updated(controller: ControllerInterface): Unit = {
    newsTestView.text += controller.answer + "\n"
    newsTestView.text += controller.state + "\n"
    controller.state match {
      case ControllerState.INSERTING_NAMES => handleNameInput()
      case ControllerState.NEXT_TYPE_N     => handleOnTurnFinished()
      case ControllerState.P_TURN =>
        handleOnTurn()
        printUserBoard()
        printTable()
      case _ =>
    }
  }

  private def printUserBoard(): Unit = {
    userBoard = new ScrollPane() {
      contents = new GridPanel(2, 12) {
        for (tile <- connector.controller.viewOfBoard) {
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

  private def tileAsViewForBoard(tile: TileInterface): GridPanel =
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
            connector.updateController(connector.controller.layDownTile(Tile.stringToTile(tile.toString).get))
        }
      }
    }

  private def printTable(): Unit = {
    deskPanel = new ScrollPane() {
      contents = new GridPanel(20, 1) {
        for (sortedSet <- connector.controller.viewOfTable) {
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

  private def tileAsViewForDesk(tile: TileInterface): GridPanel =
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
                connector.controller.moveTile(Tile.stringToTile(oneIsSelected.get.toString).get,
                                         Tile.stringToTile(tile.toString).get)
                oneIsSelected = None
              }
            } else {
              oneIsSelected = Option(tile)
            }
        }
      }
    }
}
