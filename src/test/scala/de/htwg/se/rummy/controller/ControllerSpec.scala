package de.htwg.se.rummy.controller

import java.io.File
import java.nio.file.{Files, Paths}

import de.htwg.se.rummy.controller.component.ControllerState._
import de.htwg.se.rummy.controller.component.{AnswerState, Controller}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, _}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{TileInterface, _}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.SortedSet

class ControllerSpec extends WordSpec with Matchers {

//
//  "A Controller" when {
//    "user finishes play" should {
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))), state = PlayerState.TURN)
//      val player2 = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(2, Color.RED, 0))))
//      val player3 = Player("Name2", 2, Board(SortedSet[TileInterface]()))
//      val player4 = Player("Name3", 3, Board(SortedSet[TileInterface]()))
//      val players = Set[PlayerInterface](player1, player2, player3, player4)
//      val desk = deskBaseImpl.Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.userFinishedPlay()
//      "userPutTileDown be 0 " in {
//        controller.desk.players.size should be(4)
//        controller.desk.players.find(_.number == 0).get.tiles.size should be(2)
//      }
//      controller.switchToNextPlayer()
//      controller.userPutTileDown += 1
//      controller.userFinishedPlay()
//      "userPutTileDown be 1 and table is ok " in {
//        player1.tiles.size should be(1)
//      }
//      controller.layDownTile(Tile.stringToTile("2R0").get)
//      controller.userFinishedPlay()
//      "table be not correct " in {
//        player1.tiles.size should be(1)
//        player2.tiles.size should be(1)
//        controller.desk.table.size should be(1)
//      }
//    }
//    "usr finishes play and wins" should {
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN)
//      val player2 = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(2, Color.RED, 0))))
//      val players = Set[PlayerInterface](player1, player2)
//      val desk = deskBaseImpl.Desk(players, Set(), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.userPutTileDown = 1
//      controller.userFinishedPlay()
//      "user should win " in {
//        controller.currentAnswerState should be(AnswerState.P_WON)
//      }
//    }
//    "user finished play and bag is empty" should {
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface](Tile(3, Color.RED, 0), Tile(3, Color.RED, 1))), state = PlayerState.TURN)
//      val player2 = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(2, Color.RED, 0))))
//      val players = Set[PlayerInterface](player1, player2)
//      val desk = deskBaseImpl.Desk(players, Set(), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.userFinishedPlay()
//      "bag be empty " in {
//        controller.desk.bagOfTiles.isEmpty should be(true)
//        controller.currentControllerState should be(MENU)
//      }
//    }
//    "should move two correct and movable tiles" should {
//      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]())))
//      val desk = deskBaseImpl.Desk(players, Set(), Set[SortedSet[TileInterface]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0))))
//      val controller = new Controller(desk)
//      controller.moveTile(Tile.stringToTile("2R0").get, Tile.stringToTile("1R0").get)
//      "have only one set with the 2 tiles side by side" in {
//        controller.desk.table.size should be(1)
//        controller.desk.table.contains(SortedSet(Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
//      }
//      controller.moveTile(Tile.stringToTile("2R1").get, Tile.stringToTile("1R0").get)
//      "have change nothing" in {
//        controller.desk.table.size should be(1)
//        controller.desk.table.contains(SortedSet(Tile(2, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
//      }
//    }
//    "lay Down a tile the user really has" should {
//      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))), state = PlayerState.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]())))
//      val desk = deskBaseImpl.Desk(players, Set(), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.layDownTile(Tile.stringToTile("1R0").get)
//      "should work (have only one set with one tile)" in {
//        controller.desk.table.size should be(1)
//        controller.desk.table.contains(SortedSet(Tile(1, Color.RED, 0), Tile(1, Color.RED, 0))) should be(true)
//      }
//      controller.layDownTile(Tile.stringToTile("1R1").get)
//      "should not work (have only one set with one tile)" in {
//        controller.desk.table.size should be(1)
//        controller.desk.table.contains(SortedSet[TileInterface](Tile(1, Color.RED, 0))) should be(true)
//        controller.desk.table.contains(SortedSet[TileInterface](Tile(1, Color.RED, 1))) should be(false)
//      }
//    }
//    "adding a player and have less than 4" should {
//      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN))
//      val desk = deskBaseImpl.Desk(players, Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.createDesk(12) // must create or you cant init all players
//      val amountOfPlayersBefore = controller.desk.players.size
//      controller.addPlayerAndInit("Name2", 12)
//      "have 2 players" in {
//        controller.hasLessThan4Players should be(true)
//        controller.desk.players.size should be(amountOfPlayersBefore + 1)
//        controller.addPlayerAndInit("Name3", 12)
//      }
//      "have 3 players" in {
//        controller.hasLessThan4Players should be(true)
//        controller.desk.players.size should be(amountOfPlayersBefore + 2)
//        controller.addPlayerAndInit("Name4", 12)
//      }
//      "have 4 players" in {
//        controller.hasLessThan4Players should be(true)
//        controller.desk.players.size should be(amountOfPlayersBefore + 3)
//        controller.addPlayerAndInit("Name5", 12)
//      }
//      "have4, which means no more player" in {
//        controller.hasLessThan4Players should be(false)
//        controller.desk.players.size should be(amountOfPlayersBefore + 4)
//      }
//    }
//    "creates a desk" should {
//      val desk = deskBaseImpl.Desk(Set[PlayerInterface](), Set(), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.createDesk(13)
//      "have 104 tiles" in {
//        controller.desk.bagOfTiles.size should be(13 * 4 * 2)
//      }
//    }
//    "getting tile sets" should {
//      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN))
//      val sets = Set[SortedSet[TileInterface]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0)))
//      val desk = Desk(players, Set(), sets)
//      val controller = new Controller(desk)
//      val oldState = controller.currentControllerState
//      "be sets" in {
//        controller.viewOfTable should be(sets)
//      }
//    }
//    "switching players" should {
//      val player0 = Player("Name0", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN)
//      val player1 = Player("Name1", 1, Board(SortedSet[TileInterface]()))
//      val player2 = Player("Name2", 2, Board(SortedSet[TileInterface]()))
//      val player3 = Player("Name3", 3, Board(SortedSet[TileInterface]()))
//      val players = Set[PlayerInterface](player0, player1, player2, player3)
//      val controller = new Controller(deskBaseImpl.Desk(players, Set(), Set[SortedSet[TileInterface]]()))
//      "have the correct previous, current and next player" in {
//        controller.getPreviousPlayer.number should be(player3.number)
//        controller.getCurrentPlayer.number should be(player0.number)
//        controller.getNextPlayer.number should be(player1.number)
//        controller.switchToNextPlayer()
//      }
//      "have the correct previous, current and next player1" in {
//        controller.getPreviousPlayer.number should be(player0.number)
//        controller.getCurrentPlayer.number should be(player1.number)
//        controller.getNextPlayer.number should be(player2.number)
//        controller.switchToNextPlayer()
//      }
//      "have the correct previous, current and next player2" in {
//        controller.getPreviousPlayer.number should be(player1.number)
//        controller.getCurrentPlayer.number should be(player2.number)
//        controller.getNextPlayer.number should be(player3.number)
//        controller.switchToNextPlayer()
//      }
//      "have the correct previous, current and next player3" in {
//        controller.getPreviousPlayer.number should be(player2.number)
//        controller.getCurrentPlayer.number should be(player3.number)
//        controller.getNextPlayer.number should be(player0.number)
//        controller.switchToNextPlayer()
//      }
//      "have the correct previous, current and next player4" in {
//        controller.getPreviousPlayer.number should be(player3.number)
//        controller.getCurrentPlayer.number should be(player0.number)
//        controller.getNextPlayer.number should be(player1.number)
//      }
//    }
//    "name input finished" should {
//      val players = Set[PlayerInterface]()
//      val desk = deskBaseImpl.Desk(players, Set(), Set[SortedSet[TileInterface]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0))))
//      val controller = new Controller(desk)
//      controller.createDesk(12) // must create desk or you cant init players
//      val oldState = controller.currentControllerState
//      controller.addPlayerAndInit("Name1", 12)
//      controller.nameInputFinished()
//      "when having not correct amount of players" in {
//        oldState should be(INSERTING_NAMES)
//        controller.currentControllerState should be(INSERTING_NAMES)
//        controller.addPlayerAndInit("Name2", 12)
//        controller.nameInputFinished()
//      }
//      "when having correct amount of players" in {
//        oldState should be(INSERTING_NAMES)
//        controller.currentControllerState should be(P_TURN)
//      }
//    }
//    "get amount of players" should {
//      val players = Set[PlayerInterface]()
//      val sets = Set[SortedSet[TileInterface]](SortedSet(Tile(2, Color.RED, 0)), SortedSet(Tile(1, Color.RED, 0)))
//      val desk = deskBaseImpl.Desk(players, Set(), sets)
//      val controller = new Controller(desk)
//      controller.createDesk(12)
//      controller.addPlayerAndInit("Name0", 12)
//      val oldState = controller.currentControllerState
//      "be 1" in {
//        controller.getAmountOfPlayers should be(1)
//        controller.addPlayerAndInit("Name1", 12)
//      }
//      "be 2" in {
//        controller.getAmountOfPlayers should be(2)
//        controller.addPlayerAndInit("Name2", 12)
//      }
//      "be 3" in {
//        controller.getAmountOfPlayers should be(3)
//        controller.addPlayerAndInit("Name3", 12)
//      }
//      "be 4" in {
//        controller.getAmountOfPlayers should be(4)
//        controller.addPlayerAndInit("Name4", 12)
//      }
//      "be also 4" in {
//        controller.currentControllerState should be(INSERTING_NAMES)
//        controller.addPlayerAndInit("Name5", 12)
//      }
//    }
//    "sets on desk are correct" should {
//      val setOfCorrectStreets =
//        Set(SortedSet[TileInterface](Tile(1, Color.GREEN, 0), Tile(2, Color.GREEN, 1), Tile(3, Color.GREEN, 0), Tile(4, Color.GREEN, 0), Tile(5, Color.GREEN, 0)), //Street 4 GREEN
//          SortedSet[TileInterface](Tile(4, Color.RED, 0), Tile(5, Color.RED, 0), Tile(6, Color.RED, 0))) // Street 3 RED
//      val setOfCorrectPairs =
//        Set(SortedSet[TileInterface](Tile(2, Color.GREEN, 0), Tile(2, Color.YELLOW, 0), Tile(2, Color.BLUE, 0)), // Pair 3 different
//          SortedSet[TileInterface](Tile(8, Color.RED, 0), Tile(8, Color.BLUE, 0), Tile(8, Color.YELLOW, 0), Tile(8, Color.GREEN, 0))) // Pair 4 different
//      val setOfWrongPairs =
//        Set(SortedSet[TileInterface](Tile(10, Color.GREEN, 0), Tile(10, Color.GREEN, 1), Tile(10, Color.BLUE, 0)), // Pair of 3 where 2 same
//          SortedSet[TileInterface](Tile(13, Color.YELLOW, 0), Tile(13, Color.GREEN, 0))) // Pair of 2
//      val setOfWrongStreets =
//        Set(SortedSet[TileInterface](Tile(9, Color.BLUE, 1), Tile(11, Color.BLUE, 1), Tile(12, Color.BLUE, 1), Tile(13, Color.BLUE, 0)), // Street with missing one
//          SortedSet[TileInterface](Tile(7, Color.RED, 0), Tile(8, Color.RED, 1))) // street with only 2
//      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]())))
//      val desk = deskBaseImpl.Desk(players, Set[TileInterface](), setOfCorrectStreets)
//      var controller = new Controller(deskBaseImpl.Desk(Set[PlayerInterface](), Set(), setOfCorrectStreets))
//      "be true when setOfCorrectStreets" in {
//        controller.setsOnDeskAreCorrect should be(true)
//      }
//      "be true when setOfCorrectPairs" in {
//        controller = new Controller(deskBaseImpl.Desk(Set[PlayerInterface](), Set(), setOfCorrectPairs))
//        controller.setsOnDeskAreCorrect should be(true)
//      }
//      "be true when setOfWrongStreets" in {
//        controller = new Controller(deskBaseImpl.Desk(Set[PlayerInterface](), Set(), setOfWrongStreets))
//        controller.setsOnDeskAreCorrect should be(false)
//      }
//      "be true when setOfWrongPairs" in {
//        controller = new Controller(deskBaseImpl.Desk(Set[PlayerInterface](), Set(), setOfWrongPairs))
//        controller.setsOnDeskAreCorrect should be(false)
//      }
//    }
//    "removing tile from table" should {
//      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]())))
//      val desk = deskBaseImpl.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]](SortedSet(Tile(1, Color.RED, 0))))
//      val controller = new Controller(desk)
//      controller.removeTileFromSet(Tile(1, Color.RED, 0))
//      "should have one less " in {
//        controller.desk.table.contains(SortedSet(Tile(1, Color.RED, 0))) should be(false)
//      }
//    }
//    "calling undo redo when laying down" should {
//      val tile1 = Tile(1, Color.RED, 0)
//      val player1 = Player("Name1", 0, Board(SortedSet[TileInterface](tile1)), state = PlayerState.TURN)
//      val players = Set[PlayerInterface](player1)
//      val desk = deskBaseImpl.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.layDownTile(Tile.stringToTile("1R0").get)
//      "undo" in {
//        controller.desk.getCurrentPlayer.has(tile1) should be(false)
//        controller.desk.table.contains(SortedSet(tile1)) should be(true)
//        controller.undo()
//        controller.desk.getCurrentPlayer.has(tile1) should be(true)
//        controller.desk.table.contains(SortedSet(tile1)) should be(false)
//        controller.redo()
//      }
//      "redo" in {
//        controller.desk.getCurrentPlayer.has(tile1) should be(false)
//        controller.desk.table.contains(SortedSet(tile1)) should be(true)
//      }
//    }
//    "calling undo redo when moving tile" should {
//      val tile1 = Tile(1, Color.RED, 0)
//      val tile2 = Tile(2, Color.RED, 0)
//      val player1 = Player("Name1", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN)
//      val players = Set[PlayerInterface](player1)
//      val desk = deskBaseImpl.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]](SortedSet[TileInterface](tile1), SortedSet[TileInterface](tile2)))
//      val controller = new Controller(desk)
//      controller.moveTile(Tile.stringToTile("1R0").get, Tile.stringToTile("2R0").get)
//      "undo" in {
//        controller.desk.table.size should be(1)
//        controller.desk.table.contains(SortedSet(tile1, tile2)) should be(true)
//        controller.undo()
//        controller.desk.table.size should be(2)
//        controller.desk.table.contains(SortedSet(tile1, tile2)) should be(false)
//        controller.redo()
//      }
//      "redo" in {
//        controller.desk.table.size should be(1)
//        controller.desk.table.contains(SortedSet(tile1, tile2)) should be(true)
//      }
//    }
//    "calling undo redo when name inserting" should {
//      val desk = deskBaseImpl.Desk(Set[PlayerInterface](), Set[TileInterface](), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.createDesk(12)
//      controller.addPlayerAndInit("Name0", 12)
//      "undo" in {
//        controller.desk.amountOfPlayers should be(1)
//        controller.undo()
//        controller.desk.amountOfPlayers should be(0)
//        controller.redo()
//      }
//      "redo" in {
//        controller.desk.amountOfPlayers should be(1)
//      }
//    }
//    "calling undo redo when user finishes play" should {
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))), state = PlayerState.TURN)
//      val player2 = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(2, Color.RED, 0))))
//      val player3 = Player("Name2", 2, Board(SortedSet[TileInterface]()))
//      val player4 = Player("Name3", 3, Board(SortedSet[TileInterface]()))
//      val players = Set[PlayerInterface](player1, player2, player3, player4)
//      val desk = deskBaseImpl.Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.userPutTileDown = 1
//      controller.userFinishedPlay()
//      "userPutTileDown be 1" in {
//        controller.userPutTileDown should be(0)
//        controller.undo()
//      }
//      "undo" in {
//        controller.userPutTileDown should be(1)
//        controller.redo()
//      }
//      "redo" in {
//        controller.userPutTileDown should be(0)
//      }
//    }
//    "calling undo redo when switching player" should {
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))), state = PlayerState.TURN)
//      val player2 = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(2, Color.RED, 0))))
//      val player3 = Player("Name2", 2, Board(SortedSet[TileInterface]()))
//      val player4 = Player("Name3", 3, Board(SortedSet[TileInterface]()))
//      val players = Set[PlayerInterface](player1, player2, player3, player4)
//      val desk = deskBaseImpl.Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.switchToNextPlayer()
//      "userPutTileDown be 1" in {
//        controller.getCurrentPlayer.number should be(1)
//        controller.undo()
//      }
//      "undo" in {
//        controller.getCurrentPlayer.number should be(0)
//        controller.redo()
//      }
//      "redo" in {
//        controller.getCurrentPlayer.number should be(1)
//      }
//    }
//    "calling undo redo when taking a tile" should {
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface]()), state = PlayerState.TURN)
//      val players = Set[PlayerInterface](player1)
//      val desk = deskBaseImpl.Desk(players, Set(Tile(1, Color.RED, 0), Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      controller.userPutTileDown = 0
//      controller.userFinishedPlay()
//      "userPutTileDown be 1" in {
//        controller.getCurrentPlayer.tiles.size should be(1)
//        controller.undo()
//      }
//      "undo" in {
//        controller.getCurrentPlayer.tiles.size should be(0)
//        controller.redo()
//      }
//      "redo" in {
//        controller.getCurrentPlayer.tiles.size should be(1)
//      }
//    }
//    "accessing view of board" should {
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface](Tile(3, Color.BLUE, 0))), state = PlayerState.TURN)
//      val players = Set[PlayerInterface](player1)
//      val desk = deskBaseImpl.Desk(players, Set(Tile(1, Color.RED, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]]())
//      val controller = new Controller(desk)
//      "return this view" in {
//        controller.viewOfBoard should be(SortedSet[TileInterface](Tile(3, Color.BLUE, 0)))
//      }
//    }
//    "reading a file" should {
//      //ATTENTION, will fail if file desk.xml exists. please remove file before running test
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1), Tile(2, Color.RED, 0))), state = PlayerState.TURN)
//      val player2 = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1), Tile(2, Color.RED, 0))), state = PlayerState.TURN)
//      val players = Set[PlayerInterface](player1, player2)
//      val desk = deskBaseImpl.Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]](SortedSet(Tile(10, Color.BLUE, 0), Tile(10, Color.RED, 0), Tile(10, Color.GREEN, 0))))
//      val controller = new Controller(desk)
//      "be no file" in {
//        Files.exists(Paths.get("/home/julian/Documents/se/rummy/test.xml")) should be(false)
//      }
//
//    }
//    "storing a file" should {
//      //ATTENTION, will fail if file desk.xml exists. please remove file before running test
//      if (Files.exists(Paths.get("target/desk.xml"))) {
//        new File("target/desk.xml").delete()
//      }
//      if (Files.exists(Paths.get("target/desk.json"))) {
//        new File("target/desk.json").delete()
//      }
//      val player1 = Player("Name0", 0, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1), Tile(2, Color.RED, 0))), state = PlayerState.TURN)
//      val player2 = Player("Name1", 1, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1), Tile(2, Color.RED, 0))), state = PlayerState.TURN)
//      val players = Set[PlayerInterface](player1, player2)
//      val desk = deskBaseImpl.Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]](SortedSet(Tile(10, Color.BLUE, 0), Tile(10, Color.RED, 0), Tile(10, Color.GREEN, 0))))
//      val controller = new Controller(desk)
//      "be no file" in {
//        var exists = false
//        if (Files.exists(Paths.get("target/desk.xml"))) {
//          exists = true
//        } else if (Files.exists(Paths.get("target/desk.json"))) {
//          exists = true
//        }
//        exists should be(false)
//        controller.storeFile()
//        controller.loadFile()
//      }
//      "be a file now" in {
//        var exists = false
//        if (Files.exists(Paths.get("target/desk.xml"))) {
//          exists = true
//        } else if (Files.exists(Paths.get("target/desk.json"))) {
//          exists = true
//        }
//        exists should be(true)
//        if (Files.exists(Paths.get("target/desk.xml"))) {
//          new File("target/desk.xml").delete()
//        }
//        if (Files.exists(Paths.get("target/desk.json"))) {
//          new File("target/desk.json").delete()
//        }
//      }
//    }
//  }
}


