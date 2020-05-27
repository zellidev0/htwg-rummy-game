import model.DeskInterface
import model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}
import model.deskComp.deskBaseImpl.deskImpl.{Board, Player}

import scala.collection.immutable.SortedSet

class PlayerController {

  type TileI   = TileInterface
  type DeskI   = DeskInterface
  type PlayerI = PlayerInterface

  def switchToNextPlayer(desk: DeskI): DeskI =
    desk.switchToNextPlayer

  def addPlayerAndInit(desk: DeskI, name: String, amountOfTiles: Int): DeskInterface =
    takeMaxTilesFromBagToPlayersBoard(desk = desk.addPlayer(Player(name, Board(SortedSet[TileI]()))),
      amountOfTiles,
      count = 0,
      name = name)

  @scala.annotation.tailrec
  private def takeMaxTilesFromBagToPlayersBoard(desk: DeskInterface,
                                                amountOfTiles: Int,
                                                count: Int,
                                                name: String): DeskI =
    count match {
      case count if amountOfTiles == count => desk
      case _ =>
        takeMaxTilesFromBagToPlayersBoard(
          desk = desk.takeTileFromBagToPlayer(desk.getPlayerByName(name).get, desk.getTileFromBag.get),
          amountOfTiles,
          count + 1,
          name)
    }

}
