package model

import model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, Tile}
import model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.SortedSet;


class DeskSpec extends WordSpec with Matchers {
  type TileI = TileInterface

  // Tiles
  val tile1Red0: TileI = Tile(1, Color.RED, 0)
  val tile2Blue0: TileI = Tile(2, Color.BLUE, 0)
  val tile3Yellow0: TileI = Tile(3, Color.YELLOW, 0)
  val tile1Red1: TileI = Tile(1, Color.RED, 1)
  val tile2Blue1: TileI = Tile(2, Color.BLUE, 1)
  val tile3Yellow1: TileI = Tile(3, Color.YELLOW, 1)
  val tile4Red0: TileI = Tile(4, Color.RED, 0)
  val tile5Blue0: TileI = Tile(5, Color.BLUE, 0)
  val tile6Yellow0: TileI = Tile(6, Color.YELLOW, 0)
  val tile7Red0: TileI = Tile(7, Color.RED, 0)
  val tile8Blue0: TileI = Tile(8, Color.BLUE, 0)

  //Boards
  val emptyBoard: Board = Board(SortedSet[TileI]())
  val board_1R0_2B0_3Y0: Board = Board(SortedSet[TileI](tile1Red0, tile2Blue0, tile3Yellow0))
  val board_1R1_2B1_3Y1: Board = Board(SortedSet[TileI](tile1Red1, tile2Blue1, tile3Yellow1))
  val emptyDesk: Set[SortedSet[TileI]] = Set[SortedSet[TileI]]()

  //Players
  val player1_empty: PlayerInterface = Player("Name1", emptyBoard);
  val player2_empty: PlayerInterface = Player("Name2", emptyBoard);
  val player3_empty: PlayerInterface = Player("Name3", emptyBoard);
  val player4_empty: PlayerInterface = Player("Name4", emptyBoard);
  val player1_board: PlayerInterface = Player("Name1", board_1R0_2B0_3Y0);
  val player2_board: PlayerInterface = Player("Name2", board_1R1_2B1_3Y1);
  val onePlayerEmptyBoard: List[PlayerInterface] = List[PlayerInterface](player1_empty);
  val twoPlayersEmptyBoards: List[PlayerInterface] = List[PlayerInterface](player1_empty, player2_empty)
  val threePlayersEmptyBoards: List[PlayerInterface] = List[PlayerInterface](player1_empty, player2_empty, player3_empty)
  val fourPlayersEmptyBoards: List[PlayerInterface] = List[PlayerInterface](player1_empty, player2_empty, player3_empty, player4_empty)
  val onePlayer_1R0_2B0_3Y0: List[PlayerInterface] = List[PlayerInterface](player1_board);
  val twoPlayers_1R0_2B0_3Y0_and_1R1_2B1_3Y1: List[PlayerInterface] = List[PlayerInterface](player1_board, player2_board)
  val noPlayers: List[PlayerInterface] = List[PlayerInterface]()

  //Table
  val emptyTable: Set[SortedSet[TileI]] = Set[SortedSet[TileI]]()
  val table_4R0__5B0_6Y0: Set[SortedSet[TileI]] = Set[SortedSet[TileI]](SortedSet(tile4Red0), SortedSet(tile5Blue0, tile6Yellow0))

  //BagOfTiles
  val emptyBag: Set[TileI] = Set[TileI]()
  val bag_7R0_8B0: Set[TileI] = Set[TileI](tile7Red0, tile8Blue0)

  "A Desk" when {
    val desk = Desk(onePlayer_1R0_2B0_3Y0, emptyBag, table_4R0__5B0_6Y0)
    "try to move a tile on the table to another on the table" should {
      val desk1 = desk.tryToMoveTwoTilesOnDesk(tile4Red0, tile5Blue0)
      "work and delete empty sets if existing" in {
        desk1 match {
          case Some(value) => value.table should be(Set[SortedSet[TileI]](SortedSet(tile4Red0, tile5Blue0, tile6Yellow0)))
          case None => fail()
        }
        desk1.get.table.size should be(1)
      }
    }
    "try to move a tile that does not exist on the table to another on the table" should {
      val desk1 = desk.tryToMoveTwoTilesOnDesk(tile1Red0, tile5Blue0)
      "not work" in {
        desk1 match {
          case Some(_) => fail()
          case None => 1 should be(1)
        }
      }
    }
    "try to move a tile that does exist on the table to another that does not exist on the table" should {
      val desk1 = desk.tryToMoveTwoTilesOnDesk(tile5Blue0, tile1Red0)
      "not work" in {
        desk1 match {
          case Some(_) => fail()
          case None =>
        }
      }
    }
    "getting a tile" should {
      val desk1 = desk.getTileFromBag
      "not work for an empty bag" in {
        desk1 match {
          case Some(_) => fail()
          case None =>
        }
      }
      val desk2 = Desk(noPlayers, bag_7R0_8B0, emptyTable).getTileFromBag
      "work for an non empty bag" in {
        desk2 match {
          case Some(_) =>
          case None => fail()
        }
      }
    }
    "adding a player" should {
      val desk1 = desk.addPlayer(player2_empty)
      val desk2 = desk1.addPlayer(player2_empty)
      "have one more player" in {
        desk1.players.size should be(2)
        desk2.players.size should be(3)
      }
    }
    "switching 4 Players" should {
      val desk1 = Desk(fourPlayersEmptyBoards, emptyBag, emptyTable)
      val desk2 = desk1.switchToNextPlayer
      val desk3 = desk2.switchToNextPlayer
      val desk4 = desk3.switchToNextPlayer
      val desk5 = desk4.switchToNextPlayer
      "go in a circle with 4 Players" in {
        desk1.getCurrentPlayer should be(player1_empty)
        desk2.getCurrentPlayer should be(player2_empty)
        desk3.getCurrentPlayer should be(player3_empty)
        desk4.getCurrentPlayer should be(player4_empty)
        desk5.getCurrentPlayer should be(player1_empty)
      }
    }
    "switching 3 Players" should {
      val desk1 = Desk(threePlayersEmptyBoards, emptyBag, emptyTable)
      val desk2 = desk1.switchToNextPlayer
      val desk3 = desk2.switchToNextPlayer
      val desk4 = desk3.switchToNextPlayer
      val desk5 = desk4.switchToNextPlayer
      "go in a circle with 3 Players" in {
        desk1.getCurrentPlayer should be(player1_empty)
        desk2.getCurrentPlayer should be(player2_empty)
        desk3.getCurrentPlayer should be(player3_empty)
        desk4.getCurrentPlayer should be(player1_empty)
        desk5.getCurrentPlayer should be(player2_empty)
      }
    }
    "switching 2 Players" should {
      val desk1 = Desk(twoPlayersEmptyBoards, emptyBag, emptyTable)
      val desk2 = desk1.switchToNextPlayer
      val desk3 = desk2.switchToNextPlayer
      val desk4 = desk3.switchToNextPlayer
      "go in a circle with 2 Players" in {
        desk1.getCurrentPlayer should be(player1_empty)
        desk2.getCurrentPlayer should be(player2_empty)
        desk3.getCurrentPlayer should be(player1_empty)
        desk4.getCurrentPlayer should be(player2_empty)
      }
    }
    "a player taking up a tile" should {
      val desk1 = Desk(onePlayer_1R0_2B0_3Y0, bag_7R0_8B0, emptyTable)
        .takeTileFromBagToPlayer(player1_board, tile7Red0)
      "work" in {
        desk1.bagOfTiles should be(bag_7R0_8B0.drop(1))
        desk1.getCurrentPlayer.board should be(player1_board.board.add(bag_7R0_8B0.head))
      }
    }
    "one player is in" should {
      val desk1 = Desk(noPlayers, emptyBag, emptyTable)
      val desk2 = desk1.addPlayer(player1_empty)
      val desk3 = desk2.addPlayer(player1_empty)
      val desk4 = desk3.addPlayer(player1_empty)
      val desk5 = desk4.addPlayer(player1_empty)
      val desk6 = desk5.addPlayer(player1_empty)
      "have less than 4 players" in {
        desk1.lessThan4P should be(true)
        desk1.correctAmountOfPlayers should be(false)
        desk1.amountOfPlayers should be(0)
        desk2.lessThan4P should be(true)
        desk2.correctAmountOfPlayers should be(false)
        desk2.amountOfPlayers should be(1)
        desk3.lessThan4P should be(true)
        desk3.correctAmountOfPlayers should be(true)
        desk3.amountOfPlayers should be(2)
        desk4.lessThan4P should be(true)
        desk4.correctAmountOfPlayers should be(true)
        desk4.amountOfPlayers should be(3)
        desk5.lessThan4P should be(false)
        desk5.correctAmountOfPlayers should be(true)
        desk5.amountOfPlayers should be(4)
        desk6.lessThan4P should be(false)
        desk6.correctAmountOfPlayers should be(false)
        desk6.amountOfPlayers should be(5)
      }
    }
    "current Player won" should {
      val desk1 = Desk(onePlayerEmptyBoard, bag_7R0_8B0, emptyTable)
      val desk2 = desk1.takeTileFromBagToPlayer(player1_empty, tile7Red0)
      "have less than 4 players" in {
        desk1.currentPlayerWon() should be(true)
        desk2.currentPlayerWon() should be(false)
      }
    }
    "seeing board view" should {
      "see the correct one" in {
        desk.boardView should be(player1_board.board.tiles)
      }
    }
    "seeing table view" should {
      "see the correct one" in {
        desk.tableView should be(table_4R0__5B0_6Y0)
      }
    }
    "get player by name" should {
      "get the correct name" in {
        desk.getPlayerByName(player1_board.name).get should be(player1_board)
        desk.getPlayerByName(player2_board.name).isEmpty should be(true)
      }
    }
    "a player puts down a tile" should {
      val desk1 = desk.putDownTile(player1_board, tile1Red0)
      "lay that tile down" in {
        desk1.table should be(table_4R0__5B0_6Y0 + SortedSet(tile1Red0))
        desk1.getCurrentPlayer.board.tiles should be(player1_board.board.tiles.drop(1))
      }
    }
    "amount of players" should {
      "lay that tile down" in {
        desk.checkTable() should be(true)
      }
    }
    "checking street" should {
      val street = SortedSet(tile1Red0, Tile(2, Color.RED, 0), Tile(3, Color.RED, 0), Tile(4, Color.RED, 0))
      val pair = SortedSet(tile1Red0, Tile(1, Color.BLUE, 0), Tile(1, Color.YELLOW, 0), Tile(1, Color.GREEN, 0))
      "be correct when street is correct" in {
        desk.checkPair(pair) should be(true)
        desk.checkPair(street) should be(false)
        desk.checkStreet(street) should be(true)
        desk.checkStreet(pair) should be(false)
      }
    }
  }
}