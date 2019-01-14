package model.component.component.component

import model.component.component.TileInterface

case class Tile(private val value: Int, private val color: Color.Value, private val ident: Int) extends TileInterface {

  override def compare(that: TileInterface): Int = if (this.identifier == that.identifier) 0 else if (this.value > that.getValue) 1 else -1
  override def getValue: Int = value
  override def getColor: Color.Value = color
  override def getIdent: Int = ident
  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[TileInterface]) {
      if (obj.asInstanceOf[Tile].identifier == this.identifier) {
        return true
      }
    }
    false
  }
  override def stringToTile(string: String): TileInterface = {
    val color = string.charAt(string.length - 2) match {
      case 'R' => Color.RED
      case 'B' => Color.BLUE
      case 'Y' => Color.YELLOW
      case 'G' => Color.GREEN
    }
    Tile(Integer.parseInt(string.substring(0, string.length - 2)), color, Integer.parseInt(string.charAt(string.length - 1).toString))

  }
}
