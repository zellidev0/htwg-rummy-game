import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.{ Board, Color, Player, Tile }
import model.deskComp.deskBaseImpl.{ Desk, TileInterface }
import org.scalatest.{ Matchers, WordSpec }

import scala.collection.immutable.SortedSet

class PlayerSpec extends WordSpec with Matchers {
  val emptyBoard: Board = Board(SortedSet[TileInterface]())

  "A PlayerService" when {
    val playerService = PlayerService()
    val john          = Player("John", Board(SortedSet[TileInterface]()), hasTurn = false)
    val mike          = Player("Mike", Board(SortedSet[TileInterface]()), hasTurn = true)
    val desk: DeskInterface =
      Desk(List(mike, john), Set(Tile(2, Color.RED, 0), Tile(1, Color.RED, 0)), Set[SortedSet[TileInterface]]())
    "creating a Player called Mike and a Player called John" should {
      "return current pLayer name Mike" in {
        playerService.currentPlayerName(desk) should be("Mike")
      }
      "return the previous player John" in {
        playerService.previousPlayer(desk) should be(john)
      }
      "switching to next player" in {
        playerService.currentPlayerName(playerService.switchToNextPlayer(desk)) should be("John")
      }
    }
    "adding a new player" should {
      val table = playerService.addPlayerAndInit(desk, "Carl", 1)
      "16 tiles should be in the bagOfTiles " in {
        table.players.size should be(3)
      }
    }
  }
}
