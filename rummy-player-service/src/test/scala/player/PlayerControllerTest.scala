package player

import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, Tile}
import model.deskComp.deskBaseImpl.{Desk, TileInterface}
import org.scalatest.{Matchers, WordSpec}
import player.{PlayerController, PlayerService}

import scala.collection.immutable.SortedSet

class PlayerControllerTest extends WordSpec with Matchers {
  val emptyBoard: Board = Board(SortedSet[TileInterface]())

  "A PlayerService" when {
    val playerService = new PlayerController()
    val john          = Player("John", Board(SortedSet[TileInterface]()))
    val mike          = Player("Mike", Board(SortedSet[TileInterface]()))
    val desk: DeskInterface = Desk(
      List(mike, john),
      Set(
        Tile(1, Color.RED, 0),
        Tile(2, Color.RED, 0),
        Tile(3, Color.RED, 0),
        Tile(4, Color.RED, 0),
        Tile(5, Color.RED, 0),
        Tile(6, Color.RED, 0),
        Tile(7, Color.RED, 0),
        Tile(8, Color.RED, 0),
        Tile(9, Color.RED, 0),
        Tile(10, Color.RED, 0),
        Tile(11, Color.RED, 0),
        Tile(12, Color.RED, 0),
        Tile(1, Color.BLUE, 0),
        Tile(1, Color.YELLOW, 0),
        Tile(2, Color.BLUE, 0),
        Tile(2, Color.YELLOW, 0),
        Tile(3, Color.BLUE, 0),
        Tile(3, Color.YELLOW, 0),
        Tile(4, Color.BLUE, 0),
        Tile(4, Color.YELLOW, 0),
        Tile(5, Color.BLUE, 0),
        Tile(5, Color.YELLOW, 0),
        Tile(6, Color.BLUE, 0),
        Tile(6, Color.YELLOW, 0),
        Tile(7, Color.BLUE, 0),
        Tile(7, Color.YELLOW, 0),
        Tile(8, Color.BLUE, 0),
        Tile(8, Color.YELLOW, 0),
        Tile(9, Color.BLUE, 0),
        Tile(9, Color.YELLOW, 0),
        Tile(10, Color.BLUE, 0),
        Tile(10, Color.YELLOW, 0),
        Tile(11, Color.BLUE, 0),
        Tile(11, Color.YELLOW, 0),
        Tile(12, Color.BLUE, 0),
        Tile(12, Color.YELLOW, 0),
      ),
      Set[SortedSet[TileInterface]]()
    )
    "creating a Player called Mike and a Player called John" should {
      "return current pLayer name Mike" in {
        playerService.switchToNextPlayer(desk).players.head should be(john)
      }
    }
    "adding a new player" should {
      val desk1: DeskInterface = playerService.addPlayerAndInit(desk, "Carl", 12)
      "12 tiles should be at the player" in {
        desk1.getPlayerByName("Carl").get.board.tiles.size should be(12)
      }
    }
  }
}
