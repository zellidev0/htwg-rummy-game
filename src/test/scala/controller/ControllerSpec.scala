package controller

import model.{Tile, _}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.SortedSet

class ControllerSpec extends WordSpec with Matchers {


  "A Controller" when {
    "user finishes play" should {
      val player1 = Player("Name0", 0, Board(SortedSet[Tile](Tile(1, Color.RED, 0))), state = State.TURN)
      val player2 = Player("Name1", 1, Board(SortedSet[Tile](Tile(2, Color.RED, 0))))
      val player3 = Player("Name2", 2, Board(SortedSet[Tile]()))
      val player4 = Player("Name3", 3, Board(SortedSet[Tile]()))
      val players = Set[Player](player1, player2, player3, player4)
      val desk = Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.userFinishedPlay()
      "userPutTileDown be 0 " in {
        controller.desk.players.size should be(4)
        controller.desk.players.find(_.number == 0).get.board.amountOfTiles() should be(2)
      }
      controller.switchToNextPlayer()
      controller.userPutTileDown += 1
      controller.userFinishedPlay()
      "userPutTileDown be 1 and table is ok " in {
        player1.board.amountOfTiles() should be(1)
      }
      controller.layDownTile("2R0")
      controller.userFinishedPlay()
      "table be not correct " in {
        player1.board.amountOfTiles() should be(1)
        player2.board.amountOfTiles() should be(1)
        controller.desk.sets.size should be(1)
      }
    }
    "should move two correct and movable tiles" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN), Player("Name2", 1, Board(SortedSet[Tile]())))
      val desk = Desk(players, Set(), Set[SortedSet[Tile]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0))))
      val controller = new Controller(desk)
      controller.moveTile("2R0", "1R0")
      "have only one set with the 2 tiles side by side" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet(Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
      }
      controller.moveTile("2R1", "1R0")
      "have change nothing" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet(Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
      }
    }
    "lay Down a tile the user really has" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile](Tile(1, Color.RED, 0))), state = State.TURN), Player("Name2", 1, Board(SortedSet[Tile]())))
      val desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.layDownTile("1R0")
      "should work (have only one set with one tile)" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet(Tile(1, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
      }
      controller.layDownTile("1R1")
      "should not work (have only one set with one tile)" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet[Tile](Tile(1, Color.RED, 0))) should be(true)
        controller.desk.sets.contains(SortedSet[Tile](Tile(1, Color.RED, 1))) should be(false)
      }
    }
    "get tile from regex" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]())))
      val desk = Desk(players, Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      val tile0 = controller.regexToTile("1R0")
      val tile1 = controller.regexToTile("10R1")
      val tile2 = controller.regexToTile("2B0")
      val tile3 = controller.regexToTile("11B1")
      val tile4 = controller.regexToTile("3Y0")
      val tile5 = controller.regexToTile("12Y1")
      val tile6 = controller.regexToTile("4G0")
      val tile7 = controller.regexToTile("13G1")
      "get the tile" in {
        tile0 should be(Tile(1, Color.RED, 0))
        tile1 should be(Tile(10, Color.RED, 1))
        tile2 should be(Tile(2, Color.BLUE, 0))
        tile3 should be(Tile(11, Color.BLUE, 1))
        tile4 should be(Tile(3, Color.YELLOW, 0))
        tile5 should be(Tile(12, Color.YELLOW, 1))
        tile6 should be(Tile(4, Color.GREEN, 0))
        tile7 should be(Tile(13, Color.GREEN, 1))
      }
    }
    "adding a player and have less than 4" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN))
      val desk = Desk(players, Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.createDesk(12) // must create or you cant init all players
      val amountOfPlayersBefore = controller.desk.players.size
      controller.addPlayerAndInit("Name2", 12)
      "have 2 players" in {
        controller.hasLessThan4Players should be(true)
        controller.desk.players.size should be(amountOfPlayersBefore + 1)
        controller.addPlayerAndInit("Name3", 12)
      }
      "have 3 players" in {
        controller.hasLessThan4Players should be(true)
        controller.desk.players.size should be(amountOfPlayersBefore + 2)
        controller.addPlayerAndInit("Name4", 12)
      }
      "have 4 players" in {
        controller.hasLessThan4Players should be(true)
        controller.desk.players.size should be(amountOfPlayersBefore + 3)
        controller.addPlayerAndInit("Name5", 12)
      }
      "have4, which means no more player" in {
        controller.hasLessThan4Players should be(false)
        controller.desk.players.size should be(amountOfPlayersBefore + 4)
      }
    }
    "creates a desk" should {
      val desk = Desk(Set[Player](), Set(), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.createDesk(13)
      "have 104 tiles" in {
        controller.desk.bagOfTiles.size should be(13 * 4 * 2)
      }
    }
    "switching players" should {
      val player0 = Player("Name0", 0, Board(SortedSet[Tile]()), state = State.TURN)
      val player1 = Player("Name1", 1, Board(SortedSet[Tile]()))
      val player2 = Player("Name2", 2, Board(SortedSet[Tile]()))
      val player3 = Player("Name3", 3, Board(SortedSet[Tile]()))
      val players = Set[Player](player0, player1, player2, player3)
      val controller = new Controller(Desk(players, Set(), Set[SortedSet[Tile]]()))
      "have the correct previous, current and next player" in {
        controller.previousP.number should be(player3.number)
        controller.currentP.number should be(player0.number)
        controller.nextP.number should be(player1.number)
        controller.switchToNextPlayer()
      }
      "have the correct previous, current and next player1" in {
        controller.previousP.number should be(player0.number)
        controller.currentP.number should be(player1.number)
        controller.nextP.number should be(player2.number)
        controller.switchToNextPlayer()
      }
      "have the correct previous, current and next player2" in {
        controller.previousP.number should be(player1.number)
        controller.currentP.number should be(player2.number)
        controller.nextP.number should be(player3.number)
        controller.switchToNextPlayer()
      }
      "have the correct previous, current and next player3" in {
        controller.previousP.number should be(player2.number)
        controller.currentP.number should be(player3.number)
        controller.nextP.number should be(player0.number)
        controller.switchToNextPlayer()
      }
      "have the correct previous, current and next player4" in {
        controller.previousP.number should be(player3.number)
        controller.currentP.number should be(player0.number)
        controller.nextP.number should be(player1.number)
      }
    }
    "name input finished" should {
      val players = Set[Player]()
      val desk = Desk(players, Set(), Set[SortedSet[Tile]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0))))
      val controller = new Controller(desk)
      controller.createDesk(12) // must create desk or you cant init players
      val oldState = controller.cState
      controller.addPlayerAndInit("Name1", 12)
      controller.nameInputFinished()
      "when having not correct amount of players" in {
        oldState should be(ContState.INSERTING_NAMES)
        controller.cState should be(ContState.INSERTING_NAMES)
        controller.addPlayerAndInit("Name2", 12)
        controller.nameInputFinished()
      }
      "when having correct amount of players" in {
        oldState should be(ContState.INSERTING_NAMES)
        controller.cState should be(ContState.P_TURN)
      }
    }
    "getting tile sets" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN))
      val sets = Set[SortedSet[Tile]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0)))
      val desk = Desk(players, Set(), sets)
      val controller = new Controller(desk)
      val oldState = controller.cState
      "be sets" in {
        controller.getTileSet should be(sets)
      }
    }
    "get amount of players" should {
      val players = Set[Player]()
      val sets = Set[SortedSet[Tile]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0)))
      val desk = Desk(players, Set(), sets)
      val controller = new Controller(desk)
      controller.createDesk(12)
      controller.addPlayerAndInit("Name0", 12)
      val oldState = controller.cState
      "be 1" in {
        controller.getAmountOfPlayers should be(1)
        controller.addPlayerAndInit("Name1", 12)
      }
      "be 2" in {
        controller.getAmountOfPlayers should be(2)
        controller.addPlayerAndInit("Name2", 12)
      }
      "be 3" in {
        controller.getAmountOfPlayers should be(3)
        controller.addPlayerAndInit("Name3", 12)
      }
      "be 4" in {
        controller.getAmountOfPlayers should be(4)
        controller.addPlayerAndInit("Name4", 12)
      }
      "be also 4" in {
        controller.cState should be(ContState.ENOUGH_PS)
        controller.addPlayerAndInit("Name5", 12)
      }
    }
    "sets on desk are correct" should {
      val setOfCorrectStreets =
        Set(SortedSet[Tile](Tile(1, Color.GREEN, 0), Tile(2, Color.GREEN, 1), Tile(3, Color.GREEN, 0), Tile(4, Color.GREEN, 0), Tile(5, Color.GREEN, 0)), //Street 4 GREEN
          SortedSet[Tile](Tile(4, Color.RED, 0), Tile(5, Color.RED, 0), Tile(6, Color.RED, 0))) // Street 3 RED
      val setOfCorrectPairs =
        Set(SortedSet[Tile](Tile(2, Color.GREEN, 0), Tile(2, Color.YELLOW, 0), Tile(2, Color.BLUE, 0)), // Pair 3 different
          SortedSet[Tile](Tile(8, Color.RED, 0), Tile(8, Color.BLUE, 0), Tile(8, Color.YELLOW, 0), Tile(8, Color.GREEN, 0))) // Pair 4 different
      val setOfWrongPairs =
        Set(SortedSet[Tile](Tile(10, Color.GREEN, 0), Tile(10, Color.GREEN, 1), Tile(10, Color.BLUE, 0)), // Pair of 3 where 2 same
          SortedSet[Tile](Tile(13, Color.YELLOW, 0), Tile(13, Color.GREEN, 0))) // Pair of 2
      val setOfWrongStreets =
        Set(SortedSet[Tile](Tile(9, Color.BLUE, 1), Tile(11, Color.BLUE, 1), Tile(12, Color.BLUE, 1), Tile(13, Color.BLUE, 0)), // Street with missing one
          SortedSet[Tile](Tile(7, Color.RED, 0), Tile(8, Color.RED, 1))) // street with only 2
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN), Player("Name2", 1, Board(SortedSet[Tile]())))
      val desk = Desk(players, Set[Tile](), setOfCorrectStreets)
      var controller = new Controller(Desk(Set[Player](), Set(), setOfCorrectStreets))
      "be true when setOfCorrectStreets" in {
        controller.setsOnDeskAreCorrect should be(true)
      }
      "be true when setOfCorrectPairs" in {
        controller = new Controller(Desk(Set[Player](), Set(), setOfCorrectPairs))
        controller.setsOnDeskAreCorrect should be(true)
      }
      "be true when setOfWrongStreets" in {
        controller = new Controller(Desk(Set[Player](), Set(), setOfWrongStreets))
        controller.setsOnDeskAreCorrect should be(false)
      }
      "be true when setOfWrongPairs" in {
        controller = new Controller(Desk(Set[Player](), Set(), setOfWrongPairs))
        controller.setsOnDeskAreCorrect should be(false)
      }
    }
    "removing tile from table" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN), Player("Name2", 1, Board(SortedSet[Tile]())))
      val desk = Desk(players, Set[Tile](), Set[SortedSet[Tile]](SortedSet(Tile(1, Color.RED, 0))))
      val controller = new Controller(desk)
      controller.removeTileFromSet(Tile(1, Color.RED, 0))
      "should have one less " in {
        controller.desk.sets.contains(SortedSet(Tile(1, Color.RED, 0))) should be(false)
      }
    }
    "calling undo redo when laying down" should {
      val tile1 = Tile(1, Color.RED, 0)
      val player1 = Player("Name1", 0, Board(SortedSet[Tile](tile1)), state = State.TURN)
      val players = Set(player1)
      val desk = Desk(players, Set[Tile](), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.layDownTile("1R0")
      "undo" in {
        controller.desk.currentP.board.contains(tile1) should be(false)
        controller.desk.sets.contains(SortedSet(tile1)) should be(true)
        controller.undo
        controller.desk.currentP.board.contains(tile1) should be(true)
        controller.desk.sets.contains(SortedSet(tile1)) should be(false)
        controller.redo
      }
      "redo" in {
        controller.desk.currentP.board.contains(tile1) should be(false)
        controller.desk.sets.contains(SortedSet(tile1)) should be(true)
      }
    }
    "calling undo redo when moving tile" should {
      val tile1 = Tile(1, Color.RED, 0)
      val tile2 = Tile(2, Color.RED, 0)
      val player1 = Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN)
      val players = Set(player1)
      val desk = Desk(players, Set[Tile](), Set[SortedSet[Tile]](SortedSet[Tile](tile1), SortedSet[Tile](tile2)))
      val controller = new Controller(desk)
      controller.moveTile("1R0", "2R0")
      "undo" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet(tile1, tile2)) should be(true)
        controller.undo
        controller.desk.sets.size should be(2)
        controller.desk.sets.contains(SortedSet(tile1, tile2)) should be(false)
        controller.redo
      }
      "redo" in {
        controller.desk.sets.size should be(1)
        controller.desk.sets.contains(SortedSet(tile1, tile2)) should be(true)
      }
    }
    "calling undo redo when name inserting" should {
      val desk = Desk(Set[Player](), Set[Tile](), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.createDesk(12)
      controller.addPlayerAndInit("Name0", 12)
      "undo" in {
        controller.desk.amountOfPlayers should be(1)
        controller.undo
        controller.desk.amountOfPlayers should be(0)
        controller.redo
      }
      "redo" in {
        controller.desk.amountOfPlayers should be(1)
      }
    }
    "calling undo redo when user finishes play" should {
      val player1 = Player("Name0", 0, Board(SortedSet[Tile](Tile(1, Color.RED, 0))), state = State.TURN)
      val player2 = Player("Name1", 1, Board(SortedSet[Tile](Tile(2, Color.RED, 0))))
      val player3 = Player("Name2", 2, Board(SortedSet[Tile]()))
      val player4 = Player("Name3", 3, Board(SortedSet[Tile]()))
      val players = Set[Player](player1, player2, player3, player4)
      val desk = Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.userPutTileDown = 1
      controller.userFinishedPlay()
      "userPutTileDown be 1" in {
        controller.userPutTileDown should be(0)
        controller.undo
      }
      "undo" in {
        controller.userPutTileDown should be(1)
        controller.redo
      }
      "redo" in {
        controller.userPutTileDown should be(0)
      }
    }
    "calling undo redo when switching player" should {
      val player1 = Player("Name0", 0, Board(SortedSet[Tile](Tile(1, Color.RED, 0))), state = State.TURN)
      val player2 = Player("Name1", 1, Board(SortedSet[Tile](Tile(2, Color.RED, 0))))
      val player3 = Player("Name2", 2, Board(SortedSet[Tile]()))
      val player4 = Player("Name3", 3, Board(SortedSet[Tile]()))
      val players = Set[Player](player1, player2, player3, player4)
      val desk = Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.switchToNextPlayer()
      "userPutTileDown be 1" in {
        controller.currentP.number should be(1)
        controller.undo
      }
      "undo" in {
        controller.currentP.number should be(0)
        controller.redo
      }
      "redo" in {
        controller.currentP.number should be(1)
      }
    }
    "calling undo redo when taking a tile" should {
      val player1 = Player("Name0", 0, Board(SortedSet[Tile]()), state = State.TURN)
      val players = Set[Player](player1)
      val desk = Desk(players, Set(Tile(1, Color.RED, 0), Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[Tile]]())
      val controller = new Controller(desk)
      controller.userPutTileDown = 0
      controller.userFinishedPlay()
      "userPutTileDown be 1" in {
        controller.currentP.board.amountOfTiles() should be(1)
        controller.undo
      }
      "undo" in {
        controller.currentP.board.amountOfTiles() should be(0)
        controller.redo
      }
      "redo" in {
        controller.currentP.board.amountOfTiles() should be(1)
      }
    }
  }
}


