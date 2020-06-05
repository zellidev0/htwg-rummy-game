package player.database

import model.DeskInterface
import model.deskComp.deskBaseImpl.PlayerInterface

trait PlayerDao {

  def create(desk: DeskInterface): Option[DeskInterface]

  def read(): Option[PlayerInterface]

  def update(): Unit

  def delete(): Unit
}
