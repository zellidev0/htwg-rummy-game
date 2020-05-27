package model.fileIO

import model.DeskInterface
import model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, Tile}
import model.deskComp.deskBaseImpl.{BoardInterface, Desk, PlayerInterface, TileInterface}
import play.api.libs.json._

import scala.collection.immutable.SortedSet
import scala.io.Source
import scala.util.Try

class FileIOJson {

  def load: Option[DeskInterface] = {
    val json: JsValue = Try(Json.parse(Source.fromFile("target/desk.json").getLines.mkString)).getOrElse(return None)
    jsonToDesk(json);
  }

  def jsonToDesk(deskAsJson: JsValue): Option[Desk] = {
    var players = List[PlayerInterface]()
    var bagOfTiles = Set[TileInterface]()
    for (i <- 0 until (deskAsJson \ "desk" \ "amountOfPlayers").get.toString.toInt) {
      val name = ((deskAsJson \ "desk" \ "players") (i) \ "name").as[String]
      var board: BoardInterface = Board(SortedSet[TileInterface]())
      for (j <- 0 until ((deskAsJson \ "desk" \ "players") (i) \ "tilesOnBoard").as[Int]) {
        val value = (((deskAsJson \ "desk" \ "players") (i) \ "board") (j) \ "value").as[Int]
        val color = (((deskAsJson \ "desk" \ "players") (i) \ "board") (j) \ "color").as[String]
        val ident = (((deskAsJson \ "desk" \ "players") (i) \ "board") (j) \ "ident").as[Int]
        board = board add Tile(value, Color.colorFromString(color), ident)
      }
      players = players :+ Player(name, board)
    }
    for (i <- 0 until (deskAsJson \ "desk" \ "bagSize").get.toString.toInt) {
      val value = ((deskAsJson \ "desk" \ "bag") (i) \ "value").as[Int]
      val color = ((deskAsJson \ "desk" \ "bag") (i) \ "color").as[String]
      val ident = ((deskAsJson \ "desk" \ "bag") (i) \ "ident").as[Int]

      bagOfTiles = bagOfTiles + Tile(((deskAsJson \ "desk" \ "bag") (i) \ "value").as[Int],
        Color.colorFromString(((deskAsJson \ "desk" \ "bag") (i) \ "color").as[String]),
        ((deskAsJson \ "desk" \ "bag") (i) \ "ident").as[Int])
    }
    var ssets = Set[SortedSet[TileInterface]]()
    for (i <- 0 until (deskAsJson \ "desk" \ "setsSize").get.toString.toInt) {
      var sortedSet = SortedSet[TileInterface]()
      for (j <- 0 until ((deskAsJson \ "desk" \ "sets") (i) \ "setSize").as[Int]) {
        val value = (((deskAsJson \ "desk" \ "sets") (i) \ "struct") (j) \ "value").as[Int]
        val color = (((deskAsJson \ "desk" \ "sets") (i) \ "struct") (j) \ "color").as[String]
        val ident = (((deskAsJson \ "desk" \ "sets") (i) \ "struct") (j) \ "ident").as[Int]
        sortedSet = sortedSet + Tile(value, Color.colorFromString(color), ident)
      }
      ssets = ssets + sortedSet
    }
    Some(Desk(players, bagOfTiles, ssets))
  }

  def save(desk: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("target/desk.json"))
    pw.write(Json.prettyPrint(deskToJson(desk)))
    pw.close()
  }
  def deskToJson(desk: DeskInterface) =
    Json.obj(
      "desk" -> Json.obj(
        "amountOfPlayers" -> JsNumber(desk.amountOfPlayers),
        "bagSize" -> JsNumber(desk.bagOfTiles.size),
        "setsSize" -> JsNumber(desk.table.size),
        "bag" -> Json.toJson(
          for {
            tile <- desk.bagOfTiles
          } yield {
            Json.toJson(tile)
          }
        ),
        "players" -> Json.toJson(
          for {
            player <- desk.players
          } yield {
            Json.toJson(player)
          }
        ),
        "sets" -> Json.toJson(
          for {
            set <- desk.table
          } yield {
            Json.obj(
              "setSize" -> set.size,
              "struct" -> Json.toJson(
                for {
                  tile <- set
                } yield {
                  Json.toJson(tile)
                }
              )
            )
          }
        )
      )
    )

  implicit val tileWrites: Writes[TileInterface] = new Writes[TileInterface] {
    def writes(tile: TileInterface) = Json.obj(
      "value" -> tile.value,
      "color" -> tile.color.toString,
      "ident" -> tile.ident
    )
  }

  implicit val playerWrites: Writes[PlayerInterface] = new Writes[PlayerInterface] {
    def writes(player: PlayerInterface) = Json.obj(
      "name" -> player.name,
      "tilesOnBoard" -> player.tiles.size,
      "board" -> Json.toJson(
        for {
          tile <- player.tiles
        } yield {
          Json.toJson(tile)
        }
      )
    )
  }
  def toJson(grid: DeskInterface): JsObject = deskToJson(grid)

}
