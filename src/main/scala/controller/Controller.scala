package controller

import model._
import util.Observable

class Controller(var desk: Desk) extends Observable {

  def setPlayerName(newName: String): Unit = desk = desk.copy(players = desk.players.+(Player(newName, desk.players.size, Board(Set[Tile]()), State.WAIT)))


  def createDesk(amountOfDifferentTiles: Int): Unit = {
    var bagOfTiles: Set[Tile] = Set[Tile]()
    var tileTable: Set[Tile] = Set[Tile]()

    for (number <- 1 to amountOfDifferentTiles) {
      for (color <- Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE)) {
        for (ident <- 0 to 1) {
          bagOfTiles += Tile(number, color, ident)
        }
      }
    }
    desk = Desk(Set[Player](), bagOfTiles, tileTable)
  }


}
