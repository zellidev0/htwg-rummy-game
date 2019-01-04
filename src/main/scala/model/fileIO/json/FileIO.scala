package model.fileIO.json

import model.DeskInterface
import model.component.Desk
import model.component.component.component.Board
import model.component.component.{BoardInterface, PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface
import play.api.libs.json.{JsValue, Json, Writes}
import util.UtilMethods

import scala.collection.SortedSet
import scala.io.Source

class FileIO extends FileIOInterface {

  override def load: DeskInterface = {
    var desk: DeskInterface = null
    val source: String = Source.fromFile("desk.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val amountOfPlayer = (json \ "desk" \ "amountOfPlayer").get.toString.toInt
    var players = Set[PlayerInterface]
    var bagOfTiles = Set[TileInterface]
    var ssets = Set[SortedSet[TileInterface]]
    for (playerNodes <- json \ "desk" \ "players"){
      for (player <- playerNodes){
        val playerName = (json \\ "name")(player).as[String]
        val playerNumber = (json \\ "number")(player).as[Int]
        val playerState = (json \\ "state")(player).as[String]
        var board = Board(SortedSet[TileInterface]())

      }
    }

    desk
  }

  override def save(grid: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("desk.json"))
    pw.write(Json.prettyPrint(deskToJson(grid)))
    pw.close
  }

  implicit val tileWrites = new Writes[TileInterface] {
    def writes(tile: TileInterface) = Json.obj(
      "value" -> tile.getValue,
      "color" -> tile.getColor,
      "ident" -> tile.identifier
    )
  }

  implicit val playerWrites = new Writes[PlayerInterface] {
    def writes(player: PlayerInterface) = Json.obj(
      "value" -> player.getName,
      "color" -> player.getNumber,
      "ident" -> player.getState,
      "board" -> player.getTiles
    )
  }

  def deskToJson(desk: DeskInterface) = {
    Json.obj(
      "desk" -> Json.obj(
        "amountOfPlayers" -> Json.toJson(desk.amountOfPlayers),
        "bag" -> Json.toJson(
          for {
            tile <- desk.bagOfTiles
          } yield {
            Json.toJson(4)
          }
        )
      )
    )
  }


}
