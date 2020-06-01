package game

import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.{ Board, Color, Player, Tile }
import model.deskComp.deskBaseImpl.{ Desk, PlayerInterface, TileInterface }
import org.scalatest.{ Matchers, WordSpec }

import scala.collection.immutable.SortedSet

class GameControllerTest extends WordSpec with Matchers {
  val emptyBoard: Board = Board(SortedSet[TileInterface]())

  "A GameService" when {
    val gameService = game.GameController()
    "creating an new desk with 1 Player" should {
      val desk: DeskInterface = Desk(
        List(Player("Mike", Board(SortedSet[TileInterface](Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))))),
        Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)),
        Set[SortedSet[TileInterface]]()
      )
      val desk1 = gameService.putTileDown(desk, Tile(1, Color.RED, 0))
      val desk2 = gameService.putTileDown(desk1, Tile(2, Color.RED, 0))
      val desk3 = gameService.moveTile(desk2, Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))
      "return an empty table" in {
        desk1.tableView.size should be(1)
        desk2.tableView.size should be(2)
        desk3.tableView.size should be(1)
      }
      "return the players board" in {
        desk.boardView.size should be(2)
        desk1.boardView.size  should be(1)
        desk2.boardView.size  should be(0)
      }
      "" in {
        desk3.tableView should be(
          Set(SortedSet[TileInterface](Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))))
      }
    }
    "creating default bagOfTiles" should {
      val table = gameService.createDefaultTable(2)
      "16 tiles should be in the bagOfTiles " in {
        table.bagOfTiles.size should be(16)
      }
    }
  }
}
