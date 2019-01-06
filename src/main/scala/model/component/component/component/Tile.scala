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
}
