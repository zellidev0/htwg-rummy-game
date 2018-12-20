package model

case class Tile(value: Int, color: Color.Value, ident: Int) extends Ordered[Tile] {
  val identifier: String = value.toString + color.toString.charAt(0) + ident

  override def compare(that: Tile): Int = if (this.identifier == that.identifier) 0 else if (this.value > that.value) 1 else -1
}
