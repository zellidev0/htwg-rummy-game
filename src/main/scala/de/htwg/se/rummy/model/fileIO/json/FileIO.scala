package de.htwg.se.rummy.model.fileIO.json

import com.google.inject.Inject
import de.htwg.se.rummy.model.deskComp.deskBaseImpl
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl._
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.Desk
import de.htwg.se.rummy.model.fileIO.FileIOInterface
import play.api.libs.json._

import scala.collection.immutable.SortedSet
import scala.io.Source

class FileIO @Inject() extends FileIOInterface {

  override def load: Desk = {
    var desk: Desk = null
    val json: JsValue = Json.parse(Source.fromFile("target/desk.json").getLines.mkString)
    var players = Set[Player]()
    var bagOfTiles = Set[Tile]()
    for (i <- 0 until (json \ "desk" \ "amountOfPlayers").get.toString.toInt) {
      val name = ((json \ "desk" \ "players") (i) \ "name").as[String]
      val number = ((json \ "desk" \ "players") (i) \ "number").as[Int]
      val state = ((json \ "desk" \ "players") (i) \ "state").as[String]
      var board: Board = Board(SortedSet[Tile]())
      for (j <- 0 until ((json \ "desk" \ "players") (i) \ "tilesOnBoard").as[Int]) {
        val value = (((json \ "desk" \ "players") (i) \ "board") (j) \ "value").as[Int]
        val color = (((json \ "desk" \ "players") (i) \ "board") (j) \ "color").as[String]
        val ident = (((json \ "desk" \ "players") (i) \ "board") (j) \ "ident").as[Int]
        board = board + Tile(value, Color.colorFromString(color), ident)
      }
      players = players + Player(name, number, board, State.stringToState(state))
    }
    for (i <- 0 until (json \ "desk" \ "bagSize").get.toString.toInt) {
      val value = ((json \ "desk" \ "bag") (i) \ "value").as[Int]
      val color = ((json \ "desk" \ "bag") (i) \ "color").as[String]
      val ident = ((json \ "desk" \ "bag") (i) \ "ident").as[Int]

      bagOfTiles = bagOfTiles + Tile(((json \ "desk" \ "bag") (i) \ "value").as[Int], Color.colorFromString(((json \ "desk" \ "bag") (i) \ "color").as[String]), ((json \ "desk" \ "bag") (i) \ "ident").as[Int])
    }
    var ssets = Set[SortedSet[Tile]]()
    for (i <- 0 until (json \ "desk" \ "setsSize").get.toString.toInt) {
      var sortedSet = SortedSet[Tile]()
      for (j <- 0 until ((json \ "desk" \ "sets") (i) \ "setSize").as[Int]) {
        val value = (((json \ "desk" \  "sets") (i) \ "struct") (j) \ "value").as[Int]
        val color = (((json \ "desk" \  "sets") (i) \ "struct") (j) \ "color").as[String]
        val ident = (((json \ "desk" \  "sets") (i) \ "struct") (j) \ "ident").as[Int]
        sortedSet = sortedSet + Tile(value, Color.colorFromString(color), ident)
      }
      ssets = ssets + sortedSet
    }


    desk = deskBaseImpl.Desk(players, bagOfTiles, ssets)
    desk
  }


  override def save(grid: Desk): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("target/desk.json"))
    pw.write(Json.prettyPrint(deskToJson(grid)))
    pw.close()
  }

  implicit val tileWrites = new Writes[Tile] {
    def writes(tile: Tile) = Json.obj(
      "value" -> tile.value,
      "color" -> tile.color.toString,
      "ident" -> tile.ident
    )
  }


  implicit val playerWrites = new Writes[Player] {
    def writes(player: Player) = Json.obj(
      "name" -> player.name,
      "number" -> player.number,
      "state" -> player.state,
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

  def deskToJson(desk: Desk) = {
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
