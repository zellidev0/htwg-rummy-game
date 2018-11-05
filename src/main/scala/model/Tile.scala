package model

import model.Color.Color

case class Tile(value: Int, color: Color, num: Int) {
  val identifier: String = value.toString + color.toString.charAt(0) + num

  override def equals(that: Any): Boolean =
    that match {
      case that: Tile => that.identifier.equals(this.identifier)
      case _ => false
    }

}
