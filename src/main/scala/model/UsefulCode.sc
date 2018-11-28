import model.{Board, Player, Tile}

import scala.collection.SortedSet
//import model.{Desk, Tile}
//
//def initializeBagOfTiles(): Desk = {
//  for (number <- 1 to amountOfTiles) {
//    for (color <- 0 to amountOfColors) {
//      for (num <- 0 to 1) {
//        bagOfTiles.update(number * (color + 1), Tile(number, colors.apply(color - 1), num))
//      }
//    }
//  }
//  return this
//}


val set1 = SortedSet[Tile]()
val board1 = Board(set1)
val set2 = SortedSet[Tile]()
val board2 = Board(set2)
var player1 = Player("Name1", 1, board1)
var player2 = Player("Name2", 2, board2)
val bagOfTiles = Set[Tile]()
val tileTable = Set[Tile]()
val players = Array(player1, player2)


var x = 2
def switchToNextPlayer(current: Player, next: Player): Unit = {

  val indexOfPlayer1 = players.indexWhere(p => p == current)
  val indexOfPlayer2 = players.indexWhere(p => p == next)
  val player1 = players.apply(indexOfPlayer1)
  val player2 = players.apply(indexOfPlayer2)


}

//
//
//test private method
//
//class PersonSpec extends FlatSpec with PrivateMethodTester {
//
//  "A Person" should "transform correctly" in {
//    val p1 = new Person(1)
//    val transform = PrivateMethod[Person]('transform)
//    assert(p2 === invokePrivate transform(p1))
//  }
//}
//
