import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.{ Board, Player }
import model.deskComp.deskBaseImpl.{ Desk, TileInterface }
import org.scalatest.{ Matchers, WordSpec }

import scala.collection.immutable.SortedSet

class GameServiceTest extends WordSpec with Matchers {

  "A GameService" when {
    val gameService = GameService()
    "creating an new desk with 1 Player" should {
      val desk: DeskInterface = Desk(List(Player("Mike", Board(SortedSet.empty[TileInterface]), hasTurn = true)),
                                     Set.empty[TileInterface],
                                     Set.empty[SortedSet[TileInterface]]);
      "return an empty table" in {
        val tableView = gameService.tableView(desk)
        tableView.size should be(0)
      }
      "return the players board" in {
        val boardView = gameService.boardView(desk)
        boardView.size should be(0)
      }
    }
  }
}
