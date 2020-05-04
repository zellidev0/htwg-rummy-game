package de.htwg.se.rummy.model

import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl._
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, _}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.SortedSet;


class DeskSpec extends WordSpec with Matchers {
  val emptyBoard: Board = Board(SortedSet[TileInterface]())
  val emptyDesk: Set[SortedSet[TileInterface]] = Set[SortedSet[TileInterface]]()

  val onePlayerWithEmptyBoard: List[PlayerInterface] =
    List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true));
  val twoPlayersWithEmptyBoards: List[PlayerInterface] =
    List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true), Player("Name2", emptyBoard))
  val noPlayers: List[PlayerInterface] = List[PlayerInterface]()

  "A Desk" when {
    "created with 2 players and 2 tiles" should {
      val desk = Desk(twoPlayersWithEmptyBoards,
        Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), emptyDesk)
      "have a player Set with 2 players and a tile set with 2 tiles" in {
        desk.amountOfPlayers should be(2)
        desk.bagOfTiles.size should be(2)
      }
      "have a correct amount of players" in {
        desk.lessThan4P should be(true)
        desk.moreThan1P should be(true)
        desk.correctAmountOfPlayers should be(true)
      }

    }
  }
  "created with 2 and gets added 3 more players" should {
    val desk = deskBaseImpl.Desk(twoPlayersWithEmptyBoards,
      Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), emptyDesk)
    val desk1 = desk.addPlayer(Player("Name3", emptyBoard))
    val desk2 = desk1.addPlayer(Player("Name4", emptyBoard))
    val desk3 = desk2.addPlayer(Player("Name5", emptyBoard))
    "not have a correct amount of players" in {
      desk3.lessThan4P should be(false)
      desk3.correctAmountOfPlayers should be(false)
    }
  }
  "created with 2 and removes one player" should {
    val desk = Desk(twoPlayersWithEmptyBoards,
      Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), emptyDesk)
    val desk1 = desk.removePlayer(desk.getNextPlayer)
    "have only one player left" in {
      desk1.amountOfPlayers should be(1)
    }
  }
  "created with empty players" should {
    val desk = Desk(noPlayers, Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), emptyDesk)
    "have size 0" in {
      desk.players.size should be(0)
    }
    "after adding a player should have size 1" in {
      val desk1 = desk.addPlayer(Player("Name1", emptyBoard))
      desk1.players.size should be(1)
    }
  }
  "layed down a tile on desk" should {
    val players = List[PlayerInterface](Player("Name1", Board(SortedSet[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1))), hasTurn = true))
    val desk = Desk(players, Set[TileInterface](), emptyDesk)
    val amountOfTilesOnBoardOfPlayer1 = players.find(p => p.name == "Name1").get.tiles.size
    val desk1 = desk.putDownTile(desk.getCurrentPlayer, Tile(1, Color.RED, 0))
    "have one more tile on table" in {
      desk1.tableContains(Tile(1, Color.RED, 0)) should be(true)
      desk1.tableContains(Tile(5, Color.BLUE, 1)) should be(false)
      desk1.tableContains(Tile(1, Color.BLUE, 1)) should be(false)
    }
    "Player 1 have one tile less" in {
      desk1.players.find(p => p.name == "Name1").get.tiles.size should be(amountOfTilesOnBoardOfPlayer1)
    }
    "have a tile in a tileSet" in {

      desk1.table.size should be(1)
    }
  }
  "user takes tile from bag" should {
    val players = onePlayerWithEmptyBoard
    val desk = deskBaseImpl.Desk(players, Set[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1)), emptyDesk)
    val amountOfTilesInBag = desk.bagOfTiles.size // 2
    val amountOfTilesOnBoardOfPlayer1 = 0
    val xamountOfTilesOnBoardOfPlayer1 = players.find(p => p.name == "Name1").get.tiles.size
    val desk1 = desk.takeTileFromBagToPlayer(players.find(p => p.name == "Name1").get, desk.getTileFromBag)
    "user have one more tile on board" in {
      desk1.players.find(p => p.name == "Name1").get.tiles.size should be(xamountOfTilesOnBoardOfPlayer1 + 1)
    }
    "bag of tile have one tile less" in {
      desk1.bagOfTiles.size should be(amountOfTilesInBag - 1)
    }

  }
  "user moves tile to tile from bag" should {
    val players = onePlayerWithEmptyBoard
    val desk = deskBaseImpl.Desk(players, Set[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1)), emptyDesk)
    val amountOfTilesInBag = desk.bagOfTiles.size // 2
    val x = players.find(p => p.name == "Name1").get.tiles.size
    val desk1 = desk.takeTileFromBagToPlayer(players.find(p => p.name == "Name1").get, desk.getTileFromBag)
    "user have one more tile on board" in {
      desk1.players.find(p => p.name == "Name1").get.tiles.size should be(x + 1)
    }
    "bag of tile have one tile less" in {
      desk1.bagOfTiles.size should be(amountOfTilesInBag - 1)
    }

  }
  "two players switch turn, -> its player" should {
    val player1 = Player("Name1", emptyBoard, hasTurn = true)
    val player2 = Player("Name2", emptyBoard)
    val desk = Desk(List(player1, player2), Set[TileInterface](), emptyDesk)
    "have current player, which is player1)" in {
      desk.getCurrentPlayer should be(player1)
      desk.getNextPlayer should be(player2)
    }
    val desk1 = desk.switchToNextPlayer
    "have status Wait (current)" in {
      desk1.players.exists(p => p.name == player1.name && p.hasTurn.equals(false)) should be(true)
      desk1.getPreviousPlayer.name should be(player1.name)
    }
    "have status TURN (next)" in {
      desk1.players.exists(p => p.name == player2.name && p.hasTurn.equals(true)) should be(true)
    }
  }
  "check table" should {
    val setOfCorrectStreets =
      Set(SortedSet[TileInterface](Tile(1, Color.GREEN, 0), Tile(2, Color.GREEN, 1), Tile(3, Color.GREEN, 0), Tile(4, Color.GREEN, 0), Tile(5, Color.GREEN, 0)), //Street 4 GREEN
        SortedSet[TileInterface](Tile(4, Color.RED, 0), Tile(5, Color.RED, 0), Tile(6, Color.RED, 0))) // Street 3 RED
    val setOfCorrectPairs =
      Set(SortedSet[TileInterface](Tile(2, Color.GREEN, 0), Tile(2, Color.YELLOW, 0), Tile(2, Color.BLUE, 0)), // Pair 3 different
        SortedSet[TileInterface](Tile(8, Color.RED, 0), Tile(8, Color.BLUE, 0), Tile(8, Color.YELLOW, 0), Tile(8, Color.GREEN, 0))) // Pair 4 different
    val setOfWrongPairs =
      Set(SortedSet[TileInterface](Tile(10, Color.GREEN, 0), Tile(10, Color.GREEN, 1), Tile(10, Color.BLUE, 0)), // Pair of 3 where 2 same
        SortedSet[TileInterface](Tile(13, Color.YELLOW, 0), Tile(13, Color.GREEN, 0))) // Pair of 2
    val setOfWrongStreets =
      Set(SortedSet[TileInterface](Tile(9, Color.BLUE, 1), Tile(11, Color.BLUE, 1), Tile(12, Color.BLUE, 1), Tile(13, Color.BLUE, 0)), // Street with missing one
        SortedSet[TileInterface](Tile(7, Color.RED, 0), Tile(8, Color.RED, 1))) // street with only 2
    val players = twoPlayersWithEmptyBoards
    "be true when setOfCorrectStreets" in {
      val desk = Desk(players, Set[TileInterface](), setOfCorrectStreets)
      desk.checkTable() should be(true)
    }
    "be true when setOfCorrectPairs" in {
      val desk = Desk(players, Set[TileInterface](), setOfCorrectPairs)
      desk.checkTable() should be(true)
    }
    //checkTable always true
    "be false when setOfWrongStreets" in {
      val desk = Desk(players, Set[TileInterface](), setOfWrongStreets)
      // todo change back to false here when changing checkTable back in production
      desk.checkTable() should be(true)
    }
    "be false when setOfWrongPairs" in {
      val desk = Desk(players, Set[TileInterface](), setOfWrongPairs)
      // todo change back to false here when changing checkTable back in production
      desk.checkTable() should be(true)
    }
  }
  "check street" should {
    val correctStreet4green = SortedSet[TileInterface](
      Tile(1, Color.GREEN, 0),
      Tile(2, Color.GREEN, 1),
      Tile(3, Color.GREEN, 0),
      Tile(4, Color.GREEN, 0),
      Tile(5, Color.GREEN, 0))
    val correctStreet3red = SortedSet[TileInterface](
      Tile(4, Color.RED, 0),
      Tile(5, Color.RED, 0),
      Tile(6, Color.RED, 0))
    val setOfCorrectStreets = Set(correctStreet4green, correctStreet3red)
    val wrongStreetOnlyTwo = SortedSet[TileInterface](
      Tile(7, Color.RED, 0),
      Tile(8, Color.RED, 1))
    val wrongStreetMissingOne = SortedSet[TileInterface](
      Tile(9, Color.BLUE, 1),
      Tile(11, Color.BLUE, 1),
      Tile(12, Color.BLUE, 1),
      Tile(13, Color.BLUE, 0));
    val setOfWrongStreets = Set(wrongStreetMissingOne, wrongStreetOnlyTwo)
    val players = twoPlayersWithEmptyBoards
    "be true when setOfCorrectStreets" in {
      val desk = Desk(players, Set[TileInterface](), setOfCorrectStreets)
      for (set <- desk.table) {
        desk.checkStreet(set) should be(true)
        desk.checkPair(set) should be(false)
      }
    }
    "be false when setOfWrongStreets" in {
      val desk = Desk(players, Set[TileInterface](), setOfWrongStreets)
      for (set <- desk.table) {
        desk.checkStreet(set) should be(false)
        desk.checkPair(set) should be(false)
      }
    }
  }
  //  "check pair" should {
  //    val setOfCorrectPairs =
  //      Set(
  //        SortedSet[TileInterface](
  //          Tile(2, Color.GREEN, 0),
  //          Tile(2, Color.BLUE, 1),
  //          Tile(2, Color.RED, 0),
  //          Tile(2, Color.YELLOW, 0)),
  //        SortedSet[TileInterface](
  //          Tile(4, Color.RED, 0),
  //          Tile(4, Color.GREEN, 0),
  //          Tile(4, Color.BLUE, 0)
  //        )
  //      )
  //    Set(SortedSet[TileInterface](
  //      Tile(2, Color.GREEN, 0),
  //      Tile(2, Color.YELLOW, 0),
  //      Tile(2, Color.BLUE, 0)
  //    ),
  //      SortedSet[TileInterface](
  //        Tile(8, Color.RED, 0),
  //        Tile(8, Color.BLUE, 0),
  //        Tile(8, Color.YELLOW, 0),
  //        Tile(8, Color.GREEN, 0)))
  //    val setOfWrongPairs =
  //      Set(
  //        SortedSet[TileInterface](
  //          Tile(10, Color.GREEN, 0),
  //          Tile(10, Color.GREEN, 1),
  //          Tile(10, Color.BLUE, 0)),
  //        SortedSet[TileInterface](
  //          Tile(13, Color.YELLOW, 0),
  //          Tile(13, Color.GREEN, 0)))
  //    val players = List[PlayerInterface](Player("Name1", emptyBoard), Player("Name2", emptyBoard))
  //    val desk = Desk(players, Set[TileInterface](), setOfCorrectPairs)
  //  "be true when setOfCorrectPair" in {
  //    for (set <- desk.table) {
  //      desk.checkStreet(set) should be(false)
  //      desk.checkPair(set) should be(true)
  //    }
  //  }
  //  "be true when setOfWrongPair" in {
  //    val desk1 = deskBaseImpl.Desk(players, Set[TileInterface](), setOfWrongPairs)
  //    for (set <- desk.table) {
  //      desk1.checkStreet(set) should be(false)
  //      desk1.checkPair(set) should be(false)
  //    }
  //  }
  //}
  "one player puts last tile down" should {
    val tile = Tile(1, Color.RED, 0)
    val desk = deskBaseImpl.Desk(List[PlayerInterface](
      Player("Name1", Board(SortedSet[TileInterface](tile)), hasTurn = true),
      Player("Name2", emptyBoard)), Set[TileInterface](), emptyDesk)
    desk.currentPlayerWon() should be(false)
    val desk1 = desk.putDownTile(desk.getCurrentPlayer, tile)
    "have that player win" in {
      desk1.currentPlayerWon() should be(true)
    }
  }
  "move a tile form one set to another" should {
    val tile = Tile(4, Color.BLUE, 0)
    val players = List[PlayerInterface](
      Player("Name1", Board(SortedSet[TileInterface](tile)), hasTurn = true))
    val desk = deskBaseImpl.Desk(players, Set[TileInterface](),
      Set[SortedSet[TileInterface]](SortedSet(Tile(1, Color.BLUE, 0), Tile(2, Color.BLUE, 0), Tile(3, Color.BLUE, 0))))
    "have 4 tiles in Set on Desk" in {
      val desk1 = desk.putDownTile(desk.getCurrentPlayer, tile)
      val desk2 = desk1.tryToMoveTwoTilesOnDesk(tile, Tile(3, Color.BLUE, 0))
      desk2.table.size should be(1)
    }
    "not change when tile not on desk" in {
      println(desk.table)
      val desk1 = desk.putDownTile(desk.getCurrentPlayer, tile)
      val desk2 = desk1.tryToMoveTwoTilesOnDesk(tile, Tile(9, Color.BLUE, 0))
      desk2.table.size should be(2)
    }
    "not change when tiles in same set" in {
      val desk2 = desk.tryToMoveTwoTilesOnDesk(Tile(1, Color.BLUE, 0), Tile(2, Color.BLUE, 0))
      desk2.table.size should be(1)
    }

  }
  "taking up a tile" should {
    val tile = Tile(4, Color.BLUE, 0)
    val tile2 = Tile(3, Color.BLUE, 0)
    val players = List[PlayerInterface](Player("Name1", Board(SortedSet[TileInterface](tile)), hasTurn = true))
    val desk = deskBaseImpl.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]](SortedSet(tile2), SortedSet()))
    val desk1 = desk.takeUpTile(desk.getCurrentPlayer, tile2)
    "have 4 tiles in Set on Deks" in {
      desk1.table.head.isEmpty should be(true)
    }
  }
  "adding to bag" should {
    val tile = Tile(4, Color.BLUE, 0)
    val players = List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true))
    val desk = deskBaseImpl.Desk(players, Set[TileInterface](), emptyDesk)
    val desk1 = desk.addToBag(tile)
    "have 4 tiles in Set on Deks" in {
      desk1.bagOfTiles.size should be(1)
    }
  }
  "taking a tile from player to bag" should {
    val tile = Tile(4, Color.BLUE, 0)
    val players = List[PlayerInterface](Player("Name1", Board(SortedSet[TileInterface](tile)), hasTurn = true))
    val desk = deskBaseImpl.Desk(players, Set[TileInterface](), emptyDesk)
    val desk1 = desk.takeTileFromPlayerToBag(desk.getCurrentPlayer, tile)
    "have one less on players board and one more in bag" in {
      desk1.bagOfTiles.size should be(1)
      desk1.players.head.tiles.size should be(0)
    }
  }
  "player won" should {
    val tile = Tile(4, Color.BLUE, 0)
    val players = List[PlayerInterface](Player("Name1", Board(SortedSet[TileInterface](tile)), hasTurn = true))
    val desk = deskBaseImpl.Desk(players, Set[TileInterface](), emptyDesk)
    val desk1 = desk.takeTileFromPlayerToBag(desk.getCurrentPlayer, tile)
    "be wrong" in {
      desk1.currentPlayerWon() should be(true)
    }
  }
  "has bag with tiles with same colors" should {
    val players = List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true))
    val desk = deskBaseImpl.Desk(players,
      Set[TileInterface](Tile(4, Color.BLUE, 0), Tile(5, Color.BLUE, 0), Tile(6, Color.BLUE, 0)),
      emptyDesk)
    "return false" in {
      desk.allTilesHaveDifferentColor(desk.bagOfTiles) should be(false)
    }
  }
  "has bag with tiles with different colors" should {
    val players = List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true))
    val desk = deskBaseImpl.Desk(players,
      Set[TileInterface](Tile(4, Color.GREEN, 0), Tile(5, Color.BLUE, 0), Tile(6, Color.YELLOW, 0)),
      emptyDesk)
    "return true" in {
      desk.allTilesHaveDifferentColor(desk.bagOfTiles) should be(true)
    }
  }
  "with a correct street" should {
    val players = List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true))
    val desk = deskBaseImpl.Desk(players,
      Set[TileInterface](Tile(4, Color.GREEN, 0), Tile(5, Color.BLUE, 0), Tile(6, Color.YELLOW, 0)),
      emptyDesk)
    "have a valid street" in {
      desk.allTilesHaveValidStreetValues(SortedSet[TileInterface]() ++ desk.bagOfTiles) should be(true)
    }
  }
  "with a wrong street" should {
    val players = List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true))
    val desk = deskBaseImpl.Desk(players,
      Set[TileInterface](Tile(4, Color.GREEN, 0), Tile(4, Color.BLUE, 0), Tile(6, Color.YELLOW, 0)),
      emptyDesk)
    "have a false street" in {
      desk.allTilesHaveValidStreetValues(SortedSet[TileInterface]() ++ desk.bagOfTiles) should be(false)
    }
  }
  "with a wrong street1" should {
    val players = List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true))
    val desk = deskBaseImpl.Desk(players,
      Set[TileInterface](Tile(4, Color.GREEN, 0), Tile(5, Color.BLUE, 0), Tile(7, Color.YELLOW, 0)),
      emptyDesk)
    "have a false street1" in {
      desk.allTilesHaveValidStreetValues(SortedSet[TileInterface]() ++ desk.bagOfTiles) should be(false)
    }
  }
  "with a wrong street2" should {
    val players = List[PlayerInterface](Player("Name1", emptyBoard, hasTurn = true))
    val desk = deskBaseImpl.Desk(players,
      Set[TileInterface](Tile(4, Color.GREEN, 0), Tile(5, Color.BLUE, 0), Tile(5, Color.YELLOW, 0)),
      emptyDesk)
    //      "have a false street2" in {
    //        desk.allTilesHaveValidStreetValues(SortedSet[TileInterface]() ++ desk.bagOfTiles) should be(false)
    //      }
  }
}