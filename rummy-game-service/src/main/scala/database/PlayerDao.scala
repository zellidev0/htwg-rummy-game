package database

import model.DeskInterface
import model.deskComp.deskBaseImpl.PlayerInterface

trait PlayerDao {

  def createPlayer(desk: DeskInterface): Option[DeskInterface]

  def readPlayer(name: String): Option[PlayerInterface]

  def createGame(deskAsJsonString: String): Boolean

  def readGame(): Option[String]

}