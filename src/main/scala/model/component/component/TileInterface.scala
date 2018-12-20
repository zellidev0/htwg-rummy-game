package model.component.component

import model.component.component.component.Color


trait TileInterface extends Ordered[TileInterface] {

  override def compare(that: TileInterface): Int

  def getValue: Int
  def getColor: Color.Value
  def getIdent: Int
  def identifier: String = getValue.toString + getColor.toString.charAt(0) + getIdent

}
