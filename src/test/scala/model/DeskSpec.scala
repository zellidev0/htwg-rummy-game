package model

import org.scalatest.{Matchers, WordSpec}

class DeskSpec extends WordSpec with Matchers {


  "A Desk" when {
    "created with 4 players and 4 tiles" should {
      val set1 = Set[Tile]()
      val board1 = Board(set1)
      val set2 = Set[Tile]()
      val board2 = Board(set2)
      val player1 = Player("Name1", 1, board1)
      val player2 = Player("Name2", 2, board2)
      val players = Array(player1, player2)
      val tile1 = Tile(1, Color.RED, 0)
      val tile2 = Tile(1, Color.RED, 1)
      val tile3 = Tile(2, Color.RED, 0)
      val tile4 = Tile(2, Color.RED, 1)
      val bagOfTiles = Set(tile1, tile2, tile3, tile4)
      val tileTable = Set[Tile]()
      val desk = Desk(players, bagOfTiles, tileTable)
      "have a player array with 4 players and a tile set with 4 tiles" in {
        desk.players.length should be(2)
        desk.bagOfTiles.size should be(4)
      }
    }
    "lay down a tile on desk" should {
      val set1 = Set[Tile](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1))
      val board1 = Board(set1)
      val set2 = Set[Tile]()
      val board2 = Board(set2)
      val player1 = Player("Name1", 1, board1)
      val player2 = Player("Name2", 2, board2)
      val players = Array(player1, player2)
      val bagOfTiles = Set[Tile]()
      val tileTable = Set[Tile]()
      var desk = Desk(players, bagOfTiles, tileTable)
      val amountOfTilesOnTable = desk.tileTable.size // 0
      val amountOfTilesOnBoardOfPlayer1 = players.apply(0).board.size() // 2
      desk = desk.layDownTileOnTable(players.apply(0), Tile(1, Color.RED, 0))
      "have one more tile on table" in {
        desk.tileTable.size should be(amountOfTilesOnTable + 1) // 1
        desk.players.apply(0).board.size() should be(amountOfTilesOnBoardOfPlayer1 - 1) // 1
      }
    }
    "user takes tile from bag to board " should {
      val set1 = Set[Tile]()
      val board1 = Board(set1)
      val set2 = Set[Tile]()
      val board2 = Board(set2)
      val player1 = Player("Name1", 1, board1)
      val player2 = Player("Name2", 2, board2)
      val players = Array(player1, player2)
      val bagOfTiles = Set[Tile](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1))
      val tileTable = Set[Tile]()
      var desk = Desk(players, bagOfTiles, tileTable)
      val amountOfTilesInBag = desk.bagOfTiles.size // 2
      val amountOfTilesOnBoardOfPlayer1 = players.apply(0).board.size() // 0
      desk = desk.takeTileFromBag(players.apply(0))
      "have one more tile on table" in {
        desk.bagOfTiles.size should be(amountOfTilesInBag - 1) // 1
        desk.players.apply(0).board.size() should be(amountOfTilesOnBoardOfPlayer1 + 1) // 1
      }
    }
    "its palyer is next" should {
      val set1 = Set[Tile]()
      val board1 = Board(set1)
      val set2 = Set[Tile]()
      val board2 = Board(set2)
      var player1 = Player("Name1", 1, board1)
      var player2 = Player("Name2", 2, board2)
      val bagOfTiles = Set[Tile]()
      val tileTable = Set[Tile]()
      player1 = player1.changeState(State.TURN) //TURN
      player2 = player2.changeState(State.WAIT) //WAIT
      val players = Array(player1, player2)
      val desk = Desk(players, bagOfTiles, tileTable)
      val current = desk.switchToNextPlayer(player1, player2)
      "players status" in{
        current.players.apply(0).state should be(State.WAIT) //WAIT
        current.players.apply(1).state should be(State.TURN) //TURN
      }
    }
  }
}
