package controller

import model._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.SortedSet

class ControllerSpec extends WordSpec with Matchers {


  "A Controller" when {
    "should move a tile" should {
      val players = Set(Player("Name1", 0, Board(Set[Tile]()), state = State.TURN), Player("Name2", 1, Board(Set[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      desk = desk.copy(tileSets = desk.tileSets.+(SortedSet(Tile(2, Color.RED, 0))))
      desk = desk.copy(tileSets = desk.tileSets.+(SortedSet(Tile(1, Color.RED, 0))))
      val controller = new Controller(desk)
      controller.moveTile("m 2R0 t 1R0")
      "have only one set with the 2 tiles side by side" in {
        controller.desk.tileSets.size should be(1)
        controller.desk.tileSets.contains(SortedSet(Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
      }
    }
    "lay Down a tile" should {
      val players = Set(Player("Name1", 0, Board(Set[Tile](Tile(1, Color.RED, 0))), state = State.TURN), Player("Name2", 1, Board(Set[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.layDownTile("l 1R0")
      "have only one set with one tile" in {
        controller.desk.tileSets.size should be(1)
        controller.desk.tileSets.contains(SortedSet(Tile(1, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
      }
    }
    "get Current Player" should {
      val players = Set(Player("Name1", 0, Board(Set[Tile](Tile(1, Color.RED, 0)))), Player("Name2", 1, Board(Set[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      var player = Player("", -1, Board(Set()))
      controller.desk.players.find(p => p.number == 0) match {
        case Some(s) =>
          player = s.changeState(State.TURN);
          controller.desk = controller.desk.copy(players = players.-(s).+(player))
      }
      "get the one player" in {
        controller.getCurrentPlayer.name should be(player.name)
        controller.getCurrentPlayer.number should be(player.number)
        controller.getCurrentPlayer.board should be(player.board)
      }
    }
    "get tile from regex Player" should {
      val players = Set(Player("Name1", 0, Board(Set[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      val tile = controller.getTileFromRegex("1R0")
      "get the one player" in {
        tile should be(Tile(1, Color.RED, 0))
      }
    }
    "set Player name" should {
      val players = Set(Player("Name1", 0, Board(Set[Tile]()), state = State.TURN))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)

      val amountOfPlayersBefore = controller.desk.players.size
      controller.setPlayerName("name")
      "have one player more" in {
        controller.desk.players.size should be(amountOfPlayersBefore + 1)
      }
    }
    "set 5 Players " should {
      val players = Set[Player]()
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      val pl1 = controller.setPlayerName("name")
      val pl2 = controller.setPlayerName("nameA")
      val pl3 = controller.setPlayerName("nameB")
      val pl4 = controller.setPlayerName("nameC")
      val pl5 = controller.setPlayerName("nameD")
      val pl6 = controller.setPlayerName("nameE")
      "work for first 4 and for the last one not" in {
        pl1 should be(true)
        pl2 should be(true)
        pl3 should be(true)
        pl4 should be(true)
        pl5 should be(false)
        pl6 should be(false)
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
      val players = Set(Player("Name", 0, Board(Set[Tile]()), state = State.TURN), Player("Name1", 1, Board(Set[Tile]())),
        Player("Name2", 2, Board(Set[Tile]())), Player("Name3", 4, Board(Set[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      val player1 = controller.switchToNextPlayer()
      "have the correct next player1" in {
        player1.toString should be(Player("Name1", 1, Board(Set[Tile]())).toString)
      }
      val player2 = controller.switchToNextPlayer()
      "have the correct next player2" in {
        player2.toString should be(Player("Name2", 2, Board(Set[Tile]())).toString)
      }
      val player3 = controller.switchToNextPlayer()
      "have the correct next player3" in {
        player3.toString should be(Player("Name3", 3, Board(Set[Tile]())).toString)
      }
      val player4 = controller.switchToNextPlayer()
      "have the correct next player0" in {
        player4.toString should be(Player("Name2", 0, Board(Set[Tile]())).toString)
      }
    }

    "inits all players" should {
      val players = Set(Player("Name0", 0, Board(Set[Tile]()), state = State.TURN), Player("Name1", 1, Board(Set[Tile]())))
      var desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.createDesk(13)
      controller.initPlayersWithStones(3)
      "both players should have 3 tiles" in {
        for (pla <- controller.desk.players) {
          pla.board.amountOfTiles() should be(3)
        }
      }
    }

  }
}


