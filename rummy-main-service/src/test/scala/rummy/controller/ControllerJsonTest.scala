package rummy.controller

import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.deskComp.deskBaseImpl.deskImpl.{Color, Tile}
import model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import model.fileIO.FileIOJson
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import rummy.controller.impl.Controller
import rummy.ui.UIConnector
import rummy.util.AnswerState._
import rummy.util.ControllerState._

import scala.collection.immutable.SortedSet

class ControllerJsonTest extends WordSpec with Matchers with ScalatestRouteTest {
  private val fileIo = new FileIOJson()
  "ControllerJson" when {
    val players = List[PlayerInterface]()
    val desk = Desk(players, Set(Tile(3, Color.BLUE, 0)), Set[SortedSet[TileInterface]]())
    val controller = Controller(desk, CREATE_DESK, MENU)
    UIConnector.updateController(controller)
    "converting a controller to Json" should {
      val jsonController = ControllerJson.controllerToJson(controller).toString()
      "should work" in {
        jsonController should be("""{"desk":{"amountOfPlayers":0,"bagSize":1,"setsSize":0,"bag":[{"value":3,"color":"BLUE","ident":0}],"players":[],"sets":[]},"answer":"Created a desk","state":"MENU"}""")
      }
    }
  }
}