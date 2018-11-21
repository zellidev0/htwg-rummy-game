package model

import org.scalatest.{Matchers, WordSpec}

class DeskSpec extends WordSpec with Matchers {


  "A Desk" when {
    "created with 4 players and 4 tiles" should {
      val set1 = Set[Tile]()
      val board1 = Board(set1)
      val set2 = Set[Tile]()
      val board2 = Board(set2)
      val player1 = Player("Name1", 0, board1)
      val player2 = Player("Name2", 1, board2)
      val players = Set(player1, player2)
      val tile1 = Tile(1, Color.RED, 0)
      val tile2 = Tile(1, Color.RED, 1)
      val tile3 = Tile(2, Color.RED, 0)
      val tile4 = Tile(2, Color.RED, 1)
      val bagOfTiles = Set(tile1, tile2, tile3, tile4)
      val tileTable = Set[Tile]()
      val desk = Desk(players, bagOfTiles, tileTable)
      "have a player Set with 4 players and a tile set with 4 tiles" in {
        desk.players.size should be(2)
        desk.bagOfTiles.size should be(4)
      }
    }
    "lay down a tile on desk" should {
      val set1 = Set[Tile](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1))
      val board1 = Board(set1)
      val player1 = Player("Name1", 0, board1)
      val players = Set(player1)
      val bagOfTiles = Set[Tile]()
      val tileTable = Set[Tile]()
      var desk = Desk(players, bagOfTiles, tileTable)
      val amountOfTilesOnTable = desk.tileTable.size // 0
      var amountOfTilesOnBoardOfPlayer1 = 0
      players.find(p => p.number == 0) match {
        case Some(value) => amountOfTilesOnBoardOfPlayer1 = value.board.amountOfTiles()
          desk = desk.layDownTileOnTable(value, Tile(1, Color.RED, 0))
      }
      "have one more tile on table" in {
        desk.tileTable.size should be(amountOfTilesOnTable + 1) // 1
        var play = desk.players.find(p => p.number == 0)
        play match {
          case Some(value) => value.board.amountOfTiles() should be(amountOfTilesOnBoardOfPlayer1 - 1) // 1
        }
      }
    }
    "user takes tile from bag" should {
      val set1 = Set[Tile]()
      val board1 = Board(set1)
      val player1 = Player("Name1", 0, board1)
      val players = Set(player1)
      val bagOfTiles = Set[Tile](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1))
      val tileTable = Set[Tile]()
      var desk = Desk(players, bagOfTiles, tileTable)
      val amountOfTilesInBag = desk.bagOfTiles.size // 2
      var amountOfTilesOnBoardOfPlayer1 = 0
      players.find(p => p.number == 0) match {
        case Some(value) => {
          amountOfTilesOnBoardOfPlayer1 = value.board.amountOfTiles()
          desk = desk.takeTileFromBag(value)
        }
      }
      "user have one more tile on board" in {
        var play = desk.players.find(p => p.number == 0)
        play match {
          case Some(value) => value.board.amountOfTiles() should be(amountOfTilesOnBoardOfPlayer1 + 1) // 1
        }
      }
      "bag of tile have one tile less" in {
        desk.bagOfTiles.size should be(amountOfTilesInBag - 1)

      }

    }
    "its palyer is next" should {
      val set1 = Set[Tile]()
      val board1 = Board(set1)
      val set2 = Set[Tile]()
      val board2 = Board(set2)
      var player1 = Player("Name1", 0, board1)
      var player2 = Player("Name2", 1, board2)
      val bagOfTiles = Set[Tile]()
      val tileTable = Set[Tile]()
      player1 = player1.changeState(State.TURN) //TURN
      player2 = player2.changeState(State.WAIT) //WAIT
      val players = Set(player1, player2)
      var desk = Desk(players, bagOfTiles, tileTable)
      desk = desk.switchToNextPlayer(player1, player2)
      "players status" in {
        var x = desk.players.find(p => p.number == player1.number)
        x match {
          case Some(value) => value.state should be(State.WAIT) //WAIT
        }
        var y = desk.players.find(p => p.number == player2.number)
        y match {
          case Some(value) => value.state should be(State.TURN) //WAIT
        }
      }
    }
  }
}
