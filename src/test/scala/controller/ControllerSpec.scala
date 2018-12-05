package controller

import model.{Tile, _}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.SortedSet

class ControllerSpec extends WordSpec with Matchers {


  "A Controller" when {
    "should move a tile" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN), Player("Name2", 1, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      desk = desk.copy(sets = desk.sets.+(SortedSet(Tile(2, Color.RED, 0))))
      desk = desk.copy(sets = desk.sets.+(SortedSet(Tile(1, Color.RED, 0))))
      val controller = new Controller(desk)
      controller.moveTile("2R0", "1R0")
      "have only one set with the 2 tiles side by side" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet(Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
      }
    }
    "lay Down a tile" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile](Tile(1, Color.RED, 0))), state = State.TURN), Player("Name2", 1, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.layDownTile("1R0")
      "have only one set with one tile" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet(Tile(1, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
      }
    }
    "get Current Player" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile](Tile(1, Color.RED, 0)))), Player("Name2", 1, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      var player = Player("", -1, Board(SortedSet()))
      controller.desk.players.find(p => p.number == 0) match {
        case Some(s) =>
          player = s.changeState(State.TURN)
          controller.desk = controller.desk.copy(players = players.-(s).+(player))
      }
      "get the one player" in {
        controller.currentP.name should be(player.name)
        controller.currentP.number should be(player.number)
        controller.currentP.board should be(player.board)
      }
    }
    "get tile from regex Player" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      val tile = controller.regexToTile("13R0")
      "get the one player" in {
        tile should be(Tile(13, Color.RED, 0))
      }
    }
    "set Player name" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN))
      var desk = Desk(players, Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      val amountOfPlayersBefore = controller.desk.players.size
      controller.addPlayerAndInit("name",2)
      "have one player more" in {
        controller.desk.players.size should be(amountOfPlayersBefore + 1)
      }
    }
    "set 5 Players " should {
      val set = Set(Tile(1, Color.RED, 0),Tile(2, Color.RED, 0),Tile(4, Color.RED, 0),Tile(1, Color.RED, 0),Tile(5, Color.RED, 0),Tile(6, Color.RED, 0),Tile(7, Color.RED, 0))
      val players = Set[Player]()
      var desk = Desk(players, set, Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.addPlayerAndInit("name",1)
      controller.addPlayerAndInit("nameA",1)
      controller.addPlayerAndInit("nameB",1)
      controller.addPlayerAndInit("nameC",1)
      controller.addPlayerAndInit("nameD",1)
      controller.addPlayerAndInit("nameE",1)
      "work for first 4 and for the last two not" in {
        controller.desk.players.size should be(4)
      }
    }
    "creates a desk" should {
      val players = Set[Player]()
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.createDesk(13)
      "work for first 4 and for the last one not" in {
        controller.desk.bagOfTiles.size should be(104)
      }
    }
    "gets next player by 2 ones" should {
      var player1 = Player("Name", 0, Board(SortedSet[Tile]()), state = State.TURN)
      val players = Set[Player](player1, Player("Name1", 1, Board(SortedSet[Tile]())), Player("Name2", 2, Board(SortedSet[Tile]())), Player("Name3", 3, Board(SortedSet[Tile]())))
      val controller = new Controller(Desk(players, Set(), Set[SortedSet[Tile]]()))
      "have the correct next player1" in {
        player1 should be(controller.currentP)
        controller.switchToNextPlayer()
      }
      "have the correct next player2" in {
        Player("Name1", 1, Board(SortedSet[Tile]()), State.TURN) should be(controller.currentP)
        controller.switchToNextPlayer()
      }
      "have the correct next player3" in {
        Player("Name2", 2, Board(SortedSet[Tile]()), State.TURN) should be(controller.currentP)
        controller.switchToNextPlayer()
      }
      "have the correct next player0" in {
        Player("Name3", 3, Board(SortedSet[Tile]()), State.TURN) should be(controller.currentP)
        controller.switchToNextPlayer()
      }
    }
  }
}


