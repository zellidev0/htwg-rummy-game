package model.fileIO.json

import model.DeskInterface
import model.component.component.{PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface
import play.api.libs.json.{JsNumber, Json, Writes}

class FileIO extends FileIOInterface {

  override def load: DeskInterface = {
    var desk: DeskInterface = null
    //    val source: String = Source.fromFile("desk.json").getLines.mkString
    //    val json: JsValue = Json.parse(source)
    //    val amountOfPlayer = (json \ "desk" \ "amountOfPlayer").get.toString.toInt
    //    var players = Set[PlayerInterface]()
    //    var bagOfTiles = Set[TileInterface]()
    //    var ssets = Set[SortedSet[TileInterface]]()
    //    for (playerNodes <- json \ "desk" \ "players") {
    //      for (player <- playerNodes) {
    //        val playerName = (json \\ "name") (player).as[String]
    //        val playerNumber = (json \\ "number") (player).as[Int]
    //        val playerState = (json \\ "state") (player).as[String]
    //        var board = Board(SortedSet[TileInterface]())
    //
    //      }
    //    }
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
      "ident" -> tile.identifier
    )
  }

  implicit val playerWrites = new Writes[PlayerInterface] {
    def writes(player: PlayerInterface) = Json.obj(
      "name" -> player.getName,
      "number" -> player.getNumber,
      "state" -> player.getState,
      "board" -> Json.toJson(
        for {
          tile <- player.getTiles
        } yield {
          Json.toJson(tile)
        }
      )
    )
  }

  def deskToJson(desk: DeskInterface) = {
    Json.obj(
      "desk" -> Json.obj(
        "amountOfPlayers" -> JsNumber(desk.amountOfPlayers),
        "bag" -> Json.toJson(
          for {
            tile <- desk.bagOfTiles
          } yield {
            Json.toJson(tile)
          }
        )
      )
    )
  }


}
