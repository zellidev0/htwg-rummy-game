package de.htwg.se.rummy.controller.component

import java.nio.file.{Files, Paths}

import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.command._
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.Desk
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Color, Player, Tile}
import de.htwg.se.rummy.model.fileIO.json.FileIO
import de.htwg.se.rummy.util.{Observable, UndoManager}

import scala.collection.immutable.SortedSet

class Controller(var desk: Desk) extends Observable {

  var userPutTileDown = 0
  var controllerState: ControllerState.Value = MENU

  private val undoManager = new UndoManager
  private val fileIO = new FileIO

  def userFinishedPlay(): Unit = {
    if (userPutTileDown == 0) {
      if (desk.bagOfTiles.isEmpty) {
        swState(BAG_IS_EMPTY)
        swState(ControllerState.MENU)
      } else {
        undoManager.doStep(new TakeTileCommand(this))
      }
    } else if (desk.checkTable()) {
      if (desk.currentPlayerWon()) {
        swState(P_WON)
      } else {
        undoManager.doStep(new FinishedCommand(userPutTileDown, this))
      }
    } else {
      swState(TABLE_NOT_CORRECT)
      swState(ControllerState.P_TURN)
    }
  }

  def swState(c: ControllerState.Value): Unit = {
    controllerState = c
    notifyObservers()
  }

  def moveTile(tile1: String, tile2: String): Unit = {
    if (!desk.contains(Tile.stringToTile(tile1)) || !desk.contains(Tile.stringToTile(tile2))) {
      swState(CANT_MOVE_THIS_TILE)
      swState(ControllerState.P_TURN)
    } else {
      undoManager.doStep(new MoveTileCommand(tile1, tile2, this))
    }
  }

  def layDownTile(tile: String): Unit = {
    if (!currentP.hasTile(Tile.stringToTile(tile))) {
      swState(P_DOES_NOT_OWN_TILE)
      swState(ControllerState.P_TURN)
    } else {
      undoManager.doStep(new LayDownCommand(tile, this))
    }
  }

  def currentP: Player = desk.getCurrentPlayer

  def previousP: Player = desk.getPreviousPlayer

  def nextP: Player = desk.getNextPlayer

  def addPlayerAndInit(newName: String, max: Int): Unit = {
    if (!hasLessThan4Players) {
      swState(ENOUGH_PS)
    } else {
      undoManager.doStep(new NameCommand(newName, max, this))
    }
  }

  def hasLessThan4Players: Boolean = desk.lessThan4P

  def createDesk(amount: Int): Unit = {
    var bagOfTiles: Set[Tile] = Set[Tile]()
    for (number <- 1 to amount;
         color <- Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
         ident <- 0 to 1) {
      bagOfTiles += Tile(number, color, ident)
    }
    desk = deskBaseImpl.Desk(Set[Player](), bagOfTiles, Set[SortedSet[Tile]]())
    swState(CREATED)
    swState(ControllerState.INSERTING_NAMES)
  }

  def switchToNextPlayer(): Unit = undoManager.doStep(new SwitchPlayerCommand(this))

  def nameInputFinished(): Unit = {
    if (desk.correctAmountOfPlayers) {
      undoManager.emptyStack()
      swState(START)
      swState(P_TURN)
    } else {
      swState(NOT_ENOUGH_PS)
      swState(ControllerState.INSERTING_NAMES)
    }
  }

  def getTileSet: Set[SortedSet[Tile]] = desk.viewOfTable

  def getAmountOfPlayers: Int = desk.amountOfPlayers

  def setsOnDeskAreCorrect: Boolean = desk.checkTable()

  def removeTileFromSet(tile: Tile): Unit = desk = desk.removeFromTable(tile)

  def undo: Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo: Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  def viewOfBoard: SortedSet[Tile] = desk.viewOfCurrentPlayersBoard

  def storeFile: Unit = {
    fileIO.save(desk)
    val oldState = controllerState
    swState(STORE_FILE)
    swState(oldState)
  }

  def loadFile: Unit = {
    if (Files.exists(Paths.get("/target/desk.json"))) {
      desk = fileIO.load
      swState(LOAD_FILE)
      swState(START)
      swState(P_TURN)
    } else {
      swState(COULD_NOT_LOAD_FILE)
      createDesk(12)
    }


  }

  def currentTableMessage(): String = {
    var s = ""
    s = s + String.format(
      "|---------------------------------------------------------------------------------------|\n" +
        "| That's on the desk.                                                                    |\n" +
        "|---------------------------------------------------------------------------------------|\n\n",
      currentP.name)
    for (sortedSet <- getTileSet) {
      for (_ <- sortedSet) {
        s = s + "____ "
      }
      s = s + "\n"
      for (tile <- sortedSet) {
        s = s + String.format("|%2s| ", tile.value.toString)
      }
      s = s + "\n"
      for (tile <- sortedSet) {
        s = s + String.format("|%s | ", tile.color.toString.charAt(0).toString)
      }
      s = s + "\n"
      for (tile <- sortedSet) {
        s = s + String.format("|%s | ", tile.ident.toString)
      }
      s = s + "\n"
      for (_ <- sortedSet) {
        s = s + String.format("\u203E\u203E\u203E\u203E ")
      }
      s = s + "\n"
    }
    s
  }

  def currentBoardMessage(): String = {
    var s = ""
    s = s + String.format(
      "|---------------------------------------------------------------------------------------|\n" +
        "| %20s thats on your board.                                             |\n" +
        "|---------------------------------------------------------------------------------------|\n",
      currentP.name)
    for (_ <- viewOfBoard) {
      s = s + "____ "
    }
    s = s + "\n"
    for (tile <- viewOfBoard) {
      s = s + String.format("|%2s| ", tile.value.toString)
    }
    s = s + "\n"
    for (tile <- viewOfBoard) {
      s = s + String.format("|%s | ", tile.color.toString.charAt(0).toString)
    }
    s = s + "\n"
    for (tile <- viewOfBoard) {
      s = s + String.format("|%s | ", tile.ident.toString)
    }
    s = s + "\n"
    for (_ <- viewOfBoard) {
      s = s + String.format("\u203E\u203E\u203E\u203E ")
    }
    s = s + "\n"
    s
  }

  def currentStateMessage(): String = {
    controllerState match {
      case ControllerState.P_DOES_NOT_OWN_TILE => "You dont have this tile on the board. Please select another one\n"
      case ControllerState.CREATED => "Desk created. Please type in 'name <name1>' where name1 is the first players name.\n"
      case ControllerState.TABLE_NOT_CORRECT => "Table looks not correct, please move tiles to match the rules\n"
      case ControllerState.START => "Start\n"
      case ControllerState.ENOUGH_PS => "The Maximum amount of players is set. Type 'f' to finish inserting names/n"
      case ControllerState.P_FINISHED => "You are finished. The next player has to type 'n' to continue,\nor type s to store the current game.\n"
      case ControllerState.P_TURN =>
        String.format(
          "|---------------------------------------------------------------------------------------|\n" +
            "| %50s it's your turn. Do your stuff.     |\n" +
            "|---------------------------------------------------------------------------------------|\n" +
            "| Type 'l <value> <FirstLetterOfColor> <num>' to put it on the table                    |\n" +
            "| Type 'm <valueA> <FirstLetterOfColorA> <numA> t <valueB> <FirstLetterOfColorB> <numB> |\n" +
            "|  to put A in where B is                                                               |\n" +
            "| Type 'f' to finish (and take a tile automatically if you did nothing)                 |\n" +
            "| Type 'z' to undo                                                                      |\n" +
            "| Type 'r' to redo                                                                      |\n" +
            "|---------------------------------------------------------------------------------------|\n\n",
          currentP.name)
      case ControllerState.INSERTED_NAME => "Player " + getAmountOfPlayers + " is added\n"
      case ControllerState.NOT_ENOUGH_PS => "Not enough Players. Type <c> to create a desk and insert names\n"
      case ControllerState.MENU => "You're finished. Great. Now type in 's' and enter to start.\n"
      case ControllerState.P_WON =>
        System.exit(0)
        String.format("%s is the winner.\n", currentP.name)
      case ControllerState.PLAYER_REMOVED => "You removed the player you inserted .\n"
      case ControllerState.UNDO_LAY_DOWN_TILE => "You took the tile up.\n"
      case ControllerState.CANT_MOVE_THIS_TILE => "You can not move this tile.\n"
      case ControllerState.LOAD_FILE => "You loaded a previous game. You can start now.\n"
      case ControllerState.STORE_FILE => "You stored a game. Go on.\n"
      case ControllerState.COULD_NOT_LOAD_FILE => "No previous game found. A new desk was created.\n"
      case ControllerState.BAG_IS_EMPTY => "No more tiles in the bag. You must lay a tile down\n"
      case ControllerState.INSERTING_NAMES => "Type in 'name <the name of the player>' and confirm. (Min 2 players, Max 4) or finish with 'f'\n"
      case _ => ""
    }
  }


}

