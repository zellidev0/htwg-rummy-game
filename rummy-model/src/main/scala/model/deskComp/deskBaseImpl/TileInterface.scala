package model.deskComp.deskBaseImpl

import model.deskComp.deskBaseImpl.deskImpl.Color

trait TileInterface extends Ordered[TileInterface] {

  val value: Int
  val color: Color.Value
  val ident: Int

  override def compare(that: TileInterface): Int
}
