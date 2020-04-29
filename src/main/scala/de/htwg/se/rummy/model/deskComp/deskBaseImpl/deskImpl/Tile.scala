package de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl

import com.google.inject.Inject
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.TileInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.Color.beginningChar


case class Tile @Inject()(value: Int, color: Color.Value, ident: Int) extends TileInterface {

  override def compare(that: TileInterface): Int =
    this.value - that.value

  override def toString: String =
    value + beginningChar(color) + ident
}

object Tile {
  def stringToTile(string: String): TileInterface = {
    Tile(Integer.parseInt(string.substring(0, string.length - 2)),
      Color.colorToChar(string.charAt(string.length - 2)),
      Integer.parseInt(string.charAt(string.length - 1).toString))
  }
}
