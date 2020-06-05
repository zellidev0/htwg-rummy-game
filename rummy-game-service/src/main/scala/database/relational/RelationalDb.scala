package database.relational

import database.PlayerDao
import model.DeskInterface
import model.deskComp.deskBaseImpl.PlayerInterface

object RelationalDb extends PlayerDao {
  private val mappings: CaseClassMapping.type = CaseClassMapping

  def create(desk: DeskInterface): Option[DeskInterface] = {
    try {
      val worked = mappings.create(desk.players.head)
      if (worked) {
        println("Saved player in database")
        Some(desk)
      } else {
        println("Error saving player in database")
        None
      }
    } catch {
      case _: Throwable => None
    }
  }

  def read(): Option[PlayerInterface] = {
    try {
      mappings.read()
    } catch {
      case _: Throwable => None
    }
  }

  def update(): Unit = {
    ???
  }

  def delete(): Unit = {
    ???
  }
}
