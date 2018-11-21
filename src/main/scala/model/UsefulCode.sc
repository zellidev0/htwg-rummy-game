import model.{Board, Player, Tile}
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

switchToNextPlayer(player1, player2)

def switchToNextPlayer(current: Player, next: Player): Unit = {

  val indexOfPlayer1 = players.indexWhere(p => p == current)
  val indexOfPlayer2 = players.indexWhere(p => p == next)
  val player1 = players.apply(indexOfPlayer1)
  val player2 = players.apply(indexOfPlayer2)

  print(player1.state)
  print(player2.state)
  val player1New = player1.changeState(State.WAIT)
  val player2New = player2.changeState(State.TURN)
  print(player1New.state)
  print(player2New.state)
  players.update(indexOfPlayer1, player1New)
  players.update(indexOfPlayer2, player2New)
  print(players)
}


