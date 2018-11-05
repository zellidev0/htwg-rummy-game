import model._

object Rummy {
  def main(args: Array[String]): Unit = {
    val set1 = Set[Tile]()
    val board1 = Board(set1)
    val set2 = Set[Tile]()
    val board2 = Board(set2)
    val player1 = Player("Name1", 1, board1)
    val player2 = Player("Name2", 2, board2)
    val players = Array(player1, player2)
    val tile1 = Tile(1, Color.RED, 0)
    var tile2: Tile = Tile(1, Color.RED, 1)
    val tile3 = Tile(2, Color.RED, 0)
    val tile4 = Tile(2, Color.RED, 1)
    val bagOfTiles = Set(tile1, tile2, tile3, tile4)
    val tileTable = Set[Tile]()
    val desk = Desk(players, bagOfTiles, tileTable)
  }
}
