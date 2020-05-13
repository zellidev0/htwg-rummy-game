package de.htwg.se.rummy.model.fileIO.json

import com.google.inject.Inject
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl._
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{BoardInterface, PlayerInterface, TileInterface}
import de.htwg.se.rummy.model.fileIO.FileIOInterface
import play.api.libs.json._

import scala.collection.immutable.SortedSet
import scala.io.Source
import scala.util.Try

class FileIO @Inject() extends FileIOInterface {

  override def load: Option[DeskInterface] = {
    val json = Try(Json.parse(Source.fromFile("target/desk.json").getLines.mkString)).getOrElse(return None)
    var players = List[PlayerInterface]()
    var bagOfTiles = Set[TileInterface]()
    for (i <- 0 until (json \ "desk" \ "amountOfPlayers").get.toString.toInt) {
      val name = ((json \ "desk" \ "players") (i) \ "name").as[String]
      val state = ((json \ "desk" \ "players") (i) \ "hasTurn").as[Boolean]
      var board: BoardInterface = Board(SortedSet[TileInterface]())
      for (j <- 0 until ((json \ "desk" \ "players") (i) \ "tilesOnBoard").as[Int]) {
        val value = (((json \ "desk" \ "players") (i) \ "board") (j) \ "value").as[Int]
        val color = (((json \ "desk" \ "players") (i) \ "board") (j) \ "color").as[String]
        val ident = (((json \ "desk" \ "players") (i) \ "board") (j) \ "ident").as[Int]
        board = board add Tile(value, Color.colorFromString(color), ident)
      }
      players = players :+ Player(name, board, state)
    }
    for (i <- 0 until (json \ "desk" \ "bagSize").get.toString.toInt) {
      val value = ((json \ "desk" \ "bag") (i) \ "value").as[Int]
      val color = ((json \ "desk" \ "bag") (i) \ "color").as[String]
      val ident = ((json \ "desk" \ "bag") (i) \ "ident").as[Int]

      bagOfTiles = bagOfTiles + Tile(((json \ "desk" \ "bag") (i) \ "value").as[Int], Color.colorFromString(((json \ "desk" \ "bag") (i) \ "color").as[String]), ((json \ "desk" \ "bag") (i) \ "ident").as[Int])
    }
    var ssets = Set[SortedSet[TileInterface]]()
    for (i <- 0 until (json \ "desk" \ "setsSize").get.toString.toInt) {
      var sortedSet = SortedSet[TileInterface]()
      for (j <- 0 until ((json \ "desk" \ "sets") (i) \ "setSize").as[Int]) {
        val value = (((json \ "desk" \  "sets") (i) \ "struct") (j) \ "value").as[Int]
        val color = (((json \ "desk" \  "sets") (i) \ "struct") (j) \ "color").as[String]
        val ident = (((json \ "desk" \  "sets") (i) \ "struct") (j) \ "ident").as[Int]
        sortedSet = sortedSet + Tile(value, Color.colorFromString(color), ident)
      }
      ssets = ssets + sortedSet
    }
    deskBaseImpl.Desk(players, bagOfTiles, ssets)
  }


  override def save(desk: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("target/desk.json"))
    pw.write(Json.prettyPrint(deskToJson(desk)))
    pw.close()
  }

  override def toJson(grid: DeskInterface): JsObject = deskToJson(grid)

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
      "hasTurn" -> player.hasTurn,
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

  def deskToJson(desk: DeskInterface) = {
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
  }


}
