package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl


case class Tile (value: Int, color: Color.Value, ident: Int) extends Ordered[Tile] {

  override def compare(that: Tile): Int =
    if (this.value == that.value && this.color == that.color && this.ident == that.ident) {
      0
    } else if (this.value > that.value) {
      1
    } else {
      -1
    }

  override def equals(obj: Any): Boolean = compare(obj.asInstanceOf[Tile]) == 0

  override def toString: String = value + color.toString.charAt(0).toString + ident
}

object Tile {
  def stringToTile(string: String): Tile = {
    val color = string.charAt(string.length - 2) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    Tile(Integer.parseInt(string.substring(0, string.length - 2)), color, Integer.parseInt(string.charAt(string.length - 1).toString))
  }
}
