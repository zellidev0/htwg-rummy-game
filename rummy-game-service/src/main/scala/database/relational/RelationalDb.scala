package database.relational

import database.PlayerDao
import model.DeskInterface
import model.deskComp.deskBaseImpl.PlayerInterface

object RelationalDb extends PlayerDao {
  private val playerMappings: PlayerMappings.type = PlayerMappings
  private val gameMappings: GameMappings.type = GameMappings

  def createPlayer(desk: DeskInterface): Option[DeskInterface] = {
    try {
      val worked = playerMappings.createPlayer(desk.players.head)
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

  def readPlayer(name:String): Option[PlayerInterface] = {
    try {
      playerMappings.readPlayer()
    } catch {
      case _: Throwable => None
    }
  }

  override def createGame(deskAsJosnString: String): Boolean = {
    try {
      val worked = gameMappings.createDesk(deskAsJosnString)
      if (worked) {
        println("Saved desk in database")
        true
      } else {
        println("Error saving desk in database")
        false
      }
    } catch {
      case _: Throwable => false
    }

  }

  override def readGame(): Option[String] = {
    try {
      gameMappings.readGame()
    } catch {
      case _: Throwable => None
    }
  }

}
