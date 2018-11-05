package model


/*
* Was ist besser: tiles etwas hinzufügen und dann zurückgeben
* oder tiles als var un neues Objet erzeugen, das einhänge und das zurückgeben
*/
case class Board(tiles: Set[Tile]) {
  def size(): Int = tiles.size

  def add(tile: Tile): Board = Board(tiles + tile)

  def remove(tile: Tile): Board = Board(tiles.filterNot(t => tile.identifier == t.identifier))

  def contains(tile: Tile): Boolean = tiles contains tile
}
