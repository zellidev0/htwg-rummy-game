package game

import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, Tile}
import model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import model.fileIO.FileIOJson
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

import scala.collection.immutable.SortedSet


class GameServiceTest extends WordSpec with Matchers with ScalatestRouteTest {

  val fileIo = new FileIOJson()

  "The service" should {
    val player1 = Player("Name0", Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))))
    val player2 = Player("Name1", Board(SortedSet[TileInterface](Tile(2, Color.RED, 0))))
    val players = List[PlayerInterface](player1, player2)
    val service = GameService
    val desk: Desk = Desk(players, Set(), Set(
      SortedSet(Tile(3, Color.BLUE, 0)),
      SortedSet(Tile(5, Color.RED, 0))
    ))
    "move a tile when calling move tile path" in {
      val body = fileIo.deskToJson(desk).+("from", Json.toJson("3B0")).+("to", Json.toJson("5R0")).toString()
      Post().withEntity(body) ~> service.moveTilePath() ~> check {
        println(response.status)
        response.status.intValue() should be(200)
      }
    }
    "switch to the next player" in {
      Post() ~> service.createPath() ~> check {
        println(response.status)
        response.status.intValue() should be(200)
      }
    }
    "putTileDownPath to the next player" in {
      val body = fileIo.deskToJson(desk).+("tile", Json.toJson("1R0")).toString()
      Post().withEntity(body) ~> service.putTileDownPath() ~> check {
        println(response.status)
        response.status.intValue() should be(200)
      }
    }
    "check correct name" in {
      val obj = Json.obj("tile" -> "1R0")
      service.checkCorrectTile(obj.toString(), "tile").isDefined should be(true)
    }
  }
}