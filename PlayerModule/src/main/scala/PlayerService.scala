import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.{Board, Player}
import model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}

import scala.collection.immutable.SortedSet

case class PlayerService() {

  type TileI = TileInterface
  type DeskI = DeskInterface
  type PlayerI = PlayerInterface

  def previousPlayer(desk: DeskI): PlayerI =
    desk.getPreviousPlayer

  def currentPlayerName(desk: DeskI): String =
    desk.getCurrentPlayer.name

  def switchToNextPlayer(desk: DeskI): DeskI =
    desk.switchToNextPlayer

  def addPlayerAndInit(desk: DeskI, name: String, amountOfTiles: Int): DeskInterface =
    takeMaxTilesFromBagToPlayersBoard(
      desk = desk.addPlayer(Player(name, Board(SortedSet[TileI]()), desk.amountOfPlayers == 0)),
      amountOfTiles,
      count = 0,
      name = name)

  @scala.annotation.tailrec
  private def takeMaxTilesFromBagToPlayersBoard(desk: DeskInterface, amountOfTiles: Int, count: Int, name: String): DeskI =
    count match {
      case count if amountOfTiles == count => desk
      case _ =>
        takeMaxTilesFromBagToPlayersBoard(
          desk = desk.takeTileFromBagToPlayer(desk.getPlayerByName(name).get, desk.getTileFromBag),
          amountOfTiles,
          count + 1,
          name)
    }

}
