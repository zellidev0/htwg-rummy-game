package player

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.deskComp.deskBaseImpl.deskImpl.{Board, Color, Player, Tile}
import model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import model.fileIO.FileIOJson
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

import scala.collection.immutable.SortedSet


class PlayerServiceTest extends WordSpec with Matchers with ScalatestRouteTest {

  val fileIo = new FileIOJson()

  "The service" should {
    val player1 = Player("Name0", Board(SortedSet[TileInterface](Tile(1, Color.RED, 0))))
    val player2 = Player("Name1", Board(SortedSet[TileInterface](Tile(2, Color.RED, 0))))
    val players = List[PlayerInterface](player1, player2)
    val service = PlayerService(2)
    val desk: Desk = Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]]())
    "return the new desk with added player" in {
      val body = fileIo.deskToJson(desk).+("name", Json.toJson("mike")).toString()
      Post().withEntity(body) ~> service.addPath() ~> check {
        println(response.status)
        response.status.intValue() should be(200)
      }
    }
  }
}