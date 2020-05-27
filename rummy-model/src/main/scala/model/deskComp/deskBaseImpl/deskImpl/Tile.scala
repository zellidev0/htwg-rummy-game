package model.deskComp.deskBaseImpl.deskImpl

import model.deskComp.deskBaseImpl.TileInterface

import scala.util.Try

case class Tile(value: Int, color: Color.Value, ident: Int) extends TileInterface {

  override def compare(that: TileInterface): Int = {
    if (this.value > that.value) {
      1
    } else if (this.value < that.value) {
      -1
    } else {
      if (this.ident < that.ident) {
        -1
      } else if (this.ident > that.ident) {
        1
      } else {
        if (Color.beginningChar(this.color) > Color.beginningChar(that.color)) {
          1
        } else if (this == that) 0 else -1

      }
    }
  }

  override def toString: String =
    value + Color.beginningChar(color) + ident

}

object Tile {

  /** Creates a tile from a string representation if it's a valid string representation. */
  def stringToTile(string: String): Option[TileInterface] =
    Some(Tile(
      parseValue(string).getOrElse(return None),
      parseColor(string).getOrElse(return None),
      parseIdent(string).getOrElse(return None)
    ))

  def parseValue(string: String): Option[Int] =
    Try(Integer.parseInt(string.substring(0, string.length - 2))).fold(_ => None, {
      case num if 1 until 14 contains num => Some(num)
      case _ => None
    })

  def parseColor(string: String): Option[Color.Value] =
    Try(string.charAt(string.length - 2)).getOrElse(None) match {
      case 'R' => Some(Color.RED)
      case 'B' => Some(Color.BLUE)
      case 'Y' => Some(Color.YELLOW)
      case 'G' => Some(Color.GREEN)
      case _ => None
    }

  def parseIdent(string: String): Option[Int] =
    Try(Integer.parseInt(string.charAt(string.length - 1).toString)).fold(_ => None, {
      case num if 0 until 2 contains num => Some(num)
      case _ => None
    })
}
