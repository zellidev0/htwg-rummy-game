import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.{ Color, Tile }
import model.deskComp.deskBaseImpl.{ Desk, PlayerInterface, TileInterface }

import scala.collection.immutable.SortedSet
import scala.util.Random

case class GameController() {

  type TileI   = TileInterface
  type DeskI   = DeskInterface
  type PlayerI = PlayerInterface

  def moveTile(desk: DeskI, tile: TileI, toTile: TileI): DeskI =
    desk.tryToMoveTwoTilesOnDesk(tile, toTile).get

  def putTileDown(desk: DeskI, tile: TileI): DeskI =
    if (desk.getCurrentPlayer.has(tile)) {
      desk.putDownTile(desk.getCurrentPlayer, tile)
    } else desk

  def createDefaultTable(amount: Int): DeskI = {
    var bagOfTiles: Set[TileI] = Set[TileI]()
    (1 to amount).foreach(
      value =>
        Set(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE)
          .foreach(color =>
            (0 to 1)
              .foreach(ident => bagOfTiles += Tile(value, color, ident))))
    Desk(List[PlayerInterface](), Random.shuffle(bagOfTiles), Set[SortedSet[TileI]]())
  }

}
