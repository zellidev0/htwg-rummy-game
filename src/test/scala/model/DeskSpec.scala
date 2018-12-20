package model

import model.component.Desk
import model.component.component._
import model.component.component.component._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.SortedSet

class DeskSpec extends WordSpec with Matchers {


  "A Desk" when {
    "created with 2 players and 2 tiles" should {
      var desk = Desk(Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]())), Player("Name2", 1, Board(SortedSet[TileInterface]()))),
        Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[TileInterface]]())
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
    "created with 2 and gets added 3 more players" should {
      var desk = component.Desk(Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]())), Player("Name2", 1, Board(SortedSet[TileInterface]()))),
        Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[TileInterface]]())
      desk = desk.addPlayer(Player("Name3", 2, Board(SortedSet[TileInterface]())))
      desk = desk.addPlayer(Player("Name4", 3, Board(SortedSet[TileInterface]())))
      desk = desk.addPlayer(Player("Name5", 4, Board(SortedSet[TileInterface]())))
      "not have a correct amount of players" in {
        desk.lessThan4P should be(false)
        desk.correctAmountOfPlayers should be(false)
      }
    }
    "created with 2 and removes one player" should {
      var desk = component.Desk(Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), State.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]()))),
        Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[TileInterface]]())
      desk = desk.removePlayer(desk.nextP)
      "have only one player left" in {
        desk.amountOfPlayers should be(1)
      }
    }
    "created with empty players" should {
      var desk = component.Desk(Set[PlayerInterface](), Set(Tile(1, Color.RED, 0), Tile(2, Color.RED, 0)), Set[SortedSet[TileInterface]]())
      "have size 0" in {
        desk.players.size should be(0)
      }
      "after adding a player should have size 1" in {
        desk = desk.addPlayer(Player("Name1", 0, Board(SortedSet[TileInterface]())))
        desk.players.size should be(1)
      }

    }
    "layed down a tile on desk" should {
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1))), state = State.TURN))
      var desk = Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]]())
      var amountOfTilesOnBoardOfPlayer1 = players.find(p => p.getNumber == 0).get.getTiles.size
      desk = desk.putDownTile(desk.currentP, Tile(1, Color.RED, 0))
      "have one more tile on table" in {
        desk.setsContains(Tile(1, Color.RED, 0)) should be(true)
        desk.setsContains(Tile(1, Color.RED, 1)) should be(false)
      }
      "Player 1 have one tile less" in {
        desk.players.find(p => p.getNumber == 0).get.getTiles.size should be(amountOfTilesOnBoardOfPlayer1 - 1)
      }
      "have a tile in a tileSet" in {

        desk.sets.size should be(1)
      }
    }
    "user takes tile from bag" should {
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]())))
      var desk = component.Desk(players, Set[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1)), Set[SortedSet[TileInterface]]())
      val amountOfTilesInBag = desk.bagOfTiles.size // 2
      var amountOfTilesOnBoardOfPlayer1 = 0
      amountOfTilesOnBoardOfPlayer1 = players.find(p => p.getNumber == 0).get.getTiles.size
      desk = desk.takeTileFromBagToPlayer(players.find(p => p.getNumber == 0).get, desk.randomTileInBag)
      "user have one more tile on board" in {
        desk.players.find(p => p.getNumber == 0).get.getTiles.size should be(amountOfTilesOnBoardOfPlayer1 + 1)
      }
      "bag of tile have one tile less" in {
        desk.bagOfTiles.size should be(amountOfTilesInBag - 1)
      }

    }
    "user moves tile to tile from bag" should {
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]())))
      var desk = component.Desk(players, Set[TileInterface](Tile(1, Color.RED, 0), Tile(1, Color.RED, 1)), Set[SortedSet[TileInterface]]())
      val amountOfTilesInBag = desk.bagOfTiles.size // 2
      var x = players.find(p => p.getNumber == 0).get.getTiles.size
      desk = desk.takeTileFromBagToPlayer(players.find(p => p.getNumber == 0).get, desk.randomTileInBag)
      "user have one more tile on board" in {
        desk.players.find(p => p.getNumber == 0).get.getTiles.size should be(x + 1)
      }
      "bag of tile have one tile less" in {
        desk.bagOfTiles.size should be(amountOfTilesInBag - 1)
      }

    }
    "two players switch turn, -> its player" should {
      val player1 = Player("Name1", 0, Board(SortedSet[TileInterface]()), state = State.TURN)
      val player2 = Player("Name2", 1, Board(SortedSet[TileInterface]()))
      var desk = component.Desk(Set(player1, player2), Set[TileInterface](), Set[SortedSet[TileInterface]]())
      "have current player, which is player1)" in {
        desk.currentP should be(player1)
        desk.nextP should be(player2)
      }
      "have status Wait (current)" in {
        desk = desk.switchToNextPlayer(player1, player2)
        desk.players.exists(p => p.getNumber == player1.getNumber && p.getState == State.WAIT) should be(true)
        desk.previousP.getNumber should be(player1.getNumber)
      }
      "have status TURN (next)" in {
        desk.players.exists(p => p.getNumber == player2.getNumber && p.getState == State.TURN) should be(true)
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
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = State.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]())))
      var desk = component.Desk(players, Set[TileInterface](), setOfCorrectStreets)
      "be true when setOfCorrectStreets" in {
        desk.checkTable() should be(true)
      }
      "be true when setOfCorrectPairs" in {
        desk = component.Desk(players, Set[TileInterface](), setOfCorrectPairs)
        desk.checkTable() should be(true)
      }
      "be true when setOfWrongStreets" in {
        desk = component.Desk(players, Set[TileInterface](), setOfWrongStreets)
        desk.checkTable() should be(false)
      }
      "be true when setOfWrongPairs" in {
        desk = component.Desk(players, Set[TileInterface](), setOfWrongPairs)
        desk.checkTable() should be(false)
      }
    }
    "check street" should {
      val setOfCorrectStreets =
        Set(SortedSet[TileInterface](Tile(1, Color.GREEN, 0), Tile(2, Color.GREEN, 1), Tile(3, Color.GREEN, 0), Tile(4, Color.GREEN, 0), Tile(5, Color.GREEN, 0)), //Street 4 GREEN
          SortedSet[TileInterface](Tile(4, Color.RED, 0), Tile(5, Color.RED, 0), Tile(6, Color.RED, 0))) // Street 3 RED
      val setOfWrongStreets =
        Set(SortedSet[TileInterface](Tile(9, Color.BLUE, 1), Tile(11, Color.BLUE, 1), Tile(12, Color.BLUE, 1), Tile(13, Color.BLUE, 0)), // Street with missing one
          SortedSet[TileInterface](Tile(7, Color.RED, 0), Tile(8, Color.RED, 1))) // street with only 2
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = State.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]())))
      var desk = component.Desk(players, Set[TileInterface](), setOfCorrectStreets)
      "be true when setOfCorrectStreets" in {
        for (set <- desk.sets) {
          desk.checkStreet(set) should be(true)
          desk.checkPair(set) should be(false)
        }
      }
      "be false when setOfWrongStreets" in {
        desk = component.Desk(players, Set[TileInterface](), setOfWrongStreets)
        for (set <- desk.sets) {
          desk.checkStreet(set) should be(false)
          desk.checkPair(set) should be(false)
        }
      }
    }
    "check pair" should {
      val setOfCorrectPairs =
        Set(SortedSet[TileInterface](Tile(2, Color.GREEN, 0), Tile(2, Color.YELLOW, 0), Tile(2, Color.BLUE, 0)), // Pair 3 different
          SortedSet[TileInterface](Tile(8, Color.RED, 0), Tile(8, Color.BLUE, 0), Tile(8, Color.YELLOW, 0), Tile(8, Color.GREEN, 0))) // Pair 4 different
      val setOfWrongPairs =
        Set(SortedSet[TileInterface](Tile(10, Color.GREEN, 0), Tile(10, Color.GREEN, 1), Tile(10, Color.BLUE, 0)), // Pair of 3 where 2 same
          SortedSet[TileInterface](Tile(13, Color.YELLOW, 0), Tile(13, Color.GREEN, 0))) // Pair of 2
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = State.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]())))
      var desk = component.Desk(players, Set[TileInterface](), setOfCorrectPairs)
      "be true when setOfCorrectPair" in {
        for (set <- desk.sets) {
          desk.checkStreet(set) should be(false)
          desk.checkPair(set) should be(true)
        }
      }
      "be true when setOfWrongPair" in {
        desk = component.Desk(players, Set[TileInterface](), setOfWrongPairs)
        for (set <- desk.sets) {
          desk.checkStreet(set) should be(false)
          desk.checkPair(set) should be(false)
        }
      }
    }
    "one player puts last tile down" should {
      val tile = Tile(1, Color.RED, 0)
      var desk = component.Desk(Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface](tile)), state = State.TURN), Player("Name2", 1, Board(SortedSet[TileInterface]()))), Set[TileInterface](), Set[SortedSet[TileInterface]]())
      desk.currentPlayerWon() should be(false)
      desk = desk.putDownTile(desk.currentP, tile)
      "have that player win" in {
        desk.currentPlayerWon() should be(true)
      }
    }
    "move a tile form one set to another" should {
      val tile = Tile(4, Color.BLUE, 0)
      val tile2 = Tile(3, Color.BLUE, 0)
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface](tile)), state = State.TURN))
      var desk = component.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]](SortedSet(Tile(1, Color.BLUE, 0), Tile(2, Color.BLUE, 0), tile2), SortedSet()))
      desk = desk.putDownTile(desk.currentP, tile)
      desk = desk.moveTwoTilesOnDesk(tile, tile2)
      "have 4 tiles in Set on Deks" in {
        desk.sets.size should be(1)
      }
    }
    "taking up a tile" should {
      val tile = Tile(4, Color.BLUE, 0)
      val tile2 = Tile(3, Color.BLUE, 0)
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface](tile)), state = State.TURN))
      var desk = component.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]](SortedSet(tile2), SortedSet()))
      desk = desk.takeUpTile(desk.currentP, tile2)
      "have 4 tiles in Set on Deks" in {
        desk.sets.head.isEmpty should be(true)
      }
    }
    "adding to bag" should {
      val tile = Tile(4, Color.BLUE, 0)
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface]()), state = State.TURN))
      var desk = component.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]]())
      desk = desk.addToBag(tile)
      "have 4 tiles in Set on Deks" in {
        desk.bagOfTiles.size should be(1)
      }
    }
    "taking a tile from player to bag" should {
      val tile = Tile(4, Color.BLUE, 0)
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface](tile)), state = State.TURN))
      var desk = component.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]]())
      desk = desk.takeTileFromPlayerToBag(desk.currentP, tile)
      "have one less on players board and one more in bag" in {
        desk.bagOfTiles.size should be(1)
        desk.players.head.getTiles.size should be(0)
      }
    }
    "player won" should {
      val tile = Tile(4, Color.BLUE, 0)
      val players = Set[PlayerInterface](Player("Name1", 0, Board(SortedSet[TileInterface](tile)), state = State.TURN))
      var desk = component.Desk(players, Set[TileInterface](), Set[SortedSet[TileInterface]]())
      desk = desk.takeTileFromPlayerToBag(desk.currentP, tile)
      "be wrong" in {
        desk.currentPlayerWon() should be(true)
      }
    }
  }
}
