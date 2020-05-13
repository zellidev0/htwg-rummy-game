import model.deskComp.deskBaseImpl.TileInterface
import model.deskComp.deskBaseImpl.deskImpl.Tile

import scala.swing.event.ButtonClicked
import scala.swing.{Action, Button, Frame, GridPanel, Menu, MenuBar, MenuItem, ScrollPane, TextArea, TextField}

class Gui(contr: ControllerInterface) extends Frame with UIInterface {
  contr.add(this)
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
        contr.storeFile()
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
    contr.currentControllerState match {
      case ControllerState.MENU            => handleMenuInput()
      case ControllerState.INSERTING_NAMES => handleNameInput()
      case ControllerState.P_TURN          => handleOnTurn()
      case ControllerState.NEXT_TYPE_N     => handleOnTurnFinished()
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
        case ButtonClicked(_) => contr.loadFile()
      }
    }
    contents = new GridPanel(4, 1) {
      contents += scrollContainerNews
      contents += deskPanel
      contents += buttonPanel
      contents += userBoard
    }
  }

  override def update() {
    newsTestView.text += contr.currentAnswerState + "\n"
    newsTestView.text += contr.currentControllerState + "\n"
    contr.currentControllerState match {
      case ControllerState.INSERTING_NAMES => handleNameInput()
      case ControllerState.NEXT_TYPE_N     => handleOnTurnFinished()
      case ControllerState.P_TURN =>
        handleOnTurn()
        printUserBoard()
        printTable()
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

  def tileAsViewForBoard(tile: TileInterface): GridPanel =
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
          case ButtonClicked(_) => contr.layDownTile(Tile.stringToTile(tile.toString).get)
        }
      }
    }

  def printTable(): Unit = {
    deskPanel = new ScrollPane() {
      contents = new GridPanel(20, 1) {
        for (sortedSet <- contr.viewOfTable) {
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

  def tileAsViewForDesk(tile: TileInterface): GridPanel =
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
                contr.moveTile(Tile.stringToTile(oneIsSelected.get.toString).get, Tile.stringToTile(tile.toString).get)
                oneIsSelected = None
              }
            } else {
              oneIsSelected = Option(tile)
            }
        }
      }
    }
}
