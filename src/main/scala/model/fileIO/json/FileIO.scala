package model.fileIO.json


import model.DeskInterface
import model.component.Desk
import model.component.component.component._
import model.component.component.{BoardInterface, PlayerInterface, TileInterface}
import model.fileIO.FileIOInterface
import play.api.libs.json._

import scala.collection.SortedSet
import scala.io.Source

class FileIO extends FileIOInterface {

  override def load: DeskInterface = {
    var desk: DeskInterface = null
    val json: JsValue = Json.parse(Source.fromFile("desk.json").getLines.mkString)
    var players = Set[PlayerInterface]()
    var bagOfTiles = Set[TileInterface]()
    for (i <- 0 until (json \ "desk" \ "amountOfPlayers").get.toString.toInt) {
      val name = ((json \ "desk" \ "players") (i) \ "name").as[String]
      val number = ((json \ "desk" \ "players") (i) \ "number").as[Int]
      val state = ((json \ "desk" \ "players") (i) \ "state").as[String]
      var board: BoardInterface = Board(SortedSet[TileInterface]())
      for (j <- 0 until ((json \ "desk" \ "players") (i) \ "tilesOnBoard").as[Int]) {
        val z = (((json \ "desk" \ "players") (i) \ "board") (j) \ "value").as[Int]
        //        board = board + Tile(z.as[Int], Color.colorFromString(z.as[String]), z.as[Int])
        board = board
      }
      players = players + Player(name, number, board, State.stringToState(state))
    }
    for (i <- 0 until (json \ "desk" \ "bagSize").get.toString.toInt) {
      bagOfTiles = bagOfTiles + Tile(((json \ "desk" \ "bag") (i) \ "value").as[Int], Color.colorFromString(((json \ "desk" \ "bag") (i) \ "color").as[String]), ((json \ "desk" \ "bag") (i) \ "ident").as[Int])
    }
    var ssets = Set[SortedSet[TileInterface]]()
    for (i <- 0 until (json \ "desk" \ "setsSize").get.toString.toInt) {
      var sortedSet = SortedSet[TileInterface]()
      //      for (i <- 0 until (json \\ "setSize") (i).as[Int]) {
      //        sortedSet = sortedSet + Tile((json \\ "value") (i).as[Int], Color.colorFromString((json \\ "color") (i).as[String]), (json \\ "ident") (i).as[Int])
      //      }
      ssets = ssets + sortedSet
    }


    desk = Desk(players, bagOfTiles, ssets)
    desk
  }


  override def save(grid: DeskInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("desk.json"))
    pw.write(Json.prettyPrint(deskToJson(grid)))
    pw.close()
  }

  implicit val tileWrites = new Writes[TileInterface] {
    def writes(tile: TileInterface) = Json.obj(
      "value" -> tile.getValue,
      "color" -> tile.getColor.toString,
      "ident" -> tile.getIdent
    )
  }


  implicit val playerWrites = new Writes[PlayerInterface] {
    def writes(player: PlayerInterface) = Json.obj(
      "name" -> player.getName,
      "number" -> player.getNumber,
      "state" -> player.getState,
      "tilesOnBoard" -> player.getTiles.size,
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
        "bagSize" -> JsNumber(desk.bagOfTiles.size),
        "setsSize" -> JsNumber(desk.sets.size),
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
            set <- desk.sets
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
