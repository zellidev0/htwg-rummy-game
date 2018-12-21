package model.fileIO.json

import model.DeskInterface
import model.component.component.{PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface
import play.api.libs.json.{JsValue, Json, Writes}

import scala.io.Source

class FileIO extends FileIOInterface {

  override def load: DeskInterface = {
    var desk: DeskInterface = null
    val source: String = Source.fromFile("grid.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val size = (json \ "grid" \ "size").get.toString.toInt
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
