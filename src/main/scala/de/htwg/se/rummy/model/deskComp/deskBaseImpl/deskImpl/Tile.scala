package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

import com.google.inject.Inject
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface


case class Tile @Inject()(value: Int, color: Color.Value, ident: Int) extends TileInterface {

  override def compare(that: TileInterface): Int = if (this.identifier == that.identifier) 0 else if (this.value > that.value) 1 else -1

  override def equals(obj: Any): Boolean = obj.isInstanceOf[TileInterface] && obj.asInstanceOf[Tile].identifier == this.identifier

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
