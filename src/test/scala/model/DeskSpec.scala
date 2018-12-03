package model

import org.scalatest.{Matchers, WordSpec}

import scala.collection.SortedSet

class DeskSpec extends WordSpec with Matchers {


  "A Desk" when {
    "created with 2 players and 2 tiles" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]())), Player("Name2", 1, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[Tile]]())
      "have a player Set with 2 players and a tile set with 2 tiles" in {
        desk.amountOfPlayers should be(2)
        desk.bagOfTiles.size should be(2)
        desk.amountOfTilesOnTable should be(0)
      }

      "have a correct amount of players" in {
        desk.hasLessThan5Players should be(true)
        desk.hasMoreThan1Player should be(true)

      }

    }
    "created with 2 and gets added 3 more players" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]())), Player("Name2", 1, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[Tile]]())
      desk = desk.addPlayer(Player("Name3", 2, Board(SortedSet[Tile]())))
      desk = desk.addPlayer(Player("Name4", 3, Board(SortedSet[Tile]())))
      desk = desk.addPlayer(Player("Name5", 4, Board(SortedSet[Tile]())))
      "not have a correct amount of players" in {
        desk.hasLessThan5Players should be(false)
      }
    }

    "created with empty players" should {
      var desk = Desk(Set[Player](), Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[Tile]]())
      "have size 0" in {
        desk.players.size should be(0)
      }
      "after adding a player should have size 1" in {
        desk = desk.addPlayer(Player("Name1", 0, Board(SortedSet[Tile]())))
        desk.players.size should be(1)
      }

    }

    "layed down a tile on desk" should {
      val player1 = Player("Name1", 0, Board(SortedSet[Tile](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1))))
      val players = Set(player1)
      var desk = Desk(players, Set[Tile](), Set[SortedSet[Tile]]())
      val amountOfTilesOnTable = desk.amountOfTilesOnTable // 0
      var amountOfTilesOnBoardOfPlayer1 = 0
      players.find(p => p.number == 0) match {
        case Some(value) => amountOfTilesOnBoardOfPlayer1 = value.board.amountOfTiles()
          desk = desk.putDownTile(value, Tile(1, Color.RED, 0))
      }
      "have one more tile on table" in {
        desk.amountOfTilesOnTable should be(amountOfTilesOnTable + 1) // 1
        desk.tileSetContains(Tile(1, Color.RED, 0)) should be(true)
        desk.tileSetContains(Tile(1, Color.RED, 1)) should be(false)

      }
      "Player 1 have one tile less" in {
        var play = desk.players.find(p => p.number == 0)
        play match {
          case Some(value) => value.board.amountOfTiles() should be(amountOfTilesOnBoardOfPlayer1 - 1) // 1
        }
      }
      "have a tile in a tileSet" in {
        desk.setWithTile(Tile(1, Color.RED, 0)) match {
          case Some(value) =>
            value should be(SortedSet[Tile](Tile(1, Color.RED, 0)))
            value.size should be(1)
        }
      }

    }
    "user takes tile from bag" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set[Tile](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1)), Set[SortedSet[Tile]]())
      val amountOfTilesInBag = desk.bagOfTiles.size // 2
      var amountOfTilesOnBoardOfPlayer1 = 0
      players.find(p => p.number == 0) match {
        case Some(value) =>
          amountOfTilesOnBoardOfPlayer1 = value.board.amountOfTiles()
          desk = desk.takeTile(value)
      }
      "user have one more tile on board" in {
        desk.players.find(p => p.number == 0) match {
          case Some(value) => value.board.amountOfTiles() should be(amountOfTilesOnBoardOfPlayer1 + 1) // 1
        }
      }
      "bag of tile have one tile less" in {
        desk.bagOfTiles.size should be(amountOfTilesInBag - 1)
      }

    }

    "user moves tile to tile from bag" should {
      val players = Set(Player("Name1", 0, Board(SortedSet[Tile]())))
      var desk = Desk(players, Set[Tile](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1)), Set[SortedSet[Tile]]())
      val amountOfTilesInBag = desk.bagOfTiles.size // 2
      var amountOfTilesOnBoardOfPlayer1 = 0
      players.find(p => p.number == 0) match {
        case Some(value) =>
          amountOfTilesOnBoardOfPlayer1 = value.board.amountOfTiles()
          desk = desk.takeTile(value)
      }
      "user have one more tile on board" in {
        desk.players.find(p => p.number == 0) match {
          case Some(value) => value.board.amountOfTiles() should be(amountOfTilesOnBoardOfPlayer1 + 1) // 1
        }
      }
      "bag of tile have one tile less" in {
        desk.bagOfTiles.size should be(amountOfTilesInBag - 1)
      }

    }


    "two players switch turn, -> its player" should {
      val player1 = Player("Name1", 0, Board(SortedSet[Tile]()), state = State.TURN)
      val player2 = Player("Name2", 1, Board(SortedSet[Tile]()))
      val players = Set(player1, player2)
      val desk = Desk(players, Set[Tile](), Set[SortedSet[Tile]]()).switchToNextPlayer(player1, player2)
      "have status Wait (current)" in {
        desk.players.exists(p => p.number == player1.number && p.state == State.WAIT) should be(true)
      }
      "have status TURN (next)" in {
        desk.players.exists(p => p.number == player2.number && p.state == State.TURN) should be(true)
      }
    }
  }
}
