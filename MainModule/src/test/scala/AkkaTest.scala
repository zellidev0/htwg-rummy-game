import akka.http.scaladsl.testkit.ScalatestRouteTest
import model.deskComp.deskBaseImpl.deskImpl.{ Board, Color, Player, Tile }
import model.deskComp.deskBaseImpl.{ Desk, PlayerInterface, TileInterface }
import org.scalatest.{ Matchers, WordSpec }

import scala.collection.immutable.SortedSet

class AkkaTest extends WordSpec with Matchers with ScalatestRouteTest {

  "Main service" when {
    val players = List[PlayerInterface]()
    val desk    = Desk(players, Set(Tile(3, Color.BLUE, 0), Tile(5, Color.RED, 0)), Set[SortedSet[TileInterface]]())
    UIConnector.controller = Controller(desk, AnswerState.CREATE_DESK, ControllerState.MENU)
    val akka: Akka = new Akka(UIConnector)
    "creating a game" when {
      "return the game" in {
        Get("/menu/create") ~> akka.menuRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
        Get("/menu/load") ~> akka.menuRoute ~> check {
          responseAs[String] shouldEqual "Wrong server state. Use endpoint only when in MENU state"
        }
      }
    }
    "state" should {
      "return the state" in {
        Get("/state") ~> akka.state ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
        Get("/players/finish") ~> akka.playerRoute ~> check {
          responseAs[String] shouldEqual "Wrong server state. Use endpoint only when in INSERTING_NAMES state"
        }
      }
    }
    "player route accessing" should {
      "player input not finished" in {
        Get("/players/finish") ~> akka.playerRoute ~> check {
          responseAs[String] shouldEqual "Wrong server state. Use endpoint only when in INSERTING_NAMES state"
        }
      }
      "add player work" in {
        Get("/players/add?name=max") ~> akka.playerRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
      }
      "undo adding player" in {
        Get("/players/add?name=max") ~> akka.playerRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
      }
      "redo adding player" in {
        Get("/players/add?name=max") ~> akka.playerRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
      }
    }
    "next route accessing" should {
      "should not work in current state" in {
        Get("/next/switch") ~> akka.nextRoute ~> check {
          responseAs[String] shouldEqual "Wrong server state. Use endpoint only when in INSERTING_NAMES state"
        }
      }
      "should work in current state" in {
        UIConnector.controller.addPlayerAndInit("max", 12)
        UIConnector.controller.nameInputFinished();
        Get("/next/switch") ~> akka.nextRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
      }
      "add player work" in {
        Get("/next/store") ~> akka.nextRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
      }
    }
    "plating a game" when {
      "done" in {
        Get("/game/done") ~> akka.gameRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
        Get("/menu/layDown?1R0") ~> akka.gameRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
        Get("/menu/undo") ~> akka.gameRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
        Get("/menu/redo") ~> akka.gameRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
        Get("/menu/moveTile?from=1R0&to=1B1") ~> akka.gameRoute ~> check {
          responseAs[String] shouldEqual UIConnector.controller.toJson.toString()
        }
      }
    }
  }

}
